package matt.meetingplanner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import matt.meetingplanner.adapter.MeetingListAdapter;

public class PastMeetings  extends Fragment  {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.past_meetings_fragment, container,false);

        MeetingRepo repo = new MeetingRepo(getContext());
        ArrayList<Meeting> meetingList = repo.getMeetingList();

        if(meetingList.size() != 0) {
            ListView listView = (ListView) rootView.findViewById(R.id.meetingList);
            listView.setAdapter(new MeetingListAdapter(getActivity(), meetingList));
        }

        return rootView;
    }



}
