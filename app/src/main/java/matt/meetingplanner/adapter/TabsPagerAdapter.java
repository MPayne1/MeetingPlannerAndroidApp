package matt.meetingplanner.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import matt.meetingplanner.CreateMeetingFragment;
import matt.meetingplanner.FutureMeetingsFragment;
import matt.meetingplanner.PastMeetings;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch(index) {
            case 0:
                return new CreateMeetingFragment();
            case 1:
                return new PastMeetings();
            case 2:
                return new FutureMeetingsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
