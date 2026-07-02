package view;

import cn.hutool.core.collection.CollUtil;
import db.BreakoutMapper;
import db.MapperExecutor;
import dto.RecordRankTableModel;
import dto.User;
import ui.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

import static ui.GameFonts.RANK_TABLE_HEADER;
import static ui.GameFonts.RANK_TABLE_TEXT;

public class LeaderboardFrame extends AbstractGameFrame {

    private static final long serialVersionUID = 4063485659525045767L;

    /**
     * 排行榜底部操作面板
     */
    private final JPanel actionPanel = new JPanel(null);

    /**
     * 排行榜记录
     */
    private final List<User> records;

    private LeaderboardFrame(List<User> records) {

        if (!CollUtil.isEmpty(this.records = records)) {
            this.openWindow(GameWindowConfig
                    .of("英雄榜", WINDOW_X, WINDOW_Y, WINDOW_WIDTH, WINDOW_HEIGHT)
                    .setLayout(new BorderLayout()).setCloseOperation(WindowConstants.EXIT_ON_CLOSE));
        }
    }

    /**
     * 打开排行榜
     *
     * @param parent 父组件
     */
    public static void open(Component parent) {

        List<User> records = MapperExecutor.query(BreakoutMapper::listRecordRanks);

        if (!CollUtil.isEmpty(records)) {
            SwingWindows.hide(parent);
            new LeaderboardFrame(records);
        } else if (SwingDialogs.confirm(parent, "榜上无名啊，要不新开一把？")) {
            SwingWindows.dispose(parent);
            SwingUtilities.invokeLater(() -> BreakoutGameFrame.open(parent));
        }
    }

    /**
     * 创建排行榜内容面板
     *
     * @param config 窗口配置
     * @return {@link JPanel} 排行榜内容面板
     */
    @Override
    protected JPanel createContentPanel(GameWindowConfig config) {
        return new MainMenuPanel(config.getLayout());
    }

    /**
     * 构建排行榜内容
     *
     * @param panel 当前窗口的根面板
     */
    @Override
    protected void buildContent(JPanel panel) {

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setPreferredSize(new Dimension(WINDOW_WIDTH, 104));

        JLabel title = new JLabel("英雄榜", SwingConstants.CENTER);
        title.setFont(TITLE_FONT);
        title.setForeground(TITLE_COLOR);
        titlePanel.add(title, BorderLayout.CENTER);

        this.actionPanel.setOpaque(false);
        this.actionPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, 90));
        JButton backHomeButton = SwingFormFactory.with(this.actionPanel, RANK_TABLE_TEXT)
                .button(new ArcadeMenuButton("返回主界面"), 340, 20, 160, 48, ACTION_BUTTON_COLOR);
        backHomeButton.setForeground(Color.WHITE);
        backHomeButton.setFocusPainted(false);
        backHomeButton.setBorderPainted(false);
        SwingActionFactory.with(this).bind(backHomeButton, () -> SwingWindows.disposeAndOpen(this, MainMenuFrame::new));

        panel.add(titlePanel, BorderLayout.NORTH);
        this.showRecordWindow(panel, this.records);
    }

    /**
     * 展示排行榜窗口
     *
     * @param panel   当前窗口的根面板
     * @param records 排行榜记录
     */
    private void showRecordWindow(JPanel panel, List<User> records) {

        JTable recordTable = new JTable(new RecordRankTableModel(records));
        recordTable.setShowGrid(false);
        recordTable.setShowHorizontalLines(false);
        recordTable.setShowVerticalLines(false);
        recordTable.setRowHeight(40);
        recordTable.setFont(RANK_TABLE_TEXT);
        recordTable.setEnabled(false);
        recordTable.setFillsViewportHeight(true);
        recordTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        recordTable.setBackground(TABLE_BACKGROUND);
        recordTable.setForeground(new Color(33, 33, 33));

        JTableHeader tableHeader = recordTable.getTableHeader();
        tableHeader.setFont(RANK_TABLE_HEADER);
        tableHeader.setBackground(TABLE_HEADER_BACKGROUND);
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setReorderingAllowed(false);
        tableHeader.setResizingAllowed(false);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        renderer.setBackground(TABLE_BACKGROUND);
        renderer.setForeground(new Color(33, 33, 33));
        for (int columnIndex = 0; columnIndex < recordTable.getColumnCount(); columnIndex++) {
            recordTable.getColumnModel().getColumn(columnIndex).setCellRenderer(renderer);
        }

        resizeColumn(recordTable, 0, 60);
        resizeColumn(recordTable, 1, 160);
        resizeColumn(recordTable, 2, 90);
        resizeColumn(recordTable, 3, 210);

        JScrollPane scrollPane = new JScrollPane(recordTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(16, 28, 10, 28));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setBackground(TABLE_BACKGROUND);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(this.actionPanel, BorderLayout.SOUTH);
    }

    /**
     * 调整表格列宽
     *
     * @param recordTable 排行榜表格
     * @param columnIndex 列下标
     * @param width       列宽
     */
    private static void resizeColumn(JTable recordTable, int columnIndex, int width) {
        recordTable.getColumnModel().getColumn(columnIndex).setPreferredWidth(width);
    }

    // ------------------------------------------------------------------------------------------------------------------------

    /**
     * 排行榜窗口横坐标
     */
    private final static int WINDOW_X = 550;

    /**
     * 排行榜窗口纵坐标
     */
    private final static int WINDOW_Y = 130;

    /**
     * 排行榜窗口宽度
     */
    private final static int WINDOW_WIDTH = 550;

    /**
     * 排行榜窗口高度
     */
    private final static int WINDOW_HEIGHT = 650;

    /**
     * 排行榜标题字体
     */
    private final static Font TITLE_FONT = new Font("黑体", Font.BOLD, 34);

    /**
     * 排行榜标题颜色
     */
    private final static Color TITLE_COLOR = Color.WHITE;

    /**
     * 排行榜按钮颜色
     */
    private final static Color ACTION_BUTTON_COLOR = new Color(42, 107, 255);

    /**
     * 排行榜表头背景色
     */
    private final static Color TABLE_HEADER_BACKGROUND = new Color(38, 50, 56);

    /**
     * 排行榜表格背景色
     */
    private final static Color TABLE_BACKGROUND = new Color(245, 247, 250);

}
