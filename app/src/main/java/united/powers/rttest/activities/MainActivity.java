package united.powers.rttest.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import united.powers.rttest.R;


public class MainActivity extends Activity {

    private static final int SPLASH_DISPLAY_TIME = 500; // splash screen delay time in milliseconds I think

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       

        new Handler().postDelayed(new Runnable() {
            public void run() {

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Slider.class);

                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();

                // transition from splash to main menu
                //overridePendingTransition(R.layout.splashfadein, R.layout.splashfadeout);

            }
        }, SPLASH_DISPLAY_TIME);
        //start();
    }
public void startTwo() {
    Intent i = new Intent(this, Slider.class);
    startActivity(i);
}

}
