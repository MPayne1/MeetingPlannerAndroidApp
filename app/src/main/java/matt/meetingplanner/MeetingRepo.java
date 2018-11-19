package matt.meetingplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class MeetingRepo {
    private DBHelper dbHelper;


    public MeetingRepo (Context context) {
        dbHelper = new DBHelper(context);
    }

    public void insert(Meeting meeting) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Meeting.KEY_NAME, meeting.name);
        values.put(Meeting.KEY_DESCRIPTION, meeting.description);
        values.put(Meeting.KEY_LOCATION, meeting.location);
        values.put(Meeting.KEY_DATE, meeting.date);
        values.put(Meeting.KEY_TIME, meeting.time);

        db.insert(Meeting.TABLE, null, values);
        db.close();
    }
}
