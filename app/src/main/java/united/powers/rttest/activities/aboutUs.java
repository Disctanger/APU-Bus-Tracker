package united.powers.rttest.activities;

import android.app.Activity;
import android.os.Bundle;

import united.powers.rttest.R;


public class aboutUs extends Activity {




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.about_us);

        getActionBar().setDisplayHomeAsUpEnabled(true);


        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            //runFragment(extra,stopName );
        }


    }

}
