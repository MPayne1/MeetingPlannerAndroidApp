package matt.meetingplanner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import matt.meetingplanner.Meeting;
import matt.meetingplanner.R;

public class MeetingListAdapter extends BaseAdapter {
    private ArrayList<Meeting> meetingList;
    private LayoutInflater inflater;
    private Context context;

    public MeetingListAdapter(Context _context, ArrayList<Meeting> list) {
        context = _context;
        meetingList = list;
        inflater = LayoutInflater.from(_context);

    }

    @Override
    public int getCount() {
        return meetingList.size();
    }

    @Override
    public Object getItem(int i) {
        return meetingList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if(view == null) {
            view = inflater.inflate(R.layout.view_meeting_entry, null);
            holder = new ViewHolder();
            holder.meetingName = (TextView) view.findViewById(R.id.meeting_list_name);
            holder.meetingDescription = (TextView) view.findViewById(R.id.meeting_list_desc);
            holder.location = (TextView) view.findViewById(R.id.meeting_list_location);
            holder.time = (TextView) view.findViewById(R.id.meeting_list_time);
            holder.date = (TextView) view.findViewById(R.id.meeting_list_date);
            holder.id = (TextView) view.findViewById(R.id.meeting_list_id);
            holder.attendees = (TextView) view.findViewById(R.id.meeting_list_attendees);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.meetingName.setText(meetingList.get(position).name);
        holder.meetingDescription.setText(meetingList.get(position).description);
        holder.location.setText(meetingList.get(position).strLocation);
        holder.time.setText(meetingList.get(position).time);
        holder.date.setText(meetingList.get(position).date);
        holder.attendees.setText(meetingList.get(position).attendees);
        holder.id.setText(String.valueOf(meetingList.get(position).meetingID));

        return view;
    }

    static class ViewHolder{
        TextView meetingName, meetingDescription, location, strLocation, date, time, id, attendees;
    }


}
