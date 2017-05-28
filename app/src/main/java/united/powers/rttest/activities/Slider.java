package united.powers.rttest.activities;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import united.powers.rttest.activities.fragments.CpanelBusStopList;
import united.powers.rttest.database.DatabaseHelper;
import united.powers.rttest.LocationReceiver;
import united.powers.rttest.LocationService;
import united.powers.rttest.R;



public class Slider extends BaseActivity implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    //ListView stop_lister ;

    private TextView txtBusNumber, txtType, txtBGStop, txtTime, txtStop, txtTimeLeft, txtTestLoc, txtWarn, txtYasumiNote;
    private Location mLastLocation;


    private static final String fontPath = "fonts/bb.ttf";

    private String holidayInformer = yasumiCalc();
    public static final String FragmentTag = "AboutUs";
    private String BUS_STOP_NAME = " ";
    private String BUS_STOP_NAME_TABLE_INNER = "nam";

    private String type = " ";
    private String busNumber = " ";
    private String busTimeHour = " ";
    private String busType = " ";
    private String direction = " ";
    private String directionUI = "APU";
    private TableLayout tLayout;
    private CharSequence mTitle;
    private String fif = "";
    private String freeName;
    private String freeTable;
    private String nextStop = "";
    private String nextStop2 = "";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private String[] arraName;
    private String[] arraName_f;
    private String[] arraStop;
    private String[] arraStop_f;
    private String[] arra_full_stops;
    private String[] arra_full_tables;


    private int busStopId;
    private int busNameId;
    private int namba;
    private int i = 0;
    private int positionOFF;

    private CompoundButton cb, EkiIki, APUIki;

    private Cursor panelCursor;


    private Button btnBackTime, btnNext, btnPrev, btnForward, btnReset ;



    private Spinner sp_number, sp_to;

    private boolean info = false;
    private boolean chik = false;
    private boolean auto_mode = true;
    private boolean testMode = false;


    private Cursor cursor = null;

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

    private BroadcastReceiver mLocationReceiver = new LocationReceiver() {

        @Override
        protected void onLocationReceived(Context context, Location loc,
                                          String BUS_STOP, String BUS_STOP_TABLE) {

            BUS_STOP_NAME = BUS_STOP;
            if (!BUS_STOP_NAME_TABLE_INNER.equals(BUS_STOP_TABLE)) {
                BUS_STOP_NAME_TABLE_INNER = BUS_STOP_TABLE;
                queryTesta();
                info = true;
                auto_mode = true;

                if (BUS_STOP_NAME != null ) {

                    if (BUS_STOP_NAME.equals("Ritsumeikan Asia Pacific University") ||
                            BUS_STOP_NAME.equals("Mori Kosaten") ||
                            BUS_STOP_NAME.equals("Mori Iriguchi") ||
                            BUS_STOP_NAME.equals("APU House Mae")) {
                        DirectionSwitch("_b");

                    }
                                    }

            }

            if (testMode){
                txtTestLoc.setText(loc.getLatitude() + " " + loc.getLongitude());
            }

        }

    };

    private void checkFirstTimePanelUse (boolean checker){
        
        if (checker == true) {
            //This checks whether panel open
            panelDrawer();
        } else {
            //if open then removes all info and then inserts new data ne
            tLayout.removeAllViews();
            panelDrawer();
        }
    }
    
    private void populatePanelDrawerData (boolean checker) {

    }

    private void populatePanelDrawerData () {
        if (!BUS_STOP_NAME_TABLE_INNER.equals("nam")) {
            upUI("");
            txtWarn.setText("");
            queryTesta();
            checkFirstTimePanelUse(panelHasNoData());
        }
    }

    private void clearWarningInfo (){

        txtWarn.setText("");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()){
            case R.id.APUIki:
                if (isChecked) {
                    directionUI = "APU";
                    direction = "";
                    populatePanelDrawerData();
                }
                break;
            case R.id.EkiIki:
                if (isChecked) {
                    directionUI = "DownTown";
                    direction = "_b";
                    if (!BUS_STOP_NAME_TABLE_INNER.equals("nam")) {
                        upUI("");
                        clearWarningInfo();

                        queryTesta();
                        tLayout.removeAllViews();
                        panelDrawer();
                    }
                }
                break;

            case R.id.cb_AOn:
                //checkButton for turning on GPS and search

                if (isChecked) {
                    CheckIt(buttonView);
                    upUI("");
                    auto_mode = true;
                    timerHandler.postDelayed(timerRunnable, 0);
                    Log.i("","Checked");
                } else {
                    StopIt(buttonView);
                    upUI("");

                }
                break;
        }

    }

    public void CheckIt(View v){
        Intent e=new Intent(this, LocationService.class);
        startService(e);
    };

    public void StopIt(View v){
        Intent e=new Intent(this, LocationService.class);
        stopService(e);
    }

    ;

    private SlidingUpPanelLayout mLayout;
    private static final String TAG = "DemoActivity";


    public Slider() {
        super(R.string.title_activity_slider);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db=new DatabaseHelper(this);

        getSlidingMenu().setMode(SlidingMenu.LEFT_RIGHT);
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        setContentView(R.layout.activity_navigation_drawer);


        getSlidingMenu().setSecondaryMenu(R.layout.menu_frame_two);
        getSlidingMenu().setSecondaryShadowDrawable(R.drawable.shadowright);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame_two, new CpanelBusStopList())
                .commit();

        //Layout ID`s

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelExpanded(View panel) {
                Log.i(TAG, "onPanelExpanded");

            }

            @Override
            public void onPanelCollapsed(View panel) {
                Log.i(TAG, "onPanelCollapsed");

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

        txtBusNumber = ((TextView) findViewById(R.id.txtABusNumber));
        txtTime = ((TextView) findViewById(R.id.txtAComeTime));
        txtBGStop = ((TextView)findViewById(R.id.txtABGStop));
        txtType = ((TextView)findViewById(R.id.txtAType));
        txtWarn = ((TextView)findViewById(R.id.txtAWarn));
        txtTestLoc = ((TextView)findViewById(R.id.txtTestLoc));
        txtYasumiNote = ((TextView) findViewById(R.id.txtYasumiNote));
        //stop_lister = (ListView)findViewById(R.id.stop_lister);

        tLayout = ((TableLayout) findViewById(R.id.panelTableLayoutAuto));

        txtTimeLeft = ((TextView)findViewById(R.id.txtATimeLeft));

        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        txtTimeLeft.setTypeface(tf);

        txtStop = ((TextView)findViewById(R.id.txtABusStop));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_one);




       // btnPrev = ((Button)findViewById(R.id.btnAPrev));
        btnBackTime = ((Button)findViewById(R.id.btnABackTime));
        btnNext = ((Button)findViewById(R.id.btnANext));
       // btnForward = ((Button)findViewById(R.id.btnAForward));
        //btnReset = ((Button)findViewById(R.id.btnAReset));

        cb = (CompoundButton) findViewById(R.id.cb_AOn);
        APUIki = (CompoundButton) findViewById(R.id.APUIki);
        EkiIki = (CompoundButton) findViewById(R.id.EkiIki);



        sp_number = ((Spinner)findViewById(R.id.sp_ANumber));
        //sp_to =     ((Spinner)findViewById(R.id.sp_ATo));


        sp_number.setOnItemSelectedListener(this);
        //sp_to    .setOnItemSelectedListener(this);
        cb       .setOnCheckedChangeListener(this);
        APUIki   .setOnCheckedChangeListener(this);
        EkiIki   .setOnCheckedChangeListener(this);


        arraStop = getResources().getStringArray(R.array.bus_stop_tables);
        arraName = getResources().getStringArray(R.array.bus_stops);
        arraName_f = getResources().getStringArray(R.array.bus_stops_f);
        arraStop_f = getResources().getStringArray(R.array.bus_stop_tables_f);
        arra_full_stops = getResources().getStringArray(R.array.bus_stops_full);
        arra_full_tables = getResources().getStringArray(R.array.bus_stop_tables_full);

        ArrayAdapter<CharSequence> adapterNumbers = ArrayAdapter
                .createFromResource(this, R.array.bus_numbers,
                        android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> adapterTypes = ArrayAdapter
                .createFromResource(this, R.array.bus_types,
                        android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> adapterDirection = ArrayAdapter
                .createFromResource(this, R.array.bus_direction,
                        android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> adapterFullList = ArrayAdapter
                .createFromResource(this, R.array.bus_stops_full,
                        android.R.layout.simple_spinner_item);

        //ArrayAdapter<String> stop_lister_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1, arra_full_tables );

        adapterNumbers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterDirection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterFullList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_number.setAdapter(adapterNumbers);
        //sp_to.setAdapter(adapterDirection);
        //stop_lister.setAdapter(stop_lister_adapter);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cursor != null) {
                    //queryTesta();
                    if (cursor.getPosition() >= cursor.getCount() - 1) {
                        //txtWarn.setText();
                        upUI( "This is the last bus");
                    } else {
                        cursor.moveToNext();
                        assignBusInfo(cursor);

                        upUI("");
                    }


                }

            }
        });


        btnBackTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cursor != null && cursor.moveToFirst()) {

                    cursor.moveToFirst();
                    assignBusInfo(cursor);


                    upUI("");

                }

            }
        });
        APUIki.setChecked(true);
        btnBackTime.setEnabled(false);
        btnNext.setEnabled(false);


       // getSupportActionBar().setTitle("Bus Hunter");

        registerReceiver(mLocationReceiver,
                new IntentFilter(LocationService.ACTION_LOCATION));



        getActionBar().setHomeButtonEnabled(true);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        }







    }

    public void assignBusInfo(Cursor cursor){
        busTimeHour = cursor.getString(cursor
                .getColumnIndex(DatabaseHelper.TIME));
        busNumber = cursor.getString(cursor
                .getColumnIndex(DatabaseHelper.NUMBER));
        busType = cursor.getString(cursor
                .getColumnIndex(DatabaseHelper.TYPE));
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view

        return super.onPrepareOptionsMenu(menu);
    }




    private void upUI( String warnMessage){

        if (!info){
            txtBusNumber.setText("--");
            txtType.setText("");
            txtBGStop.setText("---");
            txtTime.setText("--:--:--");
            txtStop.setText("Bus Stop: Retrieving GPS location");
            txtTimeLeft.setText("--:--:--");

            btnBackTime.setEnabled(false);
            btnNext.setEnabled(false);

        }

        if (info){
            txtBusNumber.setText(busNumber);
            txtType.setText("");
            if(APUIki.isChecked()){
                txtBGStop.setText("APU");
            }
            else {
                txtBGStop.setText("Downtown");
            }
            txtTime.setText(busTimeHour);
            if (auto_mode) {
                txtStop.setText("Bus Stop: " + BUS_STOP_NAME);
            } else {
                txtStop.setText("Bus Stop: " + freeName);
            }
            txtTimeLeft.setText(timeLeft());
            btnBackTime.setEnabled(true);
            btnNext.setEnabled(true);
        }



        txtWarn.setTextColor(getResources().getColor(R.color.Red));
        txtWarn.setText(warnMessage);

    }

    public void disableButton(Button button){
        button.setEnabled(false);
    }

    public void enableButton(Button button){
        button.setEnabled(true);
    }

    public void updateUI() {

        if (holidayInformer.equals("_y")){
            txtYasumiNote.setTextColor(getResources().getColor(R.color.Red));
            txtYasumiNote.setText("Non-school day");
        }
        else {
            txtYasumiNote.setTextColor(getResources().getColor(R.color.Green));
            txtYasumiNote.setText("School day");

        }

        if (!info){
            txtBusNumber.setText("--");
            txtType.setText("");
            txtBGStop.setText("---");
            txtTime.setText("--:--:--");
            txtStop.setText("Bus Stop: Retrieving GPS location");
            txtTimeLeft.setText("--:--:--");

        }

        if (info){
            txtBusNumber.setText(busNumber);
            txtType.setText("");
            if(APUIki.isChecked()){
                txtBGStop.setText("APU");
            }
            else {
                txtBGStop.setText("Downtown");
            }
            txtTime.setText(busTimeHour);
            if (auto_mode) {
                txtStop.setText("Bus Stop: " + BUS_STOP_NAME);
            } else {
                txtStop.setText("Bus Stop: " + freeName);
            }
            txtTimeLeft.setText(timeLeft());

            if (panelHasNoData()){
                panelDrawer();
            }

        }



    }

    private void cleanUI(String warning) {
        txtBusNumber.setText("--");
        txtType.setText("----");
        txtBGStop.setText("---");
        txtTime.setText("--:--:--");
        txtStop.setText("Bus Stop: Not defined");
        txtTimeLeft.setText("--:--:--");

    }


    private void queryTesta() {

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        String day = "";

        if (dayOfTheWeek.equals("Saturday")|| dayOfTheWeek.equals("Sunday")){
            day = "_y";
        }
        else {
            day = "";
        }



        if (BUS_STOP_NAME_TABLE_INNER.equals(DatabaseHelper.BUS_STOPS)){
            cleanUI("You are out of coverage area");
        }

        else  {


            try {



                Calendar cal = Calendar.getInstance();

                int hourofday = cal.get(Calendar.HOUR_OF_DAY);

                long millis = System.currentTimeMillis();

                if (auto_mode == true){
                    cursor = db.getReadableDatabase().rawQuery("SELECT number, timer, type, hour, minute FROM " + BUS_STOP_NAME_TABLE_INNER+ direction+ day +" WHERE time(timer) > time('" + String.format("%02d:%02d:%02d",
                            hourofday,
                            TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                            TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1))+ "')" + type + fif , null);

                }

                if (auto_mode == false){
                    cursor = db.getReadableDatabase().rawQuery("SELECT number, timer, type, hour, minute FROM " + freeTable +                direction+ day +" WHERE time(timer) > time('" + String.format("%02d:%02d:%02d",
                            hourofday,
                            TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                            TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1))+ "')" + type + fif , null);
                }

                if (cursor.getCount() <= 0) {

                    if (auto_mode == true){
                        cursor = db.getReadableDatabase().rawQuery(
                                "SELECT number, timer, type, hour, minute FROM "
                                        + BUS_STOP_NAME_TABLE_INNER + direction + day
                                        + " WHERE time(timer) > time('07:00:00')"
                                        + type + fif, null);
                    }

                    if (auto_mode == false){
                        cursor = db.getReadableDatabase().rawQuery(
                                "SELECT number, timer, type, hour, minute FROM "
                                        + freeTable + direction + day
                                        + " WHERE time(timer) > time('07:00:00')"
                                        + type + fif, null);
                    }


                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        assignBusInfo(cursor);

                        //upUI("");
                        upUI("");
                        clearWarningInfo();
                        info = true;
                    }
                    if (cursor.getCount() <= 0) {
                        info = false;
                        upUI( "There is no such bus");
                        cleanUI("There is no such bus");

                        chik = true;
                    }
                }

                if (cursor.getCount() > 0) {

                    cursor.moveToFirst();
                    assignBusInfo(cursor);



                    Log.i("Day Manager", day);
                    info = true;
                    upUI("");


                }

            } finally {

                if (info == true){

                }

                if (info == false){
                    busTimeHour = "";
                    busNumber = "";
                    busType = "";
                }



            }

        }

    }

    public void restart(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void runAboutUs(){

        Intent intent = new Intent();
        intent.setClass(this, aboutUs.class);

        this.startActivity(intent);

        getSlidingMenu().toggle();
        Log.i("Runner", "implementing");
    }

    public void runCalculator(){

        Intent intent = new Intent();
        intent.setClass(this, Calculator.class);

        this.startActivity(intent);

        getSlidingMenu().toggle();
        Log.i("Runner", "implementing");
    }


    public void startListing(int position, String stopName){
        Intent i = new Intent(this, ManualActivity.class);
        i.putExtra(ManualActivity.EXTRA_ACTIVITY_MESSAGE, position);
        i.putExtra(ManualActivity.EXTRA_STOP_NAME, stopName);

        startActivity(i);
        getSlidingMenu().showContent();
    }

    public void detachNe(){
        Fragment fragment = getFragmentManager().findFragmentByTag(Slider.FragmentTag);

        if (fragment != null){
            getFragmentManager().beginTransaction().remove(fragment).commit();

        }
        getSlidingMenu().toggle();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {

        switch (parent.getId()) {
            case R.id.sp_ANumber:
                switch (position) {
                    case 0:
                        fif = " ";
                        if (BUS_STOP_NAME_TABLE_INNER != "nam") {
                            clearWarningInfo();

                            upUI("");

                            queryTesta();

                        }
                        break;
                    case 1:
                        fif = " AND number = 50 ";
                        if (BUS_STOP_NAME_TABLE_INNER != "nam") {
                            upUI("");
                            clearWarningInfo();

                            queryTesta();

                        }
                        break;
                    case 2:
                        fif = " AND number = 51 ";
                        if (BUS_STOP_NAME_TABLE_INNER != "nam") {
                            upUI("");
                            clearWarningInfo();
                            queryTesta();

                        }
                        break;
                    case 3:
                        fif = " AND number = 52 ";
                        if (BUS_STOP_NAME_TABLE_INNER != "nam") {
                            clearWarningInfo();
                            upUI("");
                            queryTesta();

                        }
                        break;
                    case 4:
                        fif = " AND number = 53 ";
                        if (BUS_STOP_NAME_TABLE_INNER != "nam") {
                            upUI("");
                            clearWarningInfo();
                            queryTesta();

                        }
                        break;
                    case 5:
                        fif = " AND number = 54 ";
                        if (BUS_STOP_NAME_TABLE_INNER != "nam") {
                            upUI("");
                            clearWarningInfo();
                            queryTesta();

                        }
                        break;
                    case 6:
                        fif = " AND number = 55 ";
                        if (BUS_STOP_NAME_TABLE_INNER != "nam") {
                            upUI("");
                            clearWarningInfo();
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

                time2 = String.format(Locale.getDefault(),"%02d:%02d:%02d", (int) diffHours ,	minutes = (int) diffMinutes, seconds = (int) diffSeconds );


                taymu = diff;

            }



        } catch (Exception e) {
            e.printStackTrace();
        }



        return time2;
    }

    private int stopId() {

        int position;

        if (sp_number.getSelectedItemPosition() == 2){
            position = Arrays.asList(arraName_f).indexOf(BUS_STOP_NAME_TABLE_INNER);
        }
        else {
            position = Arrays.asList(arraStop).indexOf(BUS_STOP_NAME_TABLE_INNER);
        }
        return position;

    }

    private int stopNameId() {
        int position;


        if (sp_number.getSelectedItemPosition() == 2){
            position = Arrays.asList(arraStop_f).indexOf(BUS_STOP_NAME);
        }
        else {
            position = Arrays.asList(arraName).indexOf(BUS_STOP_NAME);
        }

        return position;

    }

    private void increment(int stop, int name) {

        if (sp_number.getSelectedItemPosition() == 2) {

            if (stop == arraStop_f.length-1) {
                stop = 0;
            } else {
                stop++;
            }

            if (name == arraName_f.length-1) {
                name = 0;
            } else {
                name++;
            }

            busStopId = stop;
            busNameId = name;

        } else {
            Log.i("crementer", "In");
            if (stop == arraStop.length-1) {
                stop = 0;
            } else {
                stop++;
            }

            if (name == arraName.length-1) {
                name = 0;
            } else {
                name++;
            }

            busStopId = stop;
            busNameId = name;
        }
    }

    private void decrement(int stop, int name) {

        int position = sp_number.getSelectedItemPosition();

        switch (position){
            case 2:
                Log.i("crementer", "In 2");
                if (stop == 0) {
                    stop = arraStop_f.length-1;
                } else {
                    stop--;
                }

                if (name == 0) {
                    name = arraName_f.length-1;
                } else {
                    name--;
                }
                busStopId = stop;
                busNameId = name;
                break;
            default:

                Log.i("crementer", "In def" + stop + name);
                if (stop == 0) {
                    stop = arraStop.length-1;
                } else {
                    stop--;
                }

                if (name == 0) {
                    name = arraName.length-1;
                }  else {
                    name--;
                }
                busStopId = stop;
                busNameId = name;
                break;

        }




    }

    @Override
    public void onResume() {
        super.onResume();

        getSlidingMenu().showContent();
        registerReceiver(mLocationReceiver,
                new IntentFilter(LocationService.ACTION_LOCATION));
        timerHandler.postDelayed(timerRunnable, 0);
        if (BUS_STOP_NAME_TABLE_INNER != "nam") {

            queryTesta();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
        //unregisterReceiver(mLocationReceiver);
        Intent e=new Intent(this, LocationService.class);
        stopService(e);

    }

    @Override
    public void onPause() {

        timerHandler.removeCallbacks(timerRunnable);
        unregisterReceiver(mLocationReceiver);
        super.onPause();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                toggle();

                return true;

            case android.R.id.title:
                toggle();

                return true;


            case R.id.action_settings:

                getSlidingMenu().showSecondaryMenu();
                break;

            case R.id.rightList:

                getSlidingMenu().showSecondaryMenu();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == PanelState.EXPANDED || mLayout.getPanelState() == PanelState.ANCHORED)) {
            mLayout.setPanelState(PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    private void panelDrawer(){

        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        //pLayout.addView(tableLayout);

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


                    TableRow lineRow = new TableRow(this);
                    tLayout.addView(lineRow);


                    TextView whiteText = new TextView(this);
                    whiteText.setText("   ");
                    whiteText.setTextSize(1);

                    lineRow.addView(whiteText);

                    for (int r = 0; r<9; r++){
                        TextView emptyText = new TextView(this);
                        emptyText.setText("   ");
                        emptyText.setTextSize(1);
                        emptyText.setBackgroundColor(getResources().getColor(R.color.Black));
                        lineRow.addView(emptyText);
                    }


                    TableRow tableRow = new TableRow(this);
                    tableRow.setId(busHour);
                    tLayout.addView(tableRow);
                    TextView txtBigTime = new TextView(this);
                    txtBigTime.setText(String.format("%02d ",busHour));

                    txtBigTime.setTextSize(18);

                    tableRow.addView(txtBigTime);
                    TextView textView = new TextView(this);

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
                    TextView textView = new TextView(this);

                    //Size and color and the format of the text
                    textView.setTextSize(14);
                    busColorer(busNumber, textView);
                    textView.setText(String.format("%02d ",busMin));
                    Log.i("Else part", "Bus minute" + busMin);

                    //New row after the 10th record
                    if (tableRow.getChildCount()== 10){
                        TableRow tableRowTwo = new TableRow(this);
                        tLayout.addView(tableRowTwo);
                        TextView freeText = new TextView(this);
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

    public boolean panelHasNoData (){
        //have to replace all first time use
        //checks the child of the tlayout
        int layoutChild = tLayout.getChildCount();

        if (layoutChild > 0){
            return false;
        }
        else {
            return true;
        }


    };

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

    //Direction Switch
    void
    DirectionSwitch(String mDirection){

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

}
