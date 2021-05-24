package Controller;

import Model.Channel;
import Model.Model;
import View.ChannelModel;
import View.UserInterface;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Use parser class to load in data and publish to table. Works from controller.
 *
 * @author Edvin Lindholm (c19elm)
 */
public class ChannelWorker extends SwingWorker<String, Channel> {

    private final Model model;
    private ArrayList<Channel> channelList;
    private final ChannelModel tableModel;
    private final UserInterface UI;
    private final Starter starter;

    /**
     * Initialize ChannelWorker to fetch channels from API.
     *
     * @param UI    Program GUI.
     * @param model Program model.
     */
    public ChannelWorker(UserInterface UI, Model model, Starter starter) {

        this.starter = starter;
        this.UI = UI;
        this.model = model;
        this.tableModel = UI.getChannelTableModel();
        tableModel.clearChannelList();
        channelList = new ArrayList<>();
        SwingUtilities.invokeLater( () -> {

            UI.setUpdateButtonStatus(false);
            UI.setDescriptionLabel("");
            UI.setChannelPicture(null);
            UI.setProgramPicture(null);
        });

    }

    /**
     * Fetch list of channels.
     *
     * @return Null
     */
    @Override
    protected String doInBackground() {

        try {
            channelList = model.getChannelList();
        } catch (ParserConfigurationException | SAXException e) {
            return "Problem vid hämtning av kanaler.";
        } catch (IOException ignored) {
        }
        if(channelList.size() == 0) {
            return "Inga kanaler hittades, kontrollera din nätuppkoppling " +
                    "och försök igen.";
        }
        for (Channel c : channelList) {
            publish(c);
        }
        return null;
    }

    /**
     * Add channels to channel table model.
     *
     * @param channels List of channels.
     */
    @Override
    protected void process(List<Channel> channels) {
        for (Channel ch : channels) {
            tableModel.addChannel(ch);
        }
    }

    /**
     * Enable update button and set parsing boolean to false.
     */
    @Override
    protected void done() {
        try {
            if(get() != null) {
                UI.popUp(get());

                publish();
                UI.setUpdateButtonStatus(true);
                starter.setParsing(false);
                starter.enableChannelTable();
            }
        } catch (InterruptedException | ExecutionException e) {
            UI.popUp("Problem vid hämntning av kanaler.");
        }

        publish();
        UI.setUpdateButtonStatus(true);
        starter.setParsing(false);
        starter.enableChannelTable();
    }

}
