package matt.meetingplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class MeetingRepo {
    private DBHelper dbHelper;


    public MeetingRepo (Context context) {
        dbHelper = new DBHelper(context);
    }

    // insert new meeting
    public void insert(Meeting meeting) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Meeting.KEY_NAME, meeting.name);
        values.put(Meeting.KEY_DESCRIPTION, meeting.description);
        values.put(Meeting.KEY_LOCATION, meeting.location);
        values.put(Meeting.KEY_DATE_TIME, meeting.dateTime);

        db.insert(Meeting.TABLE, null, values);
        db.close();
    }


    // get list of all meetings
    // TODO change so only get past meetings
    // TODO make new method to get future meetings
    public ArrayList<Meeting> getMeetingList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Meeting.TABLE +";";

        ArrayList<Meeting> meetingList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if(cursor.moveToFirst()) {
            do {
                Meeting m = new Meeting();
                m.name = cursor.getString(cursor.getColumnIndex(Meeting.KEY_NAME));
                m.description = cursor.getString(cursor.getColumnIndex(Meeting.KEY_DESCRIPTION));
                m.dateTime = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Meeting.KEY_DATE_TIME)));
                m.location = cursor.getString(cursor.getColumnIndex(Meeting.KEY_LOCATION));
                m.meetingID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Meeting.KEY_ID)));
                meetingList.add(m);
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return meetingList;
    }

    public ArrayList<Meeting> getPastMeetingList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Meeting.TABLE +  " WHERE "  +  Meeting.KEY_DATE_TIME
                + " < " + System.currentTimeMillis() + ";";

        ArrayList<Meeting> meetingList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                Meeting m = new Meeting();
                m.name = cursor.getString(cursor.getColumnIndex(Meeting.KEY_NAME));
                m.description = cursor.getString(cursor.getColumnIndex(Meeting.KEY_DESCRIPTION));
                m.dateTime = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Meeting.KEY_DATE_TIME)));
                m.location = cursor.getString(cursor.getColumnIndex(Meeting.KEY_LOCATION));
                m.meetingID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Meeting.KEY_ID)));
                meetingList.add(m);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return meetingList;
    }

    public ArrayList<Meeting> getPastMeetingList2() {
        ArrayList<Meeting> allMeetings = getMeetingList();
        ArrayList<Meeting> pastMeetings = new ArrayList<>();
        for(int i =0; i < allMeetings.size(); i++) {
            long dt = allMeetings.get(i).dateTime;
            if(dt < System.currentTimeMillis()) {
                pastMeetings.add(allMeetings.get(i));
            }
        }
        return pastMeetings;
    }

}
