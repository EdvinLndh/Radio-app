package View;

import Model.Program;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Define table layout for program table.
 *
 * @author Edvin Lindholm (c19elm)
 */
public class ProgramModel extends AbstractTableModel {

    private final ArrayList<Program> programs;

    public ProgramModel() {
        programs = new ArrayList<>();
    }

    /**
     * Get amount of rows in table.
     *
     * @return Row count.
     */
    @Override
    public int getRowCount() {
        return programs.size();
    }

    /**
     * Gets amount of columns in table.
     *
     * @return Column count.
     */
    @Override
    public int getColumnCount() {
        return 3;
    }

    /**
     * Get value at specified index.
     *
     * @param rowIndex    Index of the row.
     * @param columnIndex Index of the column.
     * @return Value at index.
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Program program = programs.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> program.getName();
            case 1 -> program.getStartTime();
            case 2 -> program.getEndTime();
            default -> null;
        };
    }

    /**
     * Get program at index given row index.
     *
     * @param rowIndex Index of row.
     * @return Program at index.
     */
    public Program getProgramAt(int rowIndex) {
        return programs.get(rowIndex);
    }

    /**
     * Get name of columns.
     *
     * @param column Column index.
     * @return Column name.
     */
    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> "Namn";
            case 1 -> "Start tid";
            case 2 -> "Slut tid";
            default -> null;
        };
    }

    /**
     * Add program to list.
     *
     * @param p Program to add.
     */
    public void addProgram(Program p) {
        programs.add(p);
        this.fireTableRowsInserted(programs.size(), programs.size());
    }

    /**
     * Clear list of programs.
     */
    public void clearProgramList() {
        int length = programs.size();
        programs.clear();
        this.fireTableRowsDeleted(0, length);
    }
}
