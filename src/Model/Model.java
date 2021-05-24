package Model;


import Controller.Starter;
import View.ProgramModel;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Business logic of program.
 *
 * @author Edvin Lindholm (c19elm)
 */
public class Model {

    private final APIParser parser;
    private final Starter starter;

    /**
     * Initialize model class.
     *
     * @param starter Controller class.
     */
    public Model(Starter starter) {
        parser = new APIParser();
        this.starter = starter;
    }

    /**
     * Loop that determines when to update channels.
     */
    public void loop() {

        Timer timer = new Timer();
        int hour = 1000 * 60 * 60;

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                starter.startWorker();

            }
        }, hour, hour);
    }

    /**
     * Parses and returns list of channels.
     *
     * @return List of parsed channels.
     * @throws ParserConfigurationException Error while parsing.
     * @throws SAXException                 Error while parsing.
     * @throws IOException                  Error reading picture.
     */
    public ArrayList<Channel> getChannelList()
            throws ParserConfigurationException, SAXException, IOException {

        return parser.buildChannelList();
    }

    /**
     * Start programWorker.
     *
     * @param channel Channel program runs on.
     * @throws ParserConfigurationException Error while parsing.
     * @throws SAXException                 Error while parsing.
     * @throws IOException                  Error reading picture.
     */
    public void startWorker(Channel channel, ProgramModel pModel)
            throws IOException, SAXException, ParserConfigurationException {

        new ProgramWorker(channel, this, pModel).execute();
    }

    /**
     * Parse programs.
     *
     * @param channel Channel program runs on.
     * @return List of parsed programs.
     * @throws ParserConfigurationException Error while parsing.
     * @throws SAXException                 Error while parsing.
     * @throws IOException                  Error reading picture.
     */
    protected ArrayList<Program> parsePrograms(Channel channel)
            throws ParserConfigurationException, SAXException, IOException {
        return parser.parsePrograms(channel);
    }

    /**
     * Method for worker to call when worker is done.
     */
    public void workerIsDone() {
        starter.enableChannelTable();
    }

}
