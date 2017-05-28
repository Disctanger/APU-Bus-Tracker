package united.powers.rttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by Disctanger on 11/11/2015.
 */
public class DrawerListener extends BroadcastReceiver {

    public static final String DRAWER_STATE = "DRAWER_STATE";
    public static final String DRAWER_IS_OPEN = "DRAWER_IS_OPEN";
    public static final String DRAWER_IS_CLOSED = "DRAWER_IS_CLOSED";
    public static final String ACTION_DRAWER = "united.powers.rttest.ACTION_DRAWER";

    private static final String TAG = "Drawer State Listener";

    @Override
    public void onReceive(Context context, Intent intent) {
        String DrawerState = (String)intent.getStringExtra(DRAWER_STATE);
        String BUS_STOP_TABLE = (String)intent.getStringExtra(LocationService.BUS_STOP_TABLE);
        Location loc = (Location)intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);

            onDrawerStateReceived(context, DrawerState);




        // if we get here, something else has happened
        if (intent.hasExtra(LocationManager.KEY_PROVIDER_ENABLED)) {
            boolean enabled = intent.getBooleanExtra(LocationManager.KEY_PROVIDER_ENABLED, false);

        }
    }

    protected void onDrawerStateReceived(Context context, String DrawerState) {
        Log.d(TAG, this + " Drawer is opened");
    }





}
