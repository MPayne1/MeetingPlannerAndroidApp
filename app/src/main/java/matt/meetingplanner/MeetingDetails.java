package matt.meetingplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import matt.meetingplanner.converter.DateTimeConverter;
import matt.meetingplanner.converter.LatLngConverter;

public class MeetingDetails extends AppCompatActivity {

    EditText name;
    EditText description;
    TextView date;
    TextView time;
    EditText attendees;
    TextView location;
    Button editBtn;
    Button cancelMeetingBtn;
    LatLng LatLngLocation = null;
    String strLocation;
    Meeting meeting = new Meeting();
    LatLngConverter latLngConverter = new LatLngConverter();
    TextView weatherText;
    ShareActionProvider shareActionProvider;
    String shareContent;
    Intent shareIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meeting_details);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        setUpViewComponents();
        setUpDatePicker();
        setUpTimePicker();
        setUpLocationBtn();
        setUpEditBtn();
        setUpCancelMeetingBtn();
        LatLngLocation = latLngConverter.getLatLngFromString(meeting.location);
        new GetWeather().execute();
    }

    // create options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bar_items, menu);

        setShareContent();
        MenuItem shareItem = menu.findItem(R.id.action_share);
        shareActionProvider = new android.support.v7.widget.ShareActionProvider(this);
        MenuItemCompat.setActionProvider(shareItem, shareActionProvider);
        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, shareContent);
        shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain").setText(shareContent).getIntent();
        shareActionProvider.setShareIntent(shareIntent);

        return true;
    }

    private void setShareContent() {
        shareContent = this.getString(R.string.shareName) + meeting.name + "\n" +
                this.getString(R.string.shareDesc) + meeting.description + "\n" +
                this.getString(R.string.shareDate) + meeting.date + "\n" +
                this.getString(R.string.shareTime) + meeting.time + "\n" +
                this.getString(R.string.shareLocation) + meeting.strLocation;
    }
    // setup settings menu
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(android.provider.Settings.ACTION_DISPLAY_SETTINGS);
                startActivity(intent);
                return true;
        }
        return false;
    }


    // TODO Date needs formatting properly so 0 and 1-9 are displayed properly
    // Setup the date picker and handle the result
    public void setUpDatePicker() {
        final Calendar newCalendar = Calendar.getInstance();
        final Long today = System.currentTimeMillis();
        final DatePickerDialog StartTime = new DatePickerDialog(this, R.style.DatePickerTheme
                ,new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear , dayOfMonth);

                if (newDate.getTimeInMillis() < today) {
                    Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.invalidDate),Toast.LENGTH_SHORT).show();
                } else {
                    newDate.set(year, monthOfYear + 1, dayOfMonth);
                    String FormattedDate = getString(R.string.dateFormatted, dayOfMonth, monthOfYear + 1, year);
                    date.setText(FormattedDate);
                    setShareContent();
                    new GetWeather().execute();
                }

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartTime.show();
            }
        });


    }

    // Setup the time picker and handle the result
    public void setUpTimePicker() {
        time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(v.getContext(), R.style.TimePickerTheme,
                        new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        // TODO need to format time to keep leading 0s and do 00 properly
                        String FormattedTime =  getString(R.string.timeFormatted, selectedHour, selectedMinute);
                        time.setText(FormattedTime);
                        setShareContent();
                        new GetWeather().execute();
                    }
                }, hour, minute, true);//Yes 24 hour time

                timePicker.show();
            }
        });

    }

    // Handle the result from the maps activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (data.hasExtra("location")) {
            LatLng loc = (LatLng) data.getExtras().get("location");
            try{
                LatLngLocation = loc;
                meeting.location = loc.toString();
                strLocation = latLngConverter.getAddress(loc, this);
                meeting.strLocation = strLocation;
                location.setText(strLocation);

                new GetWeather().execute();
                setShareContent();
            }catch(Exception e) {
                e.printStackTrace();
            }

        }
    }

    // Create the location button's onClickListener
    public void setUpLocationBtn() {
        location.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                intent.putExtra("location", LatLngLocation);
                startActivityForResult(intent, 10);
            }
        });
    }

    // Create the edit meeting button's onClickListener
    public void setUpEditBtn() {
        editBtn.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                MeetingRepo repo = new MeetingRepo(getBaseContext());
                if(isFormFilled()) {
                    meeting.name = name.getText().toString();
                    meeting.description = description.getText().toString();
                    meeting.date = date.getText().toString();
                    meeting.time = time.getText().toString();
                    meeting.attendees = attendees.getText().toString();
                    meeting.strLocation = strLocation;
                    repo.updateMeeting(meeting);
                    Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.changesSaved), Toast.LENGTH_SHORT).show();
                    name.setText(null);
                    description.setText(null);
                    time.setText(null);
                    date.setText(null);
                    location.setText(null);
                    weatherText.setText(null);
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.formFilled),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setUpCancelMeetingBtn() {
        cancelMeetingBtn.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                createConfirmCancelDialog().create();
            }
        });
    }

    // set up the view components
    public void setUpViewComponents() {
        Bundle extras = getIntent().getExtras();
        int id = 1;
        if(extras != null) {
            id = extras.getInt("id");
        }
        MeetingRepo repo = new MeetingRepo(this);
        meeting = repo.getMeetingById(id);

        name = (EditText)findViewById(R.id.meeting_details_meetingName);
        description = (EditText) findViewById(R.id.meeting_details_meetingDesc);
        date = (TextView) findViewById(R.id.meeting_details_textDate);
        time = (TextView) findViewById(R.id.meeting_details_textTime);
        location  = (TextView) findViewById(R.id.meeting_details_locationText);
        attendees = (EditText)findViewById(R.id.meeting_details_meetingAttendees);
        editBtn = (Button) findViewById(R.id.meeting_details_editBtn);
        cancelMeetingBtn = (Button) findViewById(R.id.meeting_details_cancelMeetingBtn);
        weatherText = (TextView) findViewById(R.id.meeting_details_weatherText);

        name.setText(meeting.name);
        description.setText(meeting.description);
        date.setText(meeting.date);
        time.setText(meeting.time);
        location.setText(meeting.strLocation);
        attendees.setText(meeting.attendees);

    }



    // Check the form is all filled before it can be submitted
    public boolean isFormFilled() {
        boolean formComplete = false;
        if(name.getText().length() > 0  && description.getText().length() > 0
                && date.getText().length() > 0  && time.getText().length() > 0
                && location.getText().length() > 0) {
            formComplete = true;
        }
        return formComplete;
    }

    // create confirm/cancel dialog when cancelling a meeting
    public AlertDialog createConfirmCancelDialog() {
        AlertDialog cancelDialog = new AlertDialog.Builder(this, R.style.CancelMeetingDialog)
                .setTitle(getBaseContext().getString(R.string.cancelMeeting))
                .setMessage(getBaseContext().getString(R.string.areYouSure))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(getBaseContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MeetingRepo repo = new MeetingRepo(getBaseContext());
                        repo.deleteMeeting(meeting.meetingID);
                        Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.meetingCancelled),
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }})
                .setNegativeButton(getBaseContext().getString(R.string.no), null).show();
        return cancelDialog;
    }


    // Async task to handle the weather api request
    private class GetWeather extends AsyncTask<Void, Void, Void> {
        String res;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void...arg0){
            LatLngConverter latLngConverter = new LatLngConverter();
            DateTimeConverter dateTimeConverter = new DateTimeConverter();
            HttpHandler httpHandler = new HttpHandler();
            LatLng loc = latLngConverter.getLatLngFromString(meeting.location);
            long dateTime = dateTimeConverter.dateTimeConvert(getBaseContext(),meeting.date, meeting.time);
            // "https://api.darksky.net/forecast/[key]/[latitude],[longitude],[time]"
            String url = "https://api.darksky.net/forecast/"+ getString(R.string.forecastAPIKEY)+"/" + loc.latitude + "," + loc.longitude + "," + (dateTime/1000);
            String jsonStr = httpHandler.makeServiceCall(url);

            if(jsonStr != null) {
                try {
                    // Get the summary from the JSON response
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONObject daily = jsonObject.getJSONObject("daily");
                    JSONArray data  = daily.getJSONArray("data");
                    JSONObject summary = data.getJSONObject(0);

                    res =  "Forecast: " + summary.getString("summary");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                res = getBaseContext().getString(R.string.weatherError);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            weatherText.setText(res);
        }
    }

}
