package matt.meetingplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

public class CreateMeetingFragment extends Fragment{

    private View view;
    EditText name;
    EditText description;
    TextView date;
    TextView time;
    EditText attendees;
    TextView location;
    Meeting meeting = new Meeting();
    Button submitBtn;
    LatLng LatLngLocation = null;
    String strLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.create_meeting_fragment, container,false);

        setUpDatePicker();
        setUpTimePicker();

        setUpViewComponents(view);

        setUpSubmitBtn();
        setUpLocationBtn();

        return view;
    }

    // TODO make so can't select past date/time
    // TODO Date needs formatting properly so 0 and 1-9 are displayed properly
    // // Setup the date picker and handle the result
    public void setUpDatePicker() {
        final TextView textDate = (TextView) view.findViewById(R.id.textDate);
        final Calendar newCalendar = Calendar.getInstance();

        final DatePickerDialog StartTime = new DatePickerDialog(this.getContext(),
                R.style.DatePickerTheme ,new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear + 1, dayOfMonth);
                String FormattedDate = getString(R.string.dateFormatted, dayOfMonth, monthOfYear + 1, year);
                textDate.setText(FormattedDate);
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));

        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartTime.show();

            }
        });
    }

    // Setup the time picker and handle the result
    public void setUpTimePicker() {
        final TextView textTime = (TextView) view.findViewById(R.id.textTime);
        textTime.setOnClickListener(new View.OnClickListener() {

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

                        String FormattedTime =  getString(R.string.timeFormatted, selectedHour,
                                selectedMinute);
                        textTime.setText(FormattedTime);
                    }
                }, hour, minute, true);//Yes 24 hour time

                timePicker.show();
            }
        });
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

    // Handle the result from the maps activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
            if (data.hasExtra("location")) {
                try {
                    LatLng loc = (LatLng) data.getExtras().get("location");
                    LatLngLocation = loc;
                    meeting.location = loc.toString();
                    strLocation = getAddress(loc);
                    location.setText(strLocation);
                }
                catch(NullPointerException e) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getContext().getString(R.string.addLocation), Toast.LENGTH_SHORT).show();
                }

            }
    }

    // Create the submit button's onClickListener
    public void setUpSubmitBtn() {
        submitBtn.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                MeetingRepo repo = new MeetingRepo(view.getContext());
                if(isFormFilled()) {
                    meeting.name = name.getText().toString();
                    meeting.description = description.getText().toString();
                    meeting.date = date.getText().toString();
                    meeting.time = time.getText().toString();
                    meeting.attendees = attendees.getText().toString();
                    meeting.strLocation = strLocation;
                    repo.insert(meeting);
                    Toast.makeText(getActivity().getApplicationContext(),
                            getContext().getString(R.string.meetingCreated), Toast.LENGTH_SHORT).show();
                    name.setText(null);
                    description.setText(null);
                    time.setText(null);
                    date.setText(null);
                    location.setText(null);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getContext().getString(R.string.formFilled), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Create the location button's onClickListener
    public void setUpLocationBtn() {
        location.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapsActivity.class);
                if(LatLngLocation != null) intent.putExtra("location", LatLngLocation);
                startActivityForResult(intent, 10);
            }
        });
    }

    // Setup the components on the view
    public void setUpViewComponents(View view){
        submitBtn = (Button) view.findViewById(R.id.submitBtn);
        name = (EditText) view.findViewById(R.id.meetingName);
        description = (EditText) view.findViewById(R.id.meetingDesc);
        date = (TextView) view.findViewById(R.id.textDate);
        time = (TextView) view.findViewById(R.id.textTime);
        location = (TextView) view.findViewById(R.id.locationText);
        attendees = (EditText) view.findViewById(R.id.meetingAttendees);
        MeetingRepo repo = new MeetingRepo(view.getContext());
        Meeting mostRecent = repo.getMostRecentlyCreatedMeeting();
        attendees.setText(mostRecent.attendees);
    }

    // Get the address of the meeting location, better for usability than latlng
    public String getAddress(LatLng location) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        try{
            List<Address> addresses = geocoder.getFromLocation(location.latitude,
                    location.longitude, 1);
            if(addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            }
        } catch (Exception e) {

        }
        return strAdd;
    }
}
