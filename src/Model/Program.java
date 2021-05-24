package Model;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;

/**
 * Model.Program object.
 *
 * @author Edvin Lindholm (c19elm)
 */
public class Program {

    private String name;
    private Date startTime;
    private Date endTime;

    private String description;
    private Image image;

    /**
     * Gets name of program.
     *
     * @return Programs name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets starting time for program.
     *
     * @return Starting time for program in String format.
     */
    public String getStartTime() {

        return reFormatDate(startTime);
    }

    /**
     * Reformat date to show cleaner on table.
     *
     * @param time Date to reformat.
     * @return Reformatted date into  String.
     */
    private String reFormatDate(Date time) {

        LocalDateTime reformatTime = time.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();

        int day = reformatTime.getDayOfMonth();
        Month month = reformatTime.getMonth();
        int hours = reformatTime.getHour() + 1;
        int minutes = reformatTime.getMinute();

        return month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                + " " + day + "    " + String.format("%02d", hours) +
                " : " + String.format("%02d", minutes);

    }


    /**
     * Gets ending time for program.
     *
     * @return ending time for program in String format.
     */
    public String getEndTime() {
        return reFormatDate(endTime);
    }

    /**
     * Set name of program.
     *
     * @param name Programs title name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets start date for program.
     *
     * @param startTime date for program.
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * Set program description.
     *
     * @param desc Description
     */
    public void setDescription(String desc) {
        this.description = desc;
    }

    /**
     * Set program image.
     *
     * @param image Image.
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Sets end date for program.
     *
     * @param endTime date for program.
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * Get image of program logo.
     *
     * @return Programs logo image.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Get programs description.
     *
     * @return Program description.
     */
    public String getDescription() {
        return description;
    }
}
