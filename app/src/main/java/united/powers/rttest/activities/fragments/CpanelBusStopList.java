package united.powers.rttest.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;

import united.powers.rttest.R;
import united.powers.rttest.activities.Slider;
import united.powers.rttest.adapters.BusStopListAdapter;

public class CpanelBusStopList extends ListFragment implements CompoundButton.OnCheckedChangeListener {


    private CompoundButton fifOneCB, fifCB, azCB;
    BusStopListAdapter fullBusStopsRev, fifOneBusStopsRev, fifBusStopsRev, fullBusStops, fifOneBusStops, fifBusStops ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View result=inflater.inflate(R.layout.right_list, container, false);

        fifOneCB = (CompoundButton) result.findViewById(R.id.tbFiftyOne);
        fifCB = (CompoundButton) result.findViewById(R.id.tbFifty);
        azCB = (CompoundButton) result.findViewById(R.id.tbAZList);

        fifCB.setOnCheckedChangeListener(this);
        fifOneCB.setOnCheckedChangeListener(this);
        azCB.setOnCheckedChangeListener(this);

        return result;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fullBusStopsRev=new BusStopListAdapter(getActivity(), getResources().getStringArray(R.array.bus_stop_names_full_rev));
        fifOneBusStopsRev=new BusStopListAdapter(getActivity(), getResources().getStringArray(R.array.bus_stop_names_fif_one_rev));
        fifBusStopsRev=new BusStopListAdapter(getActivity(), getResources().getStringArray(R.array.bus_stop_names_fif_rev));
        fullBusStops=new BusStopListAdapter(getActivity(), getResources().getStringArray(R.array.bus_stops_full));
        fifOneBusStops=new BusStopListAdapter(getActivity(), getResources().getStringArray(R.array.bus_stops_f));
        fifBusStops=new BusStopListAdapter(getActivity(), getResources().getStringArray(R.array.bus_stops));

        //BusStopListAdapter fullBusStops;
        //general list of the busStops
        fullBusStops=new BusStopListAdapter(getActivity(), getResources().getStringArray(R.array.bus_stops_full));
        setListAdapter(fullBusStops);
        //listView speed
        getListView().setFriction(ViewConfiguration.getScrollFriction()* 1);




    }

    @Override
    public void onListItemClick(ListView lv, View v, int position, long id) {
        //Passing position and the name of the busstop to the fragment ManualListFrag
        //position is not used in the manual fragment
        String selection = String.valueOf(lv.getItemAtPosition(position));
        //******* Working ******* Log.i("Click informer", selection + "is clicked");
        ((Slider)getActivity()).startListing(position, selection);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()){

            case R.id.tbFifty:

                if (fifCB.isChecked()){

                    //fifCB.setChecked(false);
                    fifOneCB.setChecked(false);
                    azCB.setChecked(false);

                    setListAdapter(fifBusStops);

                    //turning of other buttons and functions

                }

                else {
                    azCB.setChecked(false);

                    setListAdapter(fullBusStops);
                    //turning of other buttons and functions

                }

            case R.id.tbFiftyOne:

                if (fifOneCB.isChecked()){

                    fifCB.setChecked(false);
                    azCB.setChecked(false);

                    setListAdapter(fifOneBusStops);
                    //turning of other buttons and functions

                }

                else {

                    azCB.setChecked(false);

                    setListAdapter(fullBusStops);
                    //turning of other buttons and functions

                }

            case R.id.tbAZList:

                if (azCB.isChecked()) {

                    if (!fifCB.isChecked() & !fifOneCB.isChecked()){

                        setListAdapter(fullBusStopsRev);
                    }

                    else if (!fifCB.isChecked() & fifOneCB.isChecked()){

                        setListAdapter(fifOneBusStopsRev);
                    }

                    else if (fifCB.isChecked() & !fifOneCB.isChecked()){

                        setListAdapter(fifBusStopsRev);
                    }


                }
                else {

                    if (!fifCB.isChecked() & !fifOneCB.isChecked()){

                        setListAdapter(fullBusStops);
                    }

                    else if (!fifCB.isChecked() & fifOneCB.isChecked()){

                        setListAdapter(fifOneBusStops);
                    }

                    else if (fifCB.isChecked() & !fifOneCB.isChecked()){

                        setListAdapter(fifBusStops);
                    }



                }

        }
    }
}
