package Model;

import java.awt.*;
import java.util.ArrayList;

/**
 * Model.Channel object.
 *
 * @author Edvin Lindholm (c19elm)
 */
public class Channel {

    private final int channelId;

    private final String name;

    private final ArrayList<Program> programs;

    private Image image;

    /**
     * Create channel.
     *
     * @param channelId Channels id.
     * @param name      Channels name.
     */
    public Channel(int channelId, String name) {
        this.channelId = channelId;
        this.name = name;
        programs = new ArrayList<>();
    }

    /**
     * Get name of channel.
     *
     * @return Model.Channel name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get image of channel logo.
     *
     * @return Model.Channel image logo.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Set channel image logo.
     *
     * @param image Image of channel logo.
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Get list of programs.
     *
     * @return List of programs.
     */
    public ArrayList<Program> getPrograms() {
        return programs;
    }

    /**
     * Gets id for channel.
     *
     * @return channelId
     */
    public int getChannelId() {
        return channelId;
    }


}
