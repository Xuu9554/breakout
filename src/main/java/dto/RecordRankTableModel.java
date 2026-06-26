package dto;

import cn.hutool.core.date.DateUtil;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class RecordRankTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 5925367813614833094L;

    private static final String[] COLUMN_NAMES = {"排名", "账号", "成绩", "完成时间"};

    private final List<User> records;

    public RecordRankTableModel(List<User> records) {
        this.records = records;
    }

    /**
     * 获取行数
     *
     * @return int 行数
     */
    @Override
    public int getRowCount() {
        return this.records.size();
    }

    /**
     * 获取列数
     *
     * @return 列数
     */
    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    /**
     * 获取列名
     *
     * @param column 列下标
     * @return 列名
     */
    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    /**
     * 获取单元格值
     *
     * @param rowIndex    行下标
     * @param columnIndex 列下标
     * @return {@link Object} 单元格值
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        User user = this.records.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rowIndex + 1;
            case 1:
                return user.getUserId();
            case 2:
                return user.getHighScore();
            case 3:
                return DateUtil.formatDateTime(user.getHighScoreAchievedTime());
            default:
                return "";
        }
    }
}
