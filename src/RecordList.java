import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Vector;

import static javax.swing.SwingUtilities.invokeLater;

public class RecordList extends JFrame {
    DataBase data = new DataBase();
    public JPanel jPanel = new JPanel();
    public JButton backToHome = new JButton("返回主界面");
    static String name = "";
    static int score = 0;
    static String time = "";
    Vector rowData, columnName;
    JTable recordTable;
    JScrollPane jsp;
    Font font = new Font("宋体", Font.BOLD, 12);

    public RecordList(String userId, String userName) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        jPanel.setBackground(Color.white);
        columnName = new Vector();
        columnName.add("排名");
        columnName.add("姓名");
        columnName.add("成绩");
        columnName.add("完成时间");
        backToHome.setBounds(350, 530, 150, 50);
        backToHome.setFont(font);
        backToHome.setBackground(Color.GREEN);
        backToHome.setFocusPainted(false);
        backToHome.setBorderPainted(false);
        try {
            if (userId.equals("")) {
                int n = JOptionPane.showConfirmDialog(null, "请先登录！", "提示", JOptionPane.YES_NO_OPTION);
                if (n == 0) {
                    setVisible(false);
                    new Login();
                } else {
                    setVisible(false);
                    new MainGame();
                }
            } else {
                String sql = "SELECT u.user_name,r.grade,r.create_time " +
                        "FROM record r " +
                        "LEFT JOIN USER u on r.user_id = u.user_id " +
                        "ORDER BY r.grade desc, r.create_time";
                ResultSet rs = DataBase.stmt.executeQuery(sql);
                rs.last();
                int i = 1;
                if (rs.getRow() != 0) {
                    rs.beforeFirst();
                    rowData = new Vector();
                    while (rs.next()) {
                        int rank = i++;
                        name = rs.getString("user_name");
                        score = Integer.parseInt(rs.getString("grade"));
                        time = sdf.format(rs.getTimestamp("create_time"));
                        Vector hang = new Vector();
                        hang.add(rank);
                        hang.add(name);
                        hang.add(score);
                        hang.add(time);
                        System.out.println(hang);
                        rowData.add(hang);
                    }
                    recordTable = new JTable(rowData, columnName);
                    recordTable.setShowGrid(false);
                    recordTable.setShowHorizontalLines(false);
                    recordTable.setShowVerticalLines(false);
                    recordTable.setRowHeight(30);
                    recordTable.setFont(font);
                    recordTable.setEnabled(false);

                    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
                    renderer.setHorizontalAlignment(SwingConstants.CENTER);//文字居中
                    recordTable.getColumn("排名").setCellRenderer(renderer);
                    recordTable.getColumn("姓名").setCellRenderer(renderer);
                    recordTable.getColumn("成绩").setCellRenderer(renderer);
                    recordTable.getColumn("完成时间").setCellRenderer(renderer);

                    TableColumn firstColumn = recordTable.getColumnModel().getColumn(0);
                    firstColumn.setPreferredWidth(10);
                    TableColumn secondColumn = recordTable.getColumnModel().getColumn(1);
                    secondColumn.setPreferredWidth(15);
                    TableColumn thirdColumn = recordTable.getColumnModel().getColumn(2);
                    thirdColumn.setPreferredWidth(10);
                    TableColumn fourthColumn = recordTable.getColumnModel().getColumn(3);
                    fourthColumn.setPreferredWidth(50);

                    jsp = new JScrollPane(recordTable);
                    jPanel.add(jsp);

                    jPanel.add(backToHome);
                    backToHome.addActionListener(new ButtonListeners(this));
                    this.add(jPanel);
                    this.setBounds(550, 130, 550, 650);
                    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    this.setVisible(true);
                    this.setResizable(false);
                    this.setTitle("打~砖~块");
                } else {
                    int n = JOptionPane.showConfirmDialog(null, "榜上无名啊，要不新开一把？", "提示", JOptionPane.YES_NO_OPTION);
                    if (n == 0) {
                        invokeLater(() -> {
                            JBreakout j = null;
                            j = new JBreakout(userId, userName);
                            j.setBackground(Color.white);
                            j.setSize(550, 700);
                            j.setLocation(550, 80);
                            j.setVisible(true);
                            j.setBreakoutComponents();
                        });
                    } else {
                        new MainGame();
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "连接数据库时好像遇到了点错误？", "提示", JOptionPane.NO_OPTION);
        }
//        recordTable = new JTable(rowData, columnName);
//        recordTable.setShowGrid(false);
//        recordTable.setShowHorizontalLines(false);
//        recordTable.setShowVerticalLines(false);
//        recordTable.setRowHeight(30);
//        recordTable.setFont(font);
//        recordTable.setEnabled(false);
//
//        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
//        renderer.setHorizontalAlignment(SwingConstants.CENTER);//文字居中
//        recordTable.getColumn("排名").setCellRenderer(renderer);
//        recordTable.getColumn("姓名").setCellRenderer(renderer);
//        recordTable.getColumn("成绩").setCellRenderer(renderer);
//        recordTable.getColumn("完成时间").setCellRenderer(renderer);
//
//        TableColumn firstColumn = recordTable.getColumnModel().getColumn(0);
//        firstColumn.setPreferredWidth(10);
//        TableColumn secondColumn = recordTable.getColumnModel().getColumn(1);
//        secondColumn.setPreferredWidth(15);
//        TableColumn thirdColumn = recordTable.getColumnModel().getColumn(2);
//        thirdColumn.setPreferredWidth(10);
//        TableColumn fourthColumn = recordTable.getColumnModel().getColumn(3);
//        fourthColumn.setPreferredWidth(50);
//
//        jsp = new JScrollPane(recordTable);
//        jPanel.add(jsp);
//
//        jPanel.add(backToHome);
//        backToHome.addActionListener(new ButtonListeners(this));
//        this.add(jPanel);
//        this.setBounds(550, 130, 550, 650);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setVisible(true);
//        this.setResizable(false);
//        this.setTitle("打~砖~块");
    }

    private class ButtonListeners implements ActionListener {
        public RecordList recordList;

        public ButtonListeners(RecordList recordList) {
            this.recordList = recordList;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            recordList.setVisible(false);
            dispose();
            Setting.ballLife.setEnabled(true);
            Setting.brickCount.setEnabled(true);
            Setting.rate.setEnabled(true);
            Setting.lifeWarning.setVisible(true);
            Setting.successCountLabelWarning.setVisible(true);
            Setting.rateWarning.setVisible(true);
            new MainGame();

        }
    }
}
