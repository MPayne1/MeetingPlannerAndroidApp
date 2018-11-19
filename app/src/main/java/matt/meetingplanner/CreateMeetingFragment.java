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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateMeetingFragment extends Fragment{

    private String mDate;
    private String mTime;
    private View view;
    EditText name;
    EditText description;
    TextView date;
    TextView time;
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

        submitBtn.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                MeetingRepo repo = new MeetingRepo(view.getContext());
                Meeting meeting = new Meeting();

                meeting.name = name.getText().toString();
                meeting.description = description.getText().toString();
                mDate = date.getText().toString();
                mTime = time.getText().toString();
                meeting.dateTime = dateFormat();

                Log.d("DateTime", "onClick: " + mDate + mTime);
                meeting.location = "location 1";

                repo.insert(meeting);
                Toast.makeText(getActivity().getApplicationContext(), "Meeting Created", Toast.LENGTH_SHORT).show();
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
                newDate.set(year, monthOfYear, dayOfMonth);
                String FormattedDate = getString(R.string.dateFormatted, dayOfMonth, monthOfYear, year);
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

    // format the date and time so can be stored in db
    private int dateFormat() {
        String dateTime = mDate + " " + mTime;
        SimpleDateFormat sdf = new SimpleDateFormat("dd / MM / yyyy HH : mm");
        Date date = null;
        try {
            date = sdf.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int dt = (int) date.getTime();
        return dt;
    }
}
