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
     * 排行榜底部操作面板
     */
    private final JPanel actionPanel = new JPanel(null);

    /**
     * 排行榜记录
     */
    private List<User> records;

    public LeaderboardFrame() {
        this.openWindow(GameWindowConfig
                .of("打~砖~块", WINDOW_X, WINDOW_Y, WINDOW_WIDTH, WINDOW_HEIGHT)
                .setLayout(new BorderLayout()).setCloseOperation(WindowConstants.EXIT_ON_CLOSE));
    }

    /**
     * 打开排行榜前加载数据，空榜时直接引导用户开局
     */
    @Override
    protected void beforeOpen() {

        if (!CollUtil.isEmpty(this.records = MapperExecutor.query(BreakoutMapper::listRecordRanks))) {
            return;
        }

        if (SwingDialogs.confirm(null, "榜上无名啊，要不新开一把？")) {
            SwingUtilities.invokeLater(() -> BreakoutGameFrame.open(this));
        } else {
            new MainMenuFrame();
        }
    }

    /**
     * 构建排行榜内容
     *
     * @param panel 当前窗口的根面板
     */
    @Override
    protected void buildContent(JPanel panel) {

        this.actionPanel.setBackground(Color.WHITE);
        this.actionPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, 90));
        JButton backHomeButton = SwingFormFactory.with(this.actionPanel, RANK_TABLE_TEXT)
                .button("返回主界面", 350, 20, 150, 50, Color.GREEN);
        backHomeButton.setFocusPainted(false);
        backHomeButton.setBorderPainted(false);
        SwingActionFactory.with(this).bind(backHomeButton, () -> SwingWindows.disposeAndOpen(this, MainMenuFrame::new));

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
        recordTable.setRowHeight(34);
        recordTable.setFont(RANK_TABLE_TEXT);
        recordTable.setEnabled(false);
        recordTable.setFillsViewportHeight(true);
        recordTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader tableHeader = recordTable.getTableHeader();
        tableHeader.setFont(RANK_TABLE_HEADER);
        tableHeader.setReorderingAllowed(false);
        tableHeader.setResizingAllowed(false);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int columnIndex = 0; columnIndex < recordTable.getColumnCount(); columnIndex++) {
            recordTable.getColumnModel().getColumn(columnIndex).setCellRenderer(renderer);
        }

        resizeColumn(recordTable, 0, 60);
        resizeColumn(recordTable, 1, 160);
        resizeColumn(recordTable, 2, 90);
        resizeColumn(recordTable, 3, 210);

        JScrollPane scrollPane = new JScrollPane(recordTable);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.white);

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

}
