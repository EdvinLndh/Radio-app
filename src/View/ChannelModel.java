package View;

import Model.Channel;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Tablemodel for channels.
 *
 * @author Edvin Lindholm (c19elm)
 */
public class ChannelModel extends AbstractTableModel {

    private final ArrayList<Channel> channelList;

    /**
     * Initialize ArrayList of channels.
     */
    public ChannelModel() {
        channelList = new ArrayList<>();
    }

    /**
     * Gets amount of rows in table.
     *
     * @return Amount of rows.
     */
    @Override
    public int getRowCount() {
        return channelList.size();
    }

    /**
     * Gets amount of columns in table.
     *
     * @return Amount of columns.
     */
    @Override
    public int getColumnCount() {
        return 1;
    }

    /**
     * Gets specific value at index.
     *
     * @param rowIndex    Index of the row.
     * @param columnIndex Index of the column.
     * @return Value at the index.
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return channelList.get(rowIndex).getName();
    }

    /**
     * Gets channel at index.
     *
     * @param rowIndex Index of channel
     * @return Model.Channel at index.
     */
    public Channel getChannelAt(int rowIndex) {
        return channelList.get(rowIndex);
    }

    /**
     * Adds channel to table.
     *
     * @param c Model.Channel to add.
     */
    public void addChannel(Channel c) {
        channelList.add(c);
        this.fireTableRowsInserted(channelList.size(), channelList.size());
    }

    /**
     * Gets name of column.
     *
     * @param columnIndex Index of column.
     * @return Name of column.
     */
    @Override
    public String getColumnName(int columnIndex) {
        return "Namn";
    }


    /**
     * Clear channel list and notifies that rows have been deleted.
     */
    public void clearChannelList(){
        int length = channelList.size();
        for(Channel c : channelList) {
            c.getPrograms().clear();
        }
        channelList.clear();
        this.fireTableRowsDeleted(0, length);
    }

}
