package ubicomp.ucd.personalassistant;

/**
 * Created by specter on 11/24/15.
 */
public class EventResultDataModel {

    private String eventLocation;
    private String eventTitle;
    private boolean hasEventNow;
    private String eventContent;

    public String getEventContent() {
        return eventContent;
    }

    public void setEventContent(String eventContent) {
        this.eventContent = eventContent;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public boolean isHasEventNow() {
        return hasEventNow;
    }

    public void setHasEventNow(boolean hasEventNow) {
        this.hasEventNow = hasEventNow;
    }

}
