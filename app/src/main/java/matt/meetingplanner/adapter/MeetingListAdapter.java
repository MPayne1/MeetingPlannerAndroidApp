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

    public MeetingListAdapter(Context context, ArrayList<Meeting> list) {
        meetingList = list;
        inflater = LayoutInflater.from(context);
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
            holder.date = (TextView) view.findViewById(R.id.meeting_list_date);
            holder.time = (TextView) view.findViewById(R.id.meeting_list_time);
            holder.id = (TextView) view.findViewById(R.id.meeting_list_id);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.meetingName.setText(meetingList.get(position).name);
        holder.meetingDescription.setText(meetingList.get(position).description);
        holder.location.setText(meetingList.get(position).location);
        holder.date.setText(meetingList.get(position).date);
        holder.time.setText(meetingList.get(position).time);
        //holder.id.setText(meetingList.get(position).meetingID);

        return view;
    }

    static class ViewHolder{
        TextView meetingName, meetingDescription, location, date, time, id;
    }
}
