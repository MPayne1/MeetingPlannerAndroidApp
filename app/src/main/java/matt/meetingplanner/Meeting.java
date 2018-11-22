package matt.meetingplanner;

public class Meeting {

    public static final String TABLE = "Meeting";
    // Column names
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_TIME = "time";
    public static final String KEY_DATE = "date";
    public static final String KEY_ID = "id";
    public static final String KEY_ATTENDEES = "attendees";

    // Meeting properties
    public int meetingID;
    public String name;
    public String description;
    public String location;
    public String time;
    public String date;
    public String attendees;
}
