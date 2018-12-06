package matt.meetingplanner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "meetingplanner.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_MEETING = "CREATE TABLE Meeting" + "("
                + Meeting.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Meeting.KEY_NAME + " TEXT, "
                + Meeting.KEY_DESCRIPTION + " TEXT, "
                + Meeting.KEY_LOCATION + " TEXT, "
                + Meeting.KEY_STR_LOCATION + " TEXT,"
                + Meeting.KEY_DATE + " TEXT,"
                + Meeting.KEY_TIME + " TEXT, "
                + Meeting.KEY_ATTENDEES + " TEXT);";
        db.execSQL(CREATE_TABLE_MEETING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "Meeting;");

        onCreate(db);
    }
}
