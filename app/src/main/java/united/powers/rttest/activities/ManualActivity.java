package united.powers.rttest.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import united.powers.rttest.DrawerListener;
import united.powers.rttest.R;

public class ManualActivity extends Activity {
    ListView listView ;




    private int extra;
    private String stopName;
    private String[] arra_full_stops;
	public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_STOP_NAME_MESSAGE = "message";
    public static final String EXTRA_ACTIVITY_MESSAGE = "message";
    public static final String EXTRA_STOP_NAME = "stopName";
    private String activityDrawer = DrawerListener.DRAWER_IS_CLOSED;

    public void onBackPressed() {

        if (activityDrawer.equals(DrawerListener.DRAWER_IS_OPEN)){
            Intent broadcast = new Intent(DrawerListener.ACTION_DRAWER);
            broadcast.putExtra(DrawerListener.DRAWER_STATE, DrawerListener.DRAWER_IS_CLOSED);
            sendBroadcast(broadcast);
            Log.i("Sending", "Sending");
        }
        else {
            super.onBackPressed();
        }


        //
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        extra = getIntent().getIntExtra(EXTRA_ACTIVITY_MESSAGE, 0);
        stopName = getIntent().getStringExtra(EXTRA_STOP_NAME);

        // Show the Up button in the action bar.


        getActionBar().setDisplayHomeAsUpEnabled(true);


        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            runFragment(extra,stopName );
        }



        // Get ListView object from xml
        //listView = (ListView) findViewById(R.id.list);
        //arra_full_stops = getResources().getStringArray(R.array.bus_stops_full);




        
        // Defined Array values to show in ListView
        
        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
          android.R.layout.simple_list_item_1, android.R.id.text1, arra_full_stops);


        // Assign adapter to ListView
        listView.setAdapter(adapter); 
        
        // ListView Item Click Listener
        listView.setOnItemClickListener(new OnItemClickListener() {

              @Override
              public void onItemClick(AdapterView<?> parent, View view,
                 int position, long id) {
                
               // ListView Clicked item index
               int itemPosition     = position;
               
               // ListView Clicked item value
               String  itemValue    = (String) listView.getItemAtPosition(position);
                 

             
              }

         }); */
    }
    
    private void runFragment(int position, String stopName){
    	
    	
    	
    	Bundle bundle = new Bundle();
    	bundle.putInt(EXTRA_MESSAGE, position);
        bundle.putString(EXTRA_STOP_NAME_MESSAGE, stopName);
    	
    	ManualListFrag listFrag = new ManualListFrag();
    	
    	listFrag.setArguments(bundle);
    	
    	if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
    		getFragmentManager().beginTransaction()
    	                          .add(android.R.id.content, listFrag).commit();
    	
    	/*getSupportFragmentManager().beginTransaction()
        .add(android.R.id.content, listFrag).commit();*/
    }



}

    void DrawerState(String drawerState){

        activityDrawer = drawerState;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, MainActivity.class));
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }
}