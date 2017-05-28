package united.powers.rttest.activities;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import united.powers.rttest.database.DatabaseHelper;
import united.powers.rttest.DrawerListener;
import united.powers.rttest.R;

public class ManualListFrag extends Fragment implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

	private static final String TAG = "Manal list";


	private TableLayout tLayout;
        
    private TextView txtBusNumber, txtType, txtBGStop, txtTime, txtStop, txtTimeLeft,  txtWarn, txtYasumiNote;

	private RelativeLayout rLayout, rLayoutIkki;


	private static final String fontPath = "fonts/bb.ttf";

	private SlidingUpPanelLayout mLayout;

	private String holidayInformer = yasumiCalc();
	private String BUS_STOP_NAME = " ";
	private String BUS_STOP_NAME_TABLE_INNER = "nam";
	private String type = " ";
	private String busNumber = " ";
	private String busTimeHour = " ";
	private String busType = " ";
	private String direction = "";
	private String directionUI = " ";
	private String fif = "";
	private String freeName;
	private String freeTable;
	private String nextStop = "";
	private String nextStop2 = "";
	private long additionalTime = 0;

	private String[] arraName;
	private String[] arraName_f;
	private String[] arraStop;
	private String[] arraStop_f;
	private String[] arra_full_stops;
	private String[] arra_full_tables;
	private String drawerState = "Closed";
	//private int[] row_ids;
	//update in 2017/01/21
	private String[] time_differences_50_down;
	private String[] time_differences_51_down;
	private String[] time_differences_51_apu;
	private String[] time_differences_50_apu;
	
	private int busStopId;
	private int busNameId;
	private int namba;
	private int i = 0;
	private int positionOFF;

    private LinearLayout pLayout;
	
	private Button btnBackTime, btnNext ;
	

	
	private Spinner sp_number;
	
	private boolean firstTime = true;
	private boolean chik = false;
	private boolean auto_mode = true;

	private CompoundButton EkiIki, APUIki;
	
	private Cursor cursor = null;
	private Cursor panelCursor;

	private DatabaseHelper db = null;

	private long taymu = 1001L;
	Handler timerHandler = new Handler();
	Runnable timerRunnable = new Runnable() {
		public void run() {
			if (chik== true) {
				chik = false;
			}
			
				timerHandler.postDelayed(this, 500);
				if (taymu <= 1000) {
					queryTesta();
				}
				updateUI();
			
		}
	};




/*
	private BroadcastReceiver mLocationReceiver = new LocationReceiver() {

		@Override
		protected void onLocationReceived(Context context, Location loc,
				String BUS_STOP, String BUS_STOP_TABLE) {

			BUS_STOP_NAME = BUS_STOP;
			if (!BUS_STOP_NAME_TABLE_INNER.equals(BUS_STOP_TABLE)) {
				BUS_STOP_NAME_TABLE_INNER = BUS_STOP_TABLE;
				queryTesta();
				auto_mode = true;
			}
		}

	};
    
    */

	private BroadcastReceiver DrawerReciever = new DrawerListener() {

		@Override
		protected void onDrawerStateReceived(Context context, String DrawerState) {
			Log.i("Drawer Reciever", "Collapsing");
			drawerState = DrawerState;
			mLayout.setPanelState(PanelState.COLLAPSED);


		}

	};
	
	
	
	@Override
	public void onResume() {
		super.onResume();

		getActivity().registerReceiver(DrawerReciever,
				new IntentFilter(DrawerListener.ACTION_DRAWER));

		timerHandler.postDelayed(timerRunnable, 0);
		if (BUS_STOP_NAME_TABLE_INNER != "nam") {
			
			queryTesta();
		}
	};
	

	
	



	@Override
	public void onPause() {

		timerHandler.removeCallbacks(timerRunnable);
		getActivity().unregisterReceiver(DrawerReciever);

		super.onPause();


	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);

      setHasOptionsMenu(true);
      setRetainInstance(true);
		Bundle bundle = getArguments();
		String stopName = bundle.getString(ManualActivity.EXTRA_STOP_NAME_MESSAGE);

        
      db=new DatabaseHelper(getActivity());
		getActivity().registerReceiver(DrawerReciever,
				new IntentFilter(DrawerListener.ACTION_DRAWER));

		//panelDrawer();
		queryTesta();

		if (stopName.equals("Ritsumeikan Asia Pacific University") ||
				stopName.equals("Mori Kosaten") ||
				stopName.equals("Mori Iriguchi") ||
				stopName.equals("APU House Mae")  ) {

			DirectionSwitch("_b");
		}
        else if (stopName.equals("Beppu Station") ||
                stopName.equals("Ekimae Honmachi") ||
                stopName.equals("Beppu Kitahama")   ) {

            DirectionSwitch("");
        }



		timerHandler.postDelayed(timerRunnable, 0);
      
      
    }
	
	@Override
	  public View onCreateView(LayoutInflater inflater,
	                           ViewGroup container,
	                           Bundle savedInstanceState) {
		
		Bundle bundle = getArguments();
		
		int position = bundle.getInt(ManualActivity.EXTRA_MESSAGE);
		String stopName = bundle.getString(ManualActivity.EXTRA_STOP_NAME_MESSAGE);

		positionOFF = position;
		
	    View result=inflater.inflate(R.layout.manual_fragment, container, false);
        
        pLayout = ((LinearLayout) result.findViewById(R.id.panel));
        tLayout = ((TableLayout) result.findViewById(R.id.panelTableLayout));
		rLayout = ((RelativeLayout) result.findViewById(R.id.relativeLayoutBir));
		rLayoutIkki = ((RelativeLayout) result.findViewById(R.id.relativeLayoutIkki));

		final int sdk = android.os.Build.VERSION.SDK_INT;
		/*if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			rLayoutIkki.setBackgroundDrawable( getResources().getDrawable(R.drawable.bustop) );
		} else {
			rLayoutIkki.setBackground( getResources().getDrawable(R.drawable.bustop));
		}*/
        
        txtBusNumber = ((TextView) result.findViewById(R.id.TxtBusNumber));
		txtYasumiNote = ((TextView) result.findViewById(R.id.txtYasumiNote));

        APUIki = (CompoundButton) result.findViewById(R.id.MAPUIki);
        EkiIki = (CompoundButton) result.findViewById(R.id.MEkiIki);

        APUIki   .setOnCheckedChangeListener(this);
        EkiIki   .setOnCheckedChangeListener(this);
	    
	    txtTime = ((TextView) result.findViewById(R.id.txtTime));
	    txtBGStop = ((TextView)result.findViewById(R.id.txtBGStop));
	    txtType = ((TextView)result.findViewById(R.id.txtType));
	    txtWarn = ((TextView)result.findViewById(R.id.txtWarn));
	    
	    txtTimeLeft = ((TextView)result.findViewById(R.id.txtTimeLeft));
	    
	    Typeface tf = Typeface.createFromAsset((getActivity()).getAssets(), fontPath);
	    txtTimeLeft.setTypeface(tf);
	    
	    txtStop = ((TextView)result.findViewById(R.id.txtStop));



		mLayout = (SlidingUpPanelLayout) result.findViewById(R.id.sliding_layout);
		mLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
			@Override
			public void onPanelSlide(View panel, float slideOffset) {
				Log.i(TAG, "onPanelSlide, offset " + slideOffset);
			}

			@Override
			public void onPanelExpanded(View panel) {
				Log.i(TAG, "onPanelExpanded");
				((ManualActivity)getActivity()).DrawerState(DrawerListener.DRAWER_IS_OPEN);
			}

			@Override
			public void onPanelCollapsed(View panel) {
				Log.i(TAG, "onPanelCollapsed");
				((ManualActivity)getActivity()).DrawerState(DrawerListener.DRAWER_IS_CLOSED);
			}

			@Override
			public void onPanelAnchored(View panel) {
				Log.i(TAG, "onPanelAnchored");
			}

			@Override
			public void onPanelHidden(View panel) {
				Log.i(TAG, "onPanelHidden");
			}
		});
	    
	    
	   
	    
	    //btnPrev = ((Button)result.findViewById(R.id.btnPrev));
	    btnBackTime = ((Button)result.findViewById(R.id.btnBackTime));
	    btnNext = ((Button)result.findViewById(R.id.btnNext));
	    //btnForward = ((Button)result.findViewById(R.id.btnForward));
	    //btnReset = ((Button)result.findViewById(R.id.btnReset));

	   
	    
	    sp_number = ((Spinner)result.findViewById(R.id.spNumber));	   

	   
	    
	    sp_number.setOnItemSelectedListener(this);	 

	   
	    
	    arraStop = getResources().getStringArray(R.array.bus_stop_tables);
	    arraName = getResources().getStringArray(R.array.bus_stops);
	    arraName_f = getResources().getStringArray(R.array.bus_stops_f);
	    arraStop_f = getResources().getStringArray(R.array.bus_stop_tables_f);
	    arra_full_stops = getResources().getStringArray(R.array.bus_stops_full);
	    arra_full_tables = getResources().getStringArray(R.array.bus_stop_tables_full);
		//row_ids = getResources().getIntArray(R.array.row_id);
	    //update in 2017
		time_differences_50_down = getResources().getStringArray(R.array.time_differences_50_down);
		time_differences_51_down = getResources().getStringArray(R.array.time_differences_51_down);
		time_differences_50_apu = getResources().getStringArray(R.array.time_differences_50_apu);
		time_differences_51_apu = getResources().getStringArray(R.array.time_differences_51_apu);



		ArrayAdapter<CharSequence> adapterNumbers = ArrayAdapter
				.createFromResource(getActivity(), R.array.bus_numbers,
						android.R.layout.simple_spinner_item);

		ArrayAdapter<CharSequence> adapterTypes = ArrayAdapter
				.createFromResource(getActivity(), R.array.bus_types,
						android.R.layout.simple_spinner_item);

		ArrayAdapter<CharSequence> adapterDirection = ArrayAdapter
				.createFromResource(getActivity(), R.array.bus_direction,
						android.R.layout.simple_spinner_item);

		ArrayAdapter<CharSequence> adapterFullList = ArrayAdapter
				.createFromResource(getActivity(), R.array.bus_stops_full,
						android.R.layout.simple_spinner_item);

		adapterNumbers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterDirection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterFullList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		sp_number.setAdapter(adapterNumbers);

		   
		cleanUI("OP op op op op ");
		
		BUS_STOP_NAME = stopName;
		BUS_STOP_NAME_TABLE_INNER = arra_full_tables[stopNameId(stopName)];

		
		updateUI();
        //APUIki.setChecked(true);
		Calendar cal = Calendar.getInstance();

		int hourofday = cal.get(Calendar.HOUR_OF_DAY);
		
		long millis = System.currentTimeMillis();
		Log.i("Create View", "Name table " + BUS_STOP_NAME_TABLE_INNER + " " + String.format("%02d:%02d:%02d",
				hourofday,
				TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
				TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1)));

		

		
		btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtWarn.setText("");
                if (cursor != null) {
                    //queryTesta();
                    if (cursor.getPosition() >= cursor.getCount() - 1) {
                        txtWarn.setText("This is the last bus");
                    } else {
                        cursor.moveToNext();
                        busTimeHour = cursor.getString(cursor
                                .getColumnIndex(DatabaseHelper.TIME));
                        busNumber = cursor.getString(cursor
                                .getColumnIndex(DatabaseHelper.NUMBER));
                        busType = cursor.getString(cursor
                                .getColumnIndex(DatabaseHelper.TYPE));


                    }

                    updateUI();


                }

            }
        });
		
		btnBackTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cursor != null & cursor.getCount() > 0) {

					cursor.moveToFirst();
					busTimeHour = cursor.getString(cursor
							.getColumnIndex(DatabaseHelper.TIME));
					busNumber = cursor.getString(cursor
							.getColumnIndex(DatabaseHelper.NUMBER));
					busType = cursor.getString(cursor
							.getColumnIndex(DatabaseHelper.TYPE));

					txtWarn.setText("");
					updateUI();


				}

			}
		});


        firstTime = true;
         return result;

    }
    

    
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
    		long id) {

    	switch (parent.getId()) {
    	case R.id.spNumber:
    		switch (position) {
    		case 0:
    			fif = " ";
    			if (BUS_STOP_NAME_TABLE_INNER != "nam") {
    				txtWarn.setText("");
    				cleanUI("");
    				queryTesta();
    				
    			}
    			break;
    		case 1:
    			fif = " AND number = 50 ";
    			if (BUS_STOP_NAME_TABLE_INNER != "nam") {
    				cleanUI("");
    				txtWarn.setText("");
    				queryTesta();
    				
    			}
    			break;
    		case 2:
    			fif = " AND number = 51 ";
    			if (BUS_STOP_NAME_TABLE_INNER != "nam") {
    				cleanUI("");
    				txtWarn.setText("");
    				queryTesta();
    				
    			}
    			break;
    		case 3:
    			fif = " AND number = 52 ";
    			if (BUS_STOP_NAME_TABLE_INNER != "nam") {
    				txtWarn.setText("");
    				cleanUI("");
    				queryTesta();
    			
    			}
    			break;
    		case 4:
    			fif = " AND number = 53 ";
    			if (BUS_STOP_NAME_TABLE_INNER != "nam") {
    				cleanUI("");
    				txtWarn.setText("");
    				queryTesta();
    				
    			}
    			break;
    		case 5:
    			fif = " AND number = 54 ";
    			if (BUS_STOP_NAME_TABLE_INNER != "nam") {
    				cleanUI("");
    				txtWarn.setText("");
    				queryTesta();
    				
    			}
    			break;
    		case 6:
    			fif = " AND number = 55 ";
    			if (BUS_STOP_NAME_TABLE_INNER != "nam") {
    				cleanUI("");
    				txtWarn.setText("");
    				queryTesta();
    				
    			}
    			break;
    		}

    		break;
    		

    	}

    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_manual, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	private void queryTesta() {
    	

    	
    	if (BUS_STOP_NAME_TABLE_INNER.equals(DatabaseHelper.BUS_STOPS)){
    		cleanUI("You are out of coverage area");
    	}
    	
    	else  {
    		
    	
    	try {
    		
    		
    		
    		Calendar cal = Calendar.getInstance();

    		int hourofday = cal.get(Calendar.HOUR_OF_DAY);
    		
    		long millis = System.currentTimeMillis();
    		
    		if (auto_mode == true){
    		cursor = db.getReadableDatabase().rawQuery("SELECT number, timer, type, hour, minute FROM " + BUS_STOP_NAME_TABLE_INNER + direction + holidayInformer +" WHERE time(timer) > time('" + String.format("%02d:%02d:%02d",
        			hourofday,
        		    TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
        		    TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1))+ "')" + fif , null);

    		}
    		
    		if (auto_mode == false){
    			cursor = db.getReadableDatabase().rawQuery("SELECT number, timer, type, hour, minute FROM " + BUS_STOP_NAME_TABLE_INNER + direction+ holidayInformer +" WHERE time(timer) > time('" + String.format("%02d:%02d:%02d",
            			hourofday,
            		    TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
            		    TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1))+ "')" + fif , null);
    		}
    		
    		if (cursor.getCount() <= 0) {

    			if (auto_mode == true){
    			cursor = db.getReadableDatabase().rawQuery(
    					"SELECT number, timer, type, hour, minute FROM "
    							+ BUS_STOP_NAME_TABLE_INNER + direction + holidayInformer
    							+ " WHERE time(timer) > time('07:00:00')" + fif
    						, null);
    			}
    			
    			if (auto_mode == false){
    				cursor = db.getReadableDatabase().rawQuery(
    						"SELECT number, timer, type, hour, minute FROM "
    								+ BUS_STOP_NAME_TABLE_INNER + direction + holidayInformer
    								+ " WHERE time(timer) > time('07:00:00')" + fif
    								, null);
    			}


    			if (cursor.getCount() > 0) {
    				cursor.moveToFirst();
    				busTimeHour = cursor.getString(cursor
    						.getColumnIndex(DatabaseHelper.TIME));
    				busNumber = cursor.getString(cursor
    						.getColumnIndex(DatabaseHelper.NUMBER));
    				busType = cursor.getString(cursor
    						.getColumnIndex(DatabaseHelper.TYPE));
    				updateUI();
    			}
    			if (cursor.getCount() <= 0) {
    				cleanUI("There is no such bus");
    				
    				chik = true;
    			}
    		}

    		if (cursor.getCount() > 0) {

    			cursor.moveToFirst();
    			
    			
    			busTimeHour = cursor.getString(cursor
    					.getColumnIndex(DatabaseHelper.TIME));
    			busNumber = cursor.getString(cursor
    					.getColumnIndex(DatabaseHelper.NUMBER));
    			busType = cursor.getString(cursor
    					.getColumnIndex(DatabaseHelper.TYPE));
    			
    			updateUI();

    			Log.i("Day Manager", busTimeHour);

    		}

    	} finally {

    	}
    	
    	}

    }
    
    private void updateUI() {

		if (holidayInformer.equals("_y")){
            txtYasumiNote.setTextColor(getResources().getColor(R.color.Red));
			txtYasumiNote.setText("Non-school day");
		}
        else {
            txtYasumiNote.setTextColor(getResources().getColor(R.color.Green));
            txtYasumiNote.setText("School day");

        }

    	txtBusNumber.setText(busNumber); 
    	txtType.setText("");
        if(EkiIki.isChecked()){
            txtBGStop.setText("DownTown");
        }
        else {
            txtBGStop.setText("APU");
        }
    	txtTime.setText(busTimeHour); 
    	txtStop.setText("Bus Stop: " + BUS_STOP_NAME);
    	txtTimeLeft.setText(timeLeft());
    	//txtWarn.setText("");
    	getActivity().setTitle("Manual Mode");

    }
    
    private void cleanUI(String warning) {
    	txtBusNumber.setText("--"); 
    	txtType.setText("----"); 
    	txtBGStop.setText("---"); 
    	txtTime.setText("--:--:--"); 
    	txtStop.setText("Bus Stop: Not defined");
    	txtTimeLeft.setText("--:--:--");
		txtWarn.setTextColor(getResources().getColor(R.color.Red));
    	txtWarn.setText(warning);
    	

    }
    
    private String timeLeft() {

    	Calendar cal = Calendar.getInstance();

    	int hourofday = cal.get(Calendar.HOUR_OF_DAY);

    	long millis = System.currentTimeMillis();
    	String time = "";
    	String time2 = "";

    	String present = String.format(
    			"%02d:%02d:%02d",
    			hourofday,
    			TimeUnit.MILLISECONDS.toMinutes(millis)
    					% TimeUnit.HOURS.toMinutes(1),
    			TimeUnit.MILLISECONDS.toSeconds(millis)
    					% TimeUnit.MINUTES.toSeconds(1));

    	SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
    	Date pTime = null;
    	Date bTime = null;
    	int hours;
    	int minutes;
    	int seconds;

    	try {

    		pTime = format.parse(present);
    		bTime = format.parse(busTimeHour);

    		if (bTime.getTime() > pTime.getTime()) {

    			long diff = bTime.getTime() - pTime.getTime();

    			long diffSeconds = diff / 1000 % 60;
    			long diffMinutes = diff / (60 * 1000) % 60;
    			long diffHours = diff / (60 * 60 * 1000) % 24;
    			
    			time =  String.valueOf(diffHours) +":"+  String.valueOf(diffMinutes) + ":"
    					+ String.valueOf(diffSeconds) ;
    			
    		 time2 = String.format(Locale.getDefault(),"%02d:%02d:%02d", hours = (int) diffHours ,	minutes = (int) diffMinutes, seconds = (int) diffSeconds );

    			
    		
    			taymu = diff;

    		}
    		
    		

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	

    	
    	return time2;
    }

    
    private int stopId() {

    	int position;
    	
    	position = Arrays.asList(arra_full_tables).indexOf(BUS_STOP_NAME_TABLE_INNER);
    	return position;

    }


    private int stopNameId(String name) {
	int position;
	
		 position = Arrays.asList(arra_full_stops).indexOf(name);

	return position;

}




    private void increment(int stop, int name) {

	
		Log.i("crementer", "In");
		if (stop == arra_full_tables.length-1) {
			stop = 0;
		} else {
			stop++;
		}

		if (name == arra_full_stops.length-1) {
			name = 0;
		} else {
			name++;
		}

		busStopId = stop;
		busNameId = name;
	
}

    private void decrement(int stop, int name) {

	int position = sp_number.getSelectedItemPosition();
	

		
		Log.i("crementer", "In def" + stop + name);
		if (stop == 0) {
			stop = arra_full_tables.length-1;
		} else {
			stop--;
		}

		if (name == 0) {
			name = arra_full_stops.length-1;
		}  else {
			name--;
		}
		busStopId = stop;
		busNameId = name;





		
	
    }

	private String yasumiCalc(){

        Cursor holidayCursor = null;


		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdfMonth = new SimpleDateFormat("M");
        SimpleDateFormat sdfDay = new SimpleDateFormat("d");

		Date d = new Date();
		String dayOfTheWeek = sdf.format(d);
        String year = sdfYear.format(d);
        String month = sdfMonth.format(d);
        String day = sdfDay.format(d);
		String text = "";
        int dayType = 999;



        try{
            holidayCursor = db.getReadableDatabase().rawQuery("SELECT year, month, day, daytype FROM schedule WHERE year = " + year + " AND month = " + month + " AND day = " + day , null);
            Log.i("Holiday definer", "SELECT year, month, day, daytype FROM schedule WHERE year = " + year + " AND month = " + month + " AND day = " + day);
            if (holidayCursor.getCount() > 0 ){
                holidayCursor.moveToFirst();
                dayType = holidayCursor.getInt(holidayCursor
                        .getColumnIndex(DatabaseHelper.DAY_TYPE));
            }

            else {
                Log.i("Holiday definer", "Problem with data base. Going to noiloj way");
                if (dayOfTheWeek.equals("Saturday")|| dayOfTheWeek.equals("Sunday")){
                    text = "_y";
                }
                else {
                    text = "";
                }
            }

            if (dayType == 0){
                text = "";
            }
            else if (dayType == 3){
                text = "_y";
            }
        }

        catch (Exception e){
            Log.i("Holiday definer", "There is an exeption. Going to noiloj way");
            if (dayOfTheWeek.equals("Saturday")|| dayOfTheWeek.equals("Sunday")){
                text = "_y";
            }
            else {
                text = "";
            }
        }


        if (holidayCursor != null){
            holidayCursor.close();
        }


		return text;
	}




	private void panelDrawer(){
        TableLayout tableLayout = new TableLayout(getActivity());
        tableLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        //pLayout.addView(tableLayout);



        firstTime = false;

		try {
			panelCursor = db.getReadableDatabase().rawQuery("SELECT number, timer, type, hour, minute FROM " + BUS_STOP_NAME_TABLE_INNER + direction + holidayInformer + " WHERE time(timer) > time('07:00:00')", null);

			Log.i("busmanager", "Check" + BUS_STOP_NAME +". Table name is"
					+ BUS_STOP_NAME_TABLE_INNER + direction + holidayInformer);
			/*panelCursor = db.getReadableDatabase().rawQuery(
						"SELECT number, timer, type, hour, minute FROM "
								+ BUS_STOP_NAME_TABLE_INNER + direction + holidayInformer
								+ " WHERE time(timer) > time('07:00:00')" + fif
						, null);*/


			int recondCount;
			recondCount = panelCursor.getCount();

			if (recondCount > 0) {

				panelCursor.moveToFirst();

			}
		}
		finally{

		}


		int busMin;
		int busHour;
		int busNumber;

		int hour_checker = 1;


		if (panelCursor.getCount() > 0){




			for (int i = 0; i < panelCursor.getCount(); i++){
                busMin = panelCursor.getInt(panelCursor
                        .getColumnIndex(DatabaseHelper.MINUTE));
                busHour = panelCursor.getInt(panelCursor
                        .getColumnIndex(DatabaseHelper.HOUR));
                busNumber = panelCursor.getInt(panelCursor
                        .getColumnIndex(DatabaseHelper.NUMBER));

                // single row concentrated option?!


				if (hour_checker == 1 || hour_checker < busHour ){


                    TableRow lineRow = new TableRow(getActivity());
                    tLayout.addView(lineRow);

                    TextView whiteText = new TextView(getActivity());
                    whiteText.setText("   ");
                    whiteText.setTextSize(1);

                    lineRow.addView(whiteText);

                    for (int r = 0; r<9; r++){
                        TextView emptyText = new TextView(getActivity());
                        emptyText.setText("   ");
                        emptyText.setTextSize(1);
                        emptyText.setBackgroundColor(getResources().getColor(R.color.Black));
                        lineRow.addView(emptyText);
                    }


                    TableRow tableRow = new TableRow(getActivity());
                    tableRow.setId(busHour);
                    tLayout.addView(tableRow);
                    TextView txtBigTime = new TextView(getActivity());
                    txtBigTime.setText(String.format("%02d ",busHour));

                    txtBigTime.setTextSize(18);

                    tableRow.addView(txtBigTime);
					TextView textView = new TextView(getActivity());

					//Size and color and the format of the text
					textView.setText(String.format("%02d ",busMin));
					textView.setTextSize(14);
					busColorer(busNumber, textView);
                    tableRow.addView(textView);

                    if (hour_checker == 1){
                        hour_checker = 7;
                    }
                    else {
                        hour_checker = busHour;
                    }
                    Log.i("Main part", "Bus hour" + busHour);
                    Log.i("Main part", "Bus minute" + busMin);
                    if ((busHour & 1) == 0){
                        if (holidayInformer.equals("_d")){

                        }
                        tableRow.setBackgroundColor(getResources().getColor(R.color.PanelBackgroundTrans));
                        //textView.setBackgroundColor(getResources().getColor(R.color.PanelBackgroundTrans));
                    }

				}
				else {

                    int p = tLayout.getChildCount();
                    TableRow tableRow = (TableRow)tLayout.getChildAt(p-1);
                    TextView textView = new TextView(getActivity());

                    //Size and color and the format of the text
                    textView.setTextSize(14);
                    busColorer(busNumber, textView);
                    textView.setText(String.format("%02d ",busMin));
                    Log.i("Else part", "Bus minute" + busMin);

					//New row after the 10th record
					if (tableRow.getChildCount()== 10){
						TableRow tableRowTwo = new TableRow(getActivity());
                        tLayout.addView(tableRowTwo);
                        TextView freeText = new TextView(getActivity());
                        tableRowTwo.addView(freeText);
						tableRowTwo.addView(textView);

					}
					else {
						tableRow.addView(textView);
					}
                    if ((busHour & 1) == 0){
                        tableRow.setBackgroundColor(getResources().getColor(R.color.PanelBackgroundTrans));
                        //textView.setBackgroundColor(getResources().getColor(R.color.PanelBackgroundTrans));
                    }

				}

            panelCursor.moveToNext();

			}


		}




	}


	public void busColorer(int number, TextView text){

		if (number == 50){
			text.setTextColor(getResources().getColor(R.color.BlaBlack));
		}
		else if (number == 51){
			text.setTextColor(getResources().getColor(R.color.Red));
		}
		else if (number == 52){
			text.setTextColor(getResources().getColor(R.color.Orange));
		}
		else if (number == 53){
			text.setTextColor(getResources().getColor(R.color.Green));
		}
		else if (number == 54){
			text.setTextColor(getResources().getColor(R.color.Green));
		}
		else if (number == 55){
			text.setTextColor(getResources().getColor(R.color.Blue));
		}

	}



	@Override
	public void onDestroy() {


		super.onDestroy();


	}

public boolean slidercha(){
	if (mLayout != null &&
			(mLayout.getPanelState() == PanelState.EXPANDED || mLayout.getPanelState() == PanelState.ANCHORED)) {
		mLayout.setPanelState(PanelState.COLLAPSED);
		return false;
	} else {
		return true;
	}
}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {


            case R.id.MEkiIki:
                if(isChecked){
                directionUI = "DownTown";
                direction = "_b";
                if (BUS_STOP_NAME_TABLE_INNER != "nam") {
                    cleanUI("");
                    txtWarn.setText("");
                    queryTesta();
                    tLayout.removeAllViews();
                    panelDrawer();
                }
                }

                break;

            case R.id.MAPUIki:

                if (isChecked) {


                directionUI = "APU";
                direction = "";
                if (BUS_STOP_NAME_TABLE_INNER != "nam") {
                    cleanUI("");
                    txtWarn.setText("");
                    queryTesta();
                    if (firstTime == true) {
                        panelDrawer();
                    } else {
                        tLayout.removeAllViews();
                        panelDrawer();
                    }
                }}
                break;



        }
	}

	//Direction Switch
	void DirectionSwitch(String mDirection){
		if (getView() != null){
			if (mDirection.equals("")){
				EkiIki.setChecked(false);
				APUIki.setChecked(true);
			}
			else if (mDirection.equals("_b")){
				APUIki.setChecked(false);
				EkiIki.setChecked(true);
			}
			else {
				EkiIki.setChecked(false);
				APUIki.setChecked(true);
			}

		}
		else {

		}


	}
}