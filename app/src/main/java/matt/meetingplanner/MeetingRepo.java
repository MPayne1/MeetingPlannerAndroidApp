package matt.meetingplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MeetingRepo {
    private DBHelper dbHelper;


    public MeetingRepo (Context context) {
        dbHelper = new DBHelper(context);
    }

    // Insert new meeting
    public void insert(Meeting meeting) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Meeting.KEY_NAME, meeting.name);
        values.put(Meeting.KEY_DESCRIPTION, meeting.description);
        values.put(Meeting.KEY_LOCATION, meeting.location);
        values.put(Meeting.KEY_STR_LOCATION, meeting.strLocation);
        values.put(Meeting.KEY_DATE, meeting.date);
        values.put(Meeting.KEY_TIME, meeting.time);
        values.put(Meeting.KEY_ATTENDEES, meeting.attendees);
        db.insert(Meeting.TABLE, null, values);
        db.close();
    }


    // get list of all meetings
    public ArrayList<Meeting> getMeetingList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Meeting.TABLE +";";

        ArrayList<Meeting> meetingList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if(cursor.moveToFirst()) {
            do {
                Meeting m = new Meeting();
                meetingList.add(createNewMeeting(m, cursor));
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return meetingList;
    }

    // Get list of all past meetings
    public ArrayList<Meeting> getPastMeetingList() {
        ArrayList<Meeting> meetingList= getMeetingList();
        ArrayList<Meeting> pastMeetings = new ArrayList<>();
        Calendar currentTime= Calendar.getInstance();

        for(int i = 0; i < meetingList.size(); i++) {
            long dateTime = dateTimeConvert(meetingList.get(i).date, meetingList.get(i).time);
            int compare = Long.compare(dateTime, currentTime.getTimeInMillis());
            Log.d("Compare", ""+compare);
            if(compare < 0){ // if compare < 0 meetingdate < currenttime
                Log.d("DateTime", "Meeting time:" + dateTime + " Current time:" + System.currentTimeMillis());
                pastMeetings.add(meetingList.get(i));
            }
        }

        return pastMeetings;
    }

    // Get list of all future meetings
    public ArrayList<Meeting> getFutureMeetingList() {
        ArrayList<Meeting> meetingList= getMeetingList();
        ArrayList<Meeting> futureMeetings = new ArrayList<>();
        Calendar currentTime= Calendar.getInstance();

        for(int i = 0; i < meetingList.size(); i++) {
            long dateTime = dateTimeConvert(meetingList.get(i).date, meetingList.get(i).time);
            int compare = Long.compare(dateTime, currentTime.getTimeInMillis());
            Log.d("Compare", ""+compare);
            if(compare > 0){ // if compare < 0 meetingdate < currenttime
                Log.d("DateTime", "Meeting time:" + dateTime + " Current time:" + System.currentTimeMillis());
                futureMeetings.add(meetingList.get(i));
            }
        }

        return futureMeetings;
    }

    // Get the most recent meeting, for the attendees autocomplete
    public Meeting getMostRecentlyCreatedMeeting() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Meeting.TABLE +";";


        Cursor cursor = db.rawQuery(selectQuery, null);
        Meeting m = new Meeting();

        if(cursor.moveToLast()) {
            createNewMeeting(m, cursor);
        }
        cursor.close();
        db.close();
        return m;
    }

    private long dateTimeConvert(String date, String time) {
        long timeInMilliseconds =0 ;
        String givenDateString = date + " " + time;
        SimpleDateFormat sdf = new SimpleDateFormat("dd / MM / yyyy HH : mm", Locale.ENGLISH);
        String timeZone = "GMT";//it can be anything timezone like IST, GMT.
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        try {
            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

    // Add all relevant data to new meeting object
    private Meeting createNewMeeting(Meeting m, Cursor cursor) {
        m.name = cursor.getString(cursor.getColumnIndex(Meeting.KEY_NAME));
        m.description = cursor.getString(cursor.getColumnIndex(Meeting.KEY_DESCRIPTION));
        m.date = cursor.getString(cursor.getColumnIndex(Meeting.KEY_DATE));
        m.time = cursor.getString(cursor.getColumnIndex(Meeting.KEY_TIME));
        m.location = cursor.getString(cursor.getColumnIndex(Meeting.KEY_LOCATION));
        m.strLocation = cursor.getString(cursor.getColumnIndex(Meeting.KEY_STR_LOCATION));
        m.attendees = cursor.getString(cursor.getColumnIndex(Meeting.KEY_ATTENDEES));
        m.meetingID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Meeting.KEY_ID)));

        return m;
    }
}

