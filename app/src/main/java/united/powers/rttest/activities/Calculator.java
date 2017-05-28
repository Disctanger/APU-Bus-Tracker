package united.powers.rttest.activities;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import united.powers.rttest.R;


public class Calculator extends Activity {


    TextView baitoStartTimeLabel, txtWorkHours, txtWageOneDay, txtWageAllDays, workLength;
    TextView dateAndTimeLabel;
    EditText etWage, etDays, overTen;
    TextView result;
    Calendar dateAndTime = Calendar.getInstance();
    Calendar startTime = Calendar.getInstance();
    static final int wage = 750;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.baito_calculator);

        getActionBar().setDisplayHomeAsUpEnabled(true);


        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            //runFragment(extra,stopName );
        }
        dateAndTimeLabel=(TextView)findViewById(R.id.txtclEndTime);
        baitoStartTimeLabel=(TextView)findViewById(R.id.txtclStartTime);
        txtWorkHours=(TextView)findViewById(R.id.txtWorkHours);
        txtWageOneDay=(TextView)findViewById(R.id.txtWageOneDay);
        txtWageAllDays=(TextView)findViewById(R.id.txtWageAllDays);
        workLength = (TextView)findViewById(R.id.txtWorkLength);

        etWage=(EditText)findViewById(R.id.editText3);
        etDays=(EditText)findViewById(R.id.editText4);
        overTen= (EditText)findViewById(R.id.overTen);
        result=(TextView)findViewById(R.id.result);
        //updateLabel();
       // updateStartLabel();


    }

   public void chooseTime(View v) {
        new TimePickerDialog(this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE),
                true)
                .show();
    }

    private Calendar filter (Calendar calendar){
        calendar.get(Calendar.MINUTE);

        int unroundedMinutes = calendar.get(Calendar.MINUTE);
        int mod = unroundedMinutes % 15;
        int toadd = 0;
        if (mod > 0 ){
              toadd = 15-mod;
        }
        else {}

        calendar.add(Calendar.MINUTE, toadd);
        return calendar;
    }

    private Calendar endFilter (Calendar calendar){
        calendar.get(Calendar.MINUTE);

        int unroundedMinutes = calendar.get(Calendar.MINUTE);
        int mod = unroundedMinutes % 15;
        int toadd = 0;
        if (mod < 15 ){
            toadd = -mod;
        }
        else {

        }

        calendar.add(Calendar.MINUTE, toadd);
        return calendar;
    }

    public void chooseStartTime(View v) {
        new TimePickerDialog(this, start,
                startTime.get(Calendar.HOUR_OF_DAY),
                startTime.get(Calendar.MINUTE),
                true)
                .show();
    }
    private void updateLabel() {
        dateAndTimeLabel
                .setText(DateUtils
                        .formatDateTime(this,
                                dateAndTime.getTimeInMillis(),
                                DateUtils.FORMAT_SHOW_TIME));
    }



    private void updateStartLabel() {
        baitoStartTimeLabel
                .setText(DateUtils
                        .formatDateTime(this,
                                startTime.getTimeInMillis(),
                                DateUtils.FORMAT_SHOW_TIME));
    }


    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay,
                              int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            updateLabel();

        }
    };

    TimePickerDialog.OnTimeSetListener start=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay,
                              int minute) {
            startTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            startTime.set(Calendar.MINUTE, minute);
            updateStartLabel();
        }
    };

    public void calculateIt(View v){

        int mWage = parseInteger(etWage);
        int mDays = parseInteger(etDays);
        int mOverTime = parseInteger(overTen);
        startTime.set(Calendar.SECOND, 0);
        dateAndTime.set(Calendar.SECOND, 0);


        long start = filter(startTime).getTimeInMillis();
        long end = endFilter(dateAndTime).getTimeInMillis();



        long difference = end - start ;

        int minutes = (int) (difference / (1000*60)% 60);
        int hours = (int) (difference / (1000*60*60)% 24);

        txtWorkHours.setText("Work hours: " + DateUtils
                .formatDateTime(this,
                        filter(startTime).getTimeInMillis(),
                        DateUtils.FORMAT_SHOW_TIME) + " - " + DateUtils
                .formatDateTime(this,
                        endFilter(dateAndTime).getTimeInMillis(),
                        DateUtils.FORMAT_SHOW_TIME) );

        workLength.setText("Work length: "+hours + ":" + minutes );

        txtWageOneDay.setText("One day wage: " + (((hours + roundWageMinute(minutes)) * mWage)+ (getOverTime(end)* (mOverTime-mWage)))+ "(" + (getOverTime(end)* (mOverTime-mWage))+ " yen is over 10 PM)");
        txtWageAllDays.setText("Total: " + ((((hours + roundWageMinute(minutes)) * mWage)+ (getOverTime(end)* (mOverTime-mWage))) * mDays));



    }

    private double getOverTime(long finishTime){
        double result;
        double testResult = 0;

        int minutes = (int) (finishTime / (1000*60)% 60);
        int hours = (int) (finishTime / (1000*60*60)% 24);
        hours = hours + 9;

        Log.i("IN1 to " + hours, testResult + " hours");

        if (hours >= 22){

            testResult = hours - 22;
            testResult = testResult + roundWageMinute(minutes);

        }else {
            testResult = 0;
        }
        Log.i("IN to " + hours, testResult + " hours");


        result = testResult;

        return result;
    }

    private int parseInteger(TextView textView){
        int result;

        if (textView.getText().toString().equals("")){
            result = 0;
        }
        else {
            result = Integer.parseInt(textView.getText().toString());
        }

        return result;
    }

    private double roundWageMinute (double minute) {

        if (minute  > 0  && minute <= 15 ){
            minute = 0.25;
        }
        else if (minute > 15 && minute <= 30){
            minute = 0.5;
        }
        else if (minute > 30 && minute <= 45){
            minute = 0.75;
        }
        else if (minute > 45 && minute <= 59){
            minute = 1;
        }



        return minute;
    }

    }