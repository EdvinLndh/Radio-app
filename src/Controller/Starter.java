package Controller;

import Model.Channel;
import Model.Program;
import Model.Model;
import View.UserInterface;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Controller class. Handles communication between view and model.
 *
 * @author Edvin Lindholm (c19elm)
 */
public class Starter {

    private UserInterface GUI;

    private final Model model;

    private Program program;

    private final AtomicBoolean parsing;

    /**
     * Initialize starter class.
     */
    public Starter() {

        model = new Model(this);
        parsing = new AtomicBoolean(false);
        startProgram();
    }

    /**
     * Starts upp RadioInfo program.
     */
    private void startProgram() {
        try {
            SwingUtilities.invokeAndWait(() -> {
                GUI = new UserInterface();
                GUI.setVisible();
            });
        } catch (InterruptedException e) {
            System.out.println("InvokeAndWait was interrupted.");
        } catch (InvocationTargetException e) {
            System.out.println("Could not invoke.");
        }
        startWorker();
        SwingUtilities.invokeLater(this::addListeners);
        model.loop();

    }

    /**
     * Add listeners for GUI components.
     */
    private void addListeners() {
        // Add selection listener to channel table.
        GUI.setChannelSelectionListener(this::buildChannelSelectionListener);

        // Add selection listener to program table.
        GUI.setProgramSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                int selectedRow = GUI.getSelectedRowInProgramTable();

                if (selectedRow == -1) {
                    return;
                }

                program = GUI.getProgramTableModel().getProgramAt(selectedRow);

                GUI.setDescriptionLabel(program.getDescription());
                GUI.setProgramPicture(program.getImage());
            }

        });

        // Add listener for button.
        GUI.addActionListener(e -> startWorker());

        // Add listeners.
        GUI.addListenerToModel();
    }

    /**
     * Build SelectionListener for channelTable.
     *
     * @param e Event.
     */
    private void buildChannelSelectionListener(
            javax.swing.event.ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            GUI.setChannelTableEnabled(false);
            int selectedRow = GUI.getSelectedRowInChannelTable();

            if (selectedRow == -1) {
                return;
            }

            Channel channel = GUI.getChannelTableModel().getChannelAt(selectedRow);

            GUI.setChannelPicture(channel.getImage());

            try {
                model.startWorker(channel, GUI.getProgramTableModel());

            } catch (IOException ignored) {
            } catch (SAXException |
                    ParserConfigurationException saxException) {
                GUI.popUp("Problem vid hämtning av program.");
            }

            GUI.getProgramTableModel().clearProgramList();
        }
    }

    /**
     * Start ChannelWorker to get channels from API.
     */
    public synchronized void startWorker() {

        if (!parsing.get()) {
            parsing.set(true);
            new ChannelWorker(GUI, model, this).execute();
        }

    }

    /**
     * Enable channelTable.
     */
    public void enableChannelTable() {
        GUI.setChannelTableEnabled(true);
    }

    /**
     * set parsing boolean to know if ChannelWorker is active.
     *
     * @param parsing True if parsing.
     */
    public void setParsing(boolean parsing) {
        this.parsing.set(parsing);
    }

}
