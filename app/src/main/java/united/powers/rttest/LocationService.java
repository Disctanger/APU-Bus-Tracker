package united.powers.rttest;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import java.util.HashMap;

import united.powers.rttest.database.DatabaseHelper;


public class LocationService extends Service implements  LocationListener {

  private LocationManager mgr=null;
  private Location lastLocation=null;

  public static final String ACTION_LOCATION = "united.powers.rttest.ACTION_LOCATION";
  public static final String CURRTENT_BUS_STOP = "Current Bus Stop";
  public static final String BUS_STOP_TABLE = "Bus Stop Table";

	HashMap<String, String> HashOfNames = new HashMap<>();
	HashMap<String, String> HashOfOffNames = new HashMap<>();

	Point	station	=	new	Point	(	DatabaseHelper.STATION	,	33.279133	,	131.500275	)	;
	Point	honmachi	=	new	Point	(	DatabaseHelper.HONMACHI	,	33.279324	,	131.501887	)	;
	Point	kitahama	=	new	Point	(	DatabaseHelper.KITAHAMA	,	33.279537	,	131.505524	)	;
	Point	towermae	=	new	Point	(	DatabaseHelper.TOWERMAE	,	33.282207	,	131.505338	)	;
	Point	matogahama	=	new	Point	(	DatabaseHelper.MATOGAHAMA	,	33.284671	,	131.504584	)	;
	Point	kyomachi	=	new	Point	(	DatabaseHelper.KYOMACHI	,	33.287972	,	131.503959	)	;
	Point	mochigahama	=	new	Point	(	DatabaseHelper.MOCHIGAHAMA	,	33.292441	,	131.502807	)	;
	Point	yubinkyoku	=	new	Point	(	DatabaseHelper.YUBINKYOKU	,	33.295158	,	131.502115	)	;
	Point	kotsucenter	=	new	Point	(	DatabaseHelper.KOTSUCENTER	,	33.298215	,	131.503122	)	;
	Point	daisan	=	new	Point	(	DatabaseHelper.DAISANFUTO	,	33.300907	,	131.502944	)	;
	Point	minamisuga	=	new	Point	(	DatabaseHelper.MINAMISUGA	,	33.302932	,	131.502009	)	;
	Point	harukigawa	=	new	Point	(	DatabaseHelper.HARUKIGAWA	,	33.306573	,	131.501957	)	;
	Point	fukamachi	=	new	Point	(	DatabaseHelper.FUKAMACHI	,	33.309081	,	131.501679	)	;
	Point	rokushyoyen	=	new	Point	(	DatabaseHelper.ROKUSHOYEN	,	33.313033	,	131.500731	)	;
	Point	shoningahama	=	new	Point	(	DatabaseHelper.SHONINGAHAMA	,	33.31462	,	131.500362	)	;
	Point	baibasu	=	new	Point	(	DatabaseHelper.BAIBASU	,	33.316753	,	131.499841	)	;
	Point	shohaen	=	new	Point	(	DatabaseHelper.SHOHAEN	,	33.320686	,	131.4983	)	;
	Point	benten	=	new	Point	(	DatabaseHelper.BENTEN	,	33.324776	,	131.496005	)	;
	Point	kiyosen	=	new	Point	(	DatabaseHelper.KIYOSEN	,	33.326037	,	131.495332	)	;
	Point	shinkawa	=	new	Point	(	DatabaseHelper.SHINKAWA	,	33.329201	,	131.493738	)	;
	Point	kamegawa	=	new	Point	(	DatabaseHelper.KAMEGAWA	,	33.331744	,	131.493339	)	;
	Point	iriebashi	=	new	Point	(	DatabaseHelper.IRIEBASHI	,	33.33483	,	131.493614	)	;
	Point	furuichi	=	new	Point	(	DatabaseHelper.FURUICHI	,	33.33871	,	131.494261	)	;
	Point	sekinoe	=	new	Point	(	DatabaseHelper.SEKINOE	,	33.342495	,	131.494285	)	;
	Point	kitashinden	=	new	Point	(	DatabaseHelper.KITASHINDEN	,	33.344096	,	131.494248	)	;
	Point	osaka	=	new	Point	(	DatabaseHelper.OSAKA	,	33.344984	,	131.490288	)	;
	Point	mori	=	new	Point	(	DatabaseHelper.MORI	,	33.346968	,	131.478048	)	;
	Point	kosaten	=	new	Point	(	DatabaseHelper.KOSATEN	,	33.34507	,	131.472843	)	;
	Point	house	=	new	Point	(	DatabaseHelper.HOUSE	,	33.337868	,	131.472695	)	;
	Point	apu	=	new	Point	(	DatabaseHelper.APU	,	33.336779	,	131.468409	)	;

	Point[] points = {station,
			honmachi,
			kitahama,
			towermae,
			matogahama,
			kyomachi,
			mochigahama,
			yubinkyoku,
			kotsucenter,
			daisan,
			minamisuga,
			harukigawa,
			fukamachi,
			rokushyoyen,
			shoningahama,
			baibasu,
			shohaen,
			benten,
			kiyosen,
			shinkawa,
			kamegawa,
			iriebashi,
			furuichi,
			sekinoe,
			kitashinden,
			osaka,
			mori,
			kosaten,
			house,
			apu};

	@Override
  public void onCreate() {
    super.onCreate();

    mgr=(LocationManager)getSystemService(LOCATION_SERVICE);
  	mgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,	this);
    mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

	  String[] busStopOffNames ;
	  String[] busStopNames;

	  busStopOffNames = getResources().getStringArray(R.array.bus_stops);
	  busStopNames	  = getResources().getStringArray(R.array.bus_stop_tables);

	  //enter tablename get off name
	  for (int i = 0; i<busStopNames.length; i++){
		  HashOfNames.put(busStopNames[i], busStopOffNames[i]);
	  }
	  //enter off name get table name
	  for (int i = 0; i<busStopNames.length; i++){
		  HashOfOffNames.put(busStopOffNames[i], busStopNames[i]);
	  }
  }
  

  @Override
  public void onDestroy() {
    mgr.removeUpdates(this);

    super.onDestroy();
  }

  @Override
  public IBinder onBind(Intent arg0) {
    return(null);
  }

  @Override
  public void onLocationChanged(Location loc) {

    Location bestLocation=getBestLocation(loc);

    if (bestLocation != lastLocation) {

      lastLocation=bestLocation;
    }
    
    String closestBusStop = getBestBusStop(bestLocation, points) ;
	String closestBusStopOffName = HashOfNames.get(closestBusStop);
    
    Intent broadcast = new Intent(ACTION_LOCATION);
    broadcast.putExtra(LocationManager.KEY_LOCATION_CHANGED, bestLocation);
    broadcast.putExtra(CURRTENT_BUS_STOP,closestBusStopOffName );
    broadcast.putExtra(BUS_STOP_TABLE, closestBusStop);
	  sendBroadcast(broadcast);
    
  
      }

	public String getBestBusStop(Location vlocation, Point[] vPoints){

		String BusStop;



		float[] distances = new float[vPoints.length];

		HashMap<Float, String> collectionOfDistances = new HashMap<>();

		//Make a list of distances between CurrentLocation and Points and separate list of distances[]
		for (int i = 0; i < vPoints.length ;i++ ){

			double pointLong =vPoints[i].getLongtitude();
			double pointLat = vPoints[i].getLatitude();

			Location destLocation = new Location("");

			destLocation.setLatitude(pointLat);
			destLocation.setLongitude(pointLong);

			float distance = vlocation.distanceTo(destLocation);

			distances[i] = distance;

			collectionOfDistances.put(distance, vPoints[i].getName());
		}

		//Get the closest distance
		float smallest = distances[0];

		for (float distance : distances) {
			if (distance < smallest) {
				smallest = distance;
			}
		}

		//Get and return the Point according to it`s distance key value
		BusStop = collectionOfDistances.get(smallest);

		return BusStop;
	}



 @Override
  public void onProviderDisabled(String provider) {

  }

  @Override
  public void onProviderEnabled(String provider) {

  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {

  }

  private Location getBestLocation(Location location) {
	    // start off by handling cases where we only have one

	    if (lastLocation == null) {
	      return(location);
	    }

	    Location older=
	        (lastLocation.getTime() < location.getTime() ? lastLocation
	            : location);
	    Location newer=(lastLocation == older ? location : lastLocation);

	    // older and less accurate fixes suck

	    if (older.getAccuracy() <= newer.getAccuracy()) {
	      return(newer);
	    }

	    // if older is within error radius of newer, assume
	    // not moving and go with older (since has better
	    // accuracy, else would have been caught by previous
	    // condition)

	    // ideally, this would really be "if the odds of
	    // the older being within the error radius of the
	    // newer are higher than 50%", taking into account
	    // the older one's accuracy as well -- the
	    // implementation of this is left as an exercise for the
	    // reader

	    if (newer.distanceTo(older) < newer.getAccuracy()) {
	      return(older);
	    }

	    // if all else fails, choose the newer one -- the device
	    // is probably moving, and so we are better off with the
	    // newer fix, even if less accurate

	    return(newer);
	  }
}
