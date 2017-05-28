package united.powers.rttest.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import united.powers.rttest.R;
import united.powers.rttest.activities.BaseActivity;
import united.powers.rttest.activities.Slider;
import united.powers.rttest.adapters.ControlPanelListAdapter;

public class CpanelNavigation extends ListFragment {

    private long mTitle;
    public static int [] prgmImages={R.drawable.ic_all_out_black_48dp,R.drawable.ic_directions_bus_black_48dp,R.drawable.ic_euro_symbol_black_48dp,R.drawable.ic_info_black_48dp};
    ControlPanelListAdapter listAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mTitle = getArguments().getInt(BaseActivity.TITLERES);
        return inflater.inflate(R.layout.list, null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listAdapter = new ControlPanelListAdapter(getActivity(), getResources().getStringArray(R.array.planets_array), prgmImages);
        setListAdapter(listAdapter);
    }

    @Override
    public void onListItemClick(ListView lv, View v, int position, long id) {

        switch (position){
            case 0:
                ((Slider) getActivity()).detachNe();
                break;

            case 1:
                if (getActivity().getTitle().equals("Bus Tracker")){
                    ((Slider) getActivity()).getSlidingMenu().toggle();
                }

                else {
                    ((Slider) getActivity()).restart();
                }
                break;

            case 2:
                ((Slider) getActivity()).runCalculator();
                break;

            case 3:
                ((Slider) getActivity()).runAboutUs();
                break;

        }
    }
}
