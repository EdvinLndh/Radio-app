package Model;

import View.ProgramModel;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Worker getting programs for channel.
 *
 * @author Edvin Lindholm (c19elm)
 */
public class ProgramWorker extends SwingWorker<Void, Program> {

    private ArrayList<Program> programList;
    private final Channel channel;
    private final Model model;
    private final ProgramModel pModel;

    /**
     * Initializes worker to parse programs.
     * @param channel Channel program runs on.
     * @param model Model class.
     * @param pModel Model for program table.
     */
    public ProgramWorker(Channel channel, Model model, ProgramModel pModel) {

        programList = new ArrayList<>();
        this.pModel = pModel;
        this.channel = channel;
        this.model = model;

    }

    /**
     * Get programs from channel and publish.
     *
     * @return Null.
     */
    @Override
    protected Void doInBackground()
            throws IOException, SAXException, ParserConfigurationException {

        programList = model.parsePrograms(channel);

        for (Program p : programList) {
            publish(p);
        }
        return null;
    }

    /**
     * Add programs to channel.
     *
     * @param programs List of programs.
     */
    @Override
    protected void process(List<Program> programs) {
        for (Program p : programs) {
            pModel.addProgram(p);
        }
    }

    /**
     * Tell model that we're done.
     */
    @Override
    protected void done() {
        model.workerIsDone();
    }
}
