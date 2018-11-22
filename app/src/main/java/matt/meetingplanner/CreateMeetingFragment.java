package matt.meetingplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CreateMeetingFragment extends Fragment{

    private View view;
    EditText name;
    EditText description;
    TextView date;
    TextView time;
    EditText attendees;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.create_meeting_fragment, container,false);

        setUpDatePicker();
        setUpTimePicker();
        Button submitBtn = (Button) view.findViewById(R.id.submitBtn);

        name = (EditText) view.findViewById(R.id.meetingName);
        description = (EditText) view.findViewById(R.id.meetingDesc);
        date = (TextView) view.findViewById(R.id.textDate);
        time = (TextView) view.findViewById(R.id.textTime);
        attendees = (EditText) view.findViewById(R.id.meetingAttendees);
        MeetingRepo repo = new MeetingRepo(view.getContext());
        Meeting mostRecent = repo.getMostRecentlyCreatedMeeting();
        attendees.setText(mostRecent.attendees);



        submitBtn.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                MeetingRepo repo = new MeetingRepo(view.getContext());
                Meeting meeting = new Meeting();
                Log.d("FormFilled", name.getText().toString());
                Log.d("FormFilled", description.getText().toString());
                Log.d("FormFilled", date.getText().toString());
                Log.d("FormFilled", time.getText().toString());
                if(isFormFilled()) {
                    meeting.name = name.getText().toString();
                    meeting.description = description.getText().toString();
                    meeting.date = date.getText().toString();
                    meeting.time = time.getText().toString();
                    meeting.location = "location 1";
                    meeting.attendees = attendees.getText().toString();

                    repo.insert(meeting);
                    Toast.makeText(getActivity().getApplicationContext(), "@string/meetingCreated", Toast.LENGTH_SHORT).show();
                    name.setText(null);
                    description.setText(null);
                    time.setText(null);
                    date.setText(null);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "@string/formFilled", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }




    // TODO Date needs formatting properly
    public void setUpDatePicker() {
        final TextView textDate = (TextView) view.findViewById(R.id.textDate);
        final Calendar newCalendar = Calendar.getInstance();

        final DatePickerDialog StartTime = new DatePickerDialog(this.getContext(), R.style.DatePickerTheme ,new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear + 1, dayOfMonth);
                String FormattedDate = getString(R.string.dateFormatted, dayOfMonth, monthOfYear + 1, year);
                textDate.setText(FormattedDate);
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartTime.show();

            }
        });
    }

    public void setUpTimePicker() {
        final TextView textTime = (TextView) view.findViewById(R.id.textTime);
        textTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(v.getContext(), R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        // TODO need to format time to keep leading 0s and do 00 properly
                        String FormattedTime =  getString(R.string.timeFormatted, selectedHour, selectedMinute);
                        textTime.setText(FormattedTime);
                    }
                }, hour, minute, true);//Yes 24 hour time

                timePicker.show();
            }
        });
    }

    public boolean isFormFilled() {
        boolean formComplete = false;
        if(name.getText().toString() != null && description.getText().toString() != null
                && date.getText().toString() != null && time.getText().toString() != null) {

            Log.d("FormFilled", name.getText().toString());
            Log.d("FormFilled", description.getText().toString());
            Log.d("FormFilled", date.getText().toString());
            Log.d("FormFilled", time.getText().toString());
            formComplete = true;
        }
        return formComplete;
    }
}
