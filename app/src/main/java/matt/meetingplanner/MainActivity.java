package matt.meetingplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        setUpDatePicker();
        setUpTimePicker();

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bar_items, menu);
        return true;
    }

    // TODO Date needs formatting properly
    public void setUpDatePicker() {
        final TextView textDate = (TextView) findViewById(R.id.textDate);
        final Calendar newCalendar = Calendar.getInstance();

        final DatePickerDialog  StartTime = new DatePickerDialog(this, R.style.DatePickerTheme ,new DatePickerDialog.OnDateSetListener() {
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
        final TextView textTime = (TextView) findViewById(R.id.textTime);
        textTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(MainActivity.this, R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        // TODO need to format time to keep leading 0s and do 00 properlty
                        String FormattedTime =  getString(R.string.timeFormatted, selectedHour, selectedMinute);
                        textTime.setText(FormattedTime);
                    }
                }, hour, minute, true);//Yes 24 hour time

                timePicker.show();
            }
        });
    }


    public void submitMeeting(View view) {
        //TODO submit meeting
        Toast.makeText(this, "Submitted", Toast.LENGTH_SHORT).show();
    }
}

