import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ObjectUtil;
import db.BreakoutMapper;
import db.MapperExecutor;
import dto.RecordRankTableModel;
import dto.User;
import ui.SwingActionFactory;
import ui.SwingFormFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class RecordList extends JFrame {

    private static final long serialVersionUID = 4063485659525045767L;

    private static final int WINDOW_X = 550;

    private static final int WINDOW_Y = 130;

    private static final int WINDOW_WIDTH = 550;

    private static final int WINDOW_HEIGHT = 650;

    private final JPanel recordPanel = new JPanel(new BorderLayout());

    private final JPanel actionPanel = new JPanel(null);

    private final static Font DEFAULT_FONT = new Font("宋体", Font.BOLD, 12);

    public RecordList() {
        this.recordPanel.setBackground(Color.WHITE);
        this.actionPanel.setBackground(Color.WHITE);
        this.actionPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, 90));
        JButton backHomeButton = SwingFormFactory.with(this.actionPanel, DEFAULT_FONT)
                .button("返回主界面", 350, 20, 150, 50, Color.GREEN);
        backHomeButton.setFocusPainted(false);
        backHomeButton.setBorderPainted(false);
        SwingActionFactory.with(this).bind(backHomeButton, this::backHome);

        if (ObjectUtil.isNull(GameSupporter.loadCurrentLoggedInUserId())) {
            this.handleAnonymousUser();
        } else {
            Opt.ofEmptyAble(MapperExecutor.query(BreakoutMapper::listRecordRanks))
                    .ifPresentOrElse(this::showRecordWindow, this::handleEmptyRecord);
        }

    }

    /**
     * 展示排行榜窗口
     *
     * @param records 排行榜记录
     */
    private void showRecordWindow(List<User> records) {

        JTable recordTable = new JTable(new RecordRankTableModel(records));
        recordTable.setShowGrid(false);
        recordTable.setShowHorizontalLines(false);
        recordTable.setShowVerticalLines(false);
        recordTable.setRowHeight(34);
        recordTable.setFont(DEFAULT_FONT);
        recordTable.setEnabled(false);
        recordTable.setFillsViewportHeight(true);
        recordTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader tableHeader = recordTable.getTableHeader();
        tableHeader.setFont(new Font("宋体", Font.BOLD, 14));
        tableHeader.setReorderingAllowed(false);
        tableHeader.setResizingAllowed(false);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int columnIndex = 0; columnIndex < recordTable.getColumnCount(); columnIndex++) {
            recordTable.getColumnModel().getColumn(columnIndex).setCellRenderer(renderer);
        }

        this.resizeColumn(recordTable, 0, 60);
        this.resizeColumn(recordTable, 1, 160);
        this.resizeColumn(recordTable, 2, 90);
        this.resizeColumn(recordTable, 3, 210);

        JScrollPane scrollPane = new JScrollPane(recordTable);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.white);

        this.recordPanel.add(scrollPane, BorderLayout.CENTER);
        this.recordPanel.add(this.actionPanel, BorderLayout.SOUTH);

        this.add(this.recordPanel);
        this.setBounds(WINDOW_X, WINDOW_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        this.setTitle("打~砖~块");
    }

    /**
     * 调整表格列宽
     *
     * @param recordTable 排行榜表格
     * @param columnIndex 列下标
     * @param width       列宽
     */
    private void resizeColumn(JTable recordTable, int columnIndex, int width) {
        recordTable.getColumnModel().getColumn(columnIndex).setPreferredWidth(width);
    }

    /**
     * 处理未登录用户
     */
    private void handleAnonymousUser() {
        int result = JOptionPane.showConfirmDialog(null, "请先登录！", "提示", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            this.setVisible(false);
            new Login();
            return;
        }

        this.setVisible(false);
        new MainGame();
    }

    /**
     * 处理空排行榜
     */
    private void handleEmptyRecord() {
        int result = JOptionPane.showConfirmDialog(null, "榜上无名啊，要不新开一把？", "提示", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(() -> JBreakout.open(this));
        } else {

            new MainGame();
        }

    }

    /**
     * 返回主界面
     */
    private void backHome() {
        this.setVisible(false);
        this.dispose();
        new MainGame();
    }

}
