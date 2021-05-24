package Model;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Parse information from API.
 *
 * @author Edvin Lindholm (c19elm)
 */
public class APIParser {

    private ArrayList<Channel> channelList;
    private final String channelURL;

    public APIParser() {
        channelList = new ArrayList<>();
        channelURL = "http://api.sr.se/api/v2/channels/?pagination=false";
    }

    /**
     * Build a list of channels from API.
     *
     * @return List of channels.
     */
    public ArrayList<Channel> buildChannelList()
            throws IOException, SAXException, ParserConfigurationException {

        channelList = new ArrayList<>();

        Document doc = buildDocument(channelURL);

        NodeList channelNodeList = doc.getElementsByTagName("channels");

        Node channels = channelNodeList.item(0);

        addChannels(channels);

        return channelList;
    }

    /**
     * Loop to add channels
     *
     * @param channels channels node.
     */
    private void addChannels(Node channels) {

        for (int i = 0; i < channels.getChildNodes().getLength(); i++) {

            Node currentNode = channels.getChildNodes().item(i);

            if (currentNode.getChildNodes().getLength() == 0) {
                continue;
            }

            int id = Integer.parseInt(currentNode.getAttributes()
                    .getNamedItem("id").getNodeValue());

            String name = currentNode.getAttributes()
                    .getNamedItem("name").getNodeValue();

            Channel ch = new Channel(id, name);

            Node imageNode = currentNode.getFirstChild().getNextSibling();
            String imageURL = imageNode.getFirstChild().getNodeValue();

            Image image = parseImage(imageURL);

            ch.setImage(image);

            channelList.add(ch);
        }
    }

    /**
     * Make a list of programs for the channel parameter.
     *
     * @param channel Channel to add programs to.
     * @return List of programs.
     * @throws ParserConfigurationException Error while parsing.
     * @throws SAXException                 Error while parsing.
     * @throws IOException                  I/O error.
     */
    public ArrayList<Program> parsePrograms(Channel channel)
            throws IOException, SAXException, ParserConfigurationException {

        ArrayList<Program> programs = new ArrayList<>();

        int totalPages;

        String URLStart = "http://api.sr.se/api/v2/" +
                "scheduledepisodes?channelid=";

        for (int day = -1; day < 2; day++) {

            String todayURL = URLStart + channel.getChannelId()
                    + "&date=" + getDateString(day);

            Document doc = buildDocument(todayURL);

            NodeList totalpages = doc.getElementsByTagName("totalpages");

            totalPages = Integer.parseInt(totalpages.item(0)
                    .getChildNodes().item(0).getNodeValue());


            for (int pageNum = 1; pageNum < totalPages; pageNum++) {

                String currPageURL = URLStart + channel.getChannelId()
                        + "&date=" + getDateString(day) + "&page=" + pageNum;

                Document pageDoc = buildDocument(currPageURL);

                NodeList schedule = pageDoc
                        .getElementsByTagName("scheduledepisode");

                for (int i = 1; i < schedule.getLength(); i++) {

                    Node currentProgramNode = schedule.item(i);

                    Program currentProgram;

                    currentProgram = parseProgram(currentProgramNode);
                    if (currentProgram == null) {
                        continue;
                    }

                    programs.add(currentProgram);
                }

            }
        }
        return programs;
    }

    /**
     * Parses information to create a program given a node.
     *
     * @param currentProgramNode Node containing information to build
     *                           program from.
     * @return Model.Program object.
     */
    private Program parseProgram(Node currentProgramNode) {

        Program currentProgram = new Program();

        int childNum = currentProgramNode.getChildNodes().getLength();

        for (int i = 1; i < childNum - 1; i++) {

            if (parseProgramInformation(currentProgramNode, currentProgram, i))
                return null;
        }
        return currentProgram;
    }

    /**
     * Parse information for program.
     *
     * @param currentProgramNode CurrentNode.
     * @param currentProgram Program to create.
     * @param i current loop index.
     * @return If program was parsed correctly.
     */
    private boolean parseProgramInformation(Node currentProgramNode,
                                            Program currentProgram, int i) {
        Node value = currentProgramNode.getChildNodes().item(i);

        switch (value.getNodeName()) {
            case "title":
                currentProgram.setName(value.getFirstChild().getNodeValue());

                break;

            case "starttimeutc":
                if (startTimeCase(currentProgram, value)) return true;

                break;

            case "endtimeutc":
                endTimeCase(currentProgram, value);

                break;

            case "description":
                if (value.getFirstChild() == null) {
                    break;
                }
                String desc = value.getFirstChild().getNodeValue();
                currentProgram.setDescription(desc);

                break;

            case "imageurl":
                String imageURL = value.getFirstChild().getNodeValue();
                currentProgram.setImage(parseImage(imageURL));

                break;

            default:
                break;
        }
        return false;
    }

    private void endTimeCase(Program currentProgram, Node value) {
        String endTime = value.getFirstChild().getNodeValue();
        Date endDate = null;
        try {
            endDate = new SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss'Z'").parse(endTime);

        } catch (ParseException ignored) {
        }
        currentProgram.setEndTime(endDate);
    }

    private boolean startTimeCase(Program currentProgram, Node value) {
        String startTime = value.getFirstChild().getNodeValue();
        Date startDate = null;
        try {
            startDate = new SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss'Z'").parse(startTime);
            long currTime = new Date().getTime();
            int halfADay = 1000 * 60 * 60 * 12;
            if (Math.abs(currTime - startDate.getTime()) > halfADay) {
                return true;
            }
        } catch (ParseException ignored) {
        }
        currentProgram.setStartTime(startDate);
        return false;
    }

    /**
     * Creates string from given day.
     *
     * @param day Day to create string from ???
     * @return String formatted to date.
     */
    private String getDateString(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, day);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date relativeDay = calendar.getTime();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return format.format(relativeDay);
    }

    /**
     * Reads image from given URL parameter.
     *
     * @param imageURL URL to the image to read.
     * @return Image of the channel logo.
     */
    private Image parseImage(String imageURL) {
        Image image = null;

        try {
            URL url = new URL(imageURL);
            image = ImageIO.read(url);
        } catch (IOException ignored) {
        }
        return image;
    }

    /**
     * Builds document using the given url parameter.
     *
     * @param url URL to build document from.
     * @return Built document.
     * @throws ParserConfigurationException Error while parsing.
     * @throws SAXException                 Error while parsing.
     * @throws IOException                  I/O error.
     */
    private Document buildDocument(String url)
            throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        URLConnection connection = new URL(url).openConnection();

        connection.addRequestProperty("Accept", "application/xml");

        Document doc = builder.parse(connection.getInputStream());

        doc.getDocumentElement().normalize();

        return doc;
    }

}
