package View;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * User interface class. Shows table of channels and programs.
 *
 * @author Edvin Lindholm (c19elm)
 */
public class UserInterface {

    private final JFrame frame;
    private JMenuBar menuBar;

    private JTable channelTable;
    private JTable programTable;

    private JScrollPane programScroller;
    private JScrollPane channelScroller;

    private ChannelModel channelTableModel;
    private ProgramModel programTableModel;

    private ListSelectionListener channelSelectionListener;
    private ListSelectionListener programSelectionListener;

    private final JLabel channelPicture;
    private final JLabel programPicture;
    private final JLabel description;
    private final JButton update;

    /**
     * Constructs window with menu, table and buttons.
     */
    public UserInterface() {
        frame = new JFrame("RadioInfo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1450, 850));
        frame.setMinimumSize(new Dimension(1000, 850));

        buildMenuBar();
        buildTable();

        channelPicture = new JLabel();
        channelPicture.setPreferredSize(new Dimension(300, 300));

        programPicture = new JLabel();
        programPicture.setPreferredSize(new Dimension(300, 300));

        JPanel panel = new JPanel();
        description = new JLabel();

        panel.add(channelPicture, BorderLayout.WEST);
        panel.add(programPicture, BorderLayout.EAST);

        JPanel tablePanel = new JPanel();

        tablePanel.add(channelScroller);
        tablePanel.add(programScroller);

        JPanel miscPanel = new JPanel();

        frame.add(panel, BorderLayout.NORTH);
        frame.add(tablePanel, BorderLayout.CENTER);

        update = new JButton("Uppdatera kanaler");

        miscPanel.add(update, BorderLayout.SOUTH);
        miscPanel.add(description, BorderLayout.NORTH);

        frame.add(miscPanel, BorderLayout.SOUTH);
        frame.setJMenuBar(menuBar);
        frame.pack();
    }

    /**
     * Builds menubar for window.
     */
    private void buildMenuBar() {

        menuBar = new JMenuBar();

        JMenu info = new JMenu("Info");

        JMenuItem about = new JMenuItem("Om");
        JMenuItem help = new JMenuItem("Hjälp");

        about.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "Gjort av Edvin Lindholm (c19elm)"));
        help.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "Hämta kanal tablåer med knappen " +
                        "\"Uppdatera kanaler\" \n" +
                        "Klicka på ett program i " +
                        "tabellen för att få mer information om programmet."));

        info.add(about);
        info.add(help);
        menuBar.add(info);
    }

    /**
     * Builds table and initializes table model.
     */
    private void buildTable() {
        channelTableModel = new ChannelModel();
        channelTable = new JTable(channelTableModel);

        programTableModel = new ProgramModel();
        programTable = new JTable(programTableModel);

        channelScroller = new JScrollPane(channelTable);
        programScroller = new JScrollPane(programTable);
    }

    /**
     * Set ListSelectionListener to channelTable.
     *
     * @param selectionListener Listener to add.
     */
    public void setChannelSelectionListener(
            ListSelectionListener selectionListener) {
        channelTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        channelSelectionListener = selectionListener;
    }

    /**
     * set selectionListener to program tables selection listener.
     *
     * @param selectionListener Listener to set.
     */
    public void setProgramSelectionListener(
            ListSelectionListener selectionListener) {

        programSelectionListener = selectionListener;
    }

    /**
     * Add ActionListener for button.
     *
     * @param listener listener to add.
     */
    public void addActionListener(ActionListener listener) {
        update.addActionListener(listener);
    }

    /**
     * Add SelectionListeners to table.
     */
    public void addListenerToModel() {
        channelTable.getSelectionModel()
                .addListSelectionListener(channelSelectionListener);
        programTable.getSelectionModel()
                .addListSelectionListener(programSelectionListener);
    }

    /**
     * Get selected row in channelTable.
     *
     * @return selected row in ChannelTable.
     */
    public int getSelectedRowInChannelTable() {
        return channelTable.getSelectedRow();
    }

    /**
     * Get selected row in programTable.
     *
     * @return Selected row in programTable.
     */
    public int getSelectedRowInProgramTable() {
        return programTable.getSelectedRow();
    }


    /**
     * Getter for table model.
     *
     * @return Table model.
     */
    public ChannelModel getChannelTableModel() {
        return channelTableModel;
    }

    /**
     * Get model of program table.
     *
     * @return Model of program table.
     */
    public ProgramModel getProgramTableModel() {
        return programTableModel;
    }


    /**
     * Sets and shows picture of channel logo.
     *
     * @param image Logo image.
     */
    public void setChannelPicture(Image image) {
        setPicture(image, channelPicture);
    }

    /**
     * Set and show picture of program logo.
     *
     * @param image Logo image.
     */
    public void setProgramPicture(Image image) {
        setPicture(image, programPicture);

    }

    /**
     * Set label icon to image.
     *
     * @param image Image to show on label.
     * @param label Label to add image to.
     */
    private void setPicture(Image image, JLabel label) {
        label.setBackground(Color.GRAY);
        if (image == null) {
            label.setIcon(null);
            label.setText("Ingen bild laddad.");

            return;
        }
        label.setText("");
        Image newImage = image.getScaledInstance(
                300, 300, Image.SCALE_SMOOTH);


        BufferedImage BImage = new BufferedImage(
                newImage.getWidth(null),
                newImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics g = BImage.getGraphics();
        g.drawImage(newImage, 0, 0, null);
        g.dispose();
        label.setIcon(new ImageIcon(BImage));

        label.repaint();
        label.revalidate();
    }


    /**
     * Makes the JFrame visible.
     */
    public void setVisible() {
        frame.setVisible(true);
    }

    /**
     * Set enable status, and text on button.
     *
     * @param bool Status of button.
     */
    public void setUpdateButtonStatus(boolean bool) {
        update.setEnabled(bool);
        if (!bool) {
            update.setText("Laddar kanaler");
        } else {
            update.setText("Uppdatera kanaler");
        }
    }

    /**
     * Sets the text on description label.
     *
     * @param desc Program description to add to label.
     */
    public void setDescriptionLabel(String desc) {
        description.setBackground(Color.WHITE);
        description.setText(desc);
    }

    /**
     * Show popup to user. Mostly for showing errors.
     *
     * @param message Message to user.
     */
    public void popUp(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Enable/disable channelTable.
     * @param value True to enable, false to disable.
     */
    public void setChannelTableEnabled(boolean value) {
        channelTable.setEnabled(value);
    }

}
