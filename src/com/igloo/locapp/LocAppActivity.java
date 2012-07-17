package com.igloo.locapp;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;


public class LocAppActivity extends FragmentActivity implements LocationListener {
    
	
	
	// private Cursor cursor;
	//private SimpleCursorAdapter adapter;
	private static final String locationScriptUrl = "http://10.2.19.184/getLocation.php";
	//private static final String accessScriptUrl = "http://10.2.19.184/updateAccess.php";
	private LocationManager locationManager;
	private String provider;
	private static final String TAG = "LocAppActivity";
	private static final String FB_APP_ID = "355719041162891";
	private SharedPreferences fbSharedPrefs;
	
	Facebook facebook = new Facebook(FB_APP_ID);
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);      
       
        setContentView(R.layout.location_list_fragment_v2);
        /*
         * Get existing access_token if any
         */
        fbSharedPrefs = getPreferences(MODE_PRIVATE);
        String access_token = fbSharedPrefs.getString("access_token", null);
        long expires = fbSharedPrefs.getLong("access_expires", 0);
        if(access_token != null) {
            facebook.setAccessToken(access_token);
            Log.d(TAG, "access token from facebook" + access_token);
        }
        if(expires != 0) {
            facebook.setAccessExpires(expires);
        }
        
        
        /*
         * Only call authorize if the access_token has expired.
         */
        if(!facebook.isSessionValid()) {

            facebook.authorize(this, new String[] {}, new DialogListener() {
                @Override
                public void onComplete(Bundle values) {
                    SharedPreferences.Editor editor = fbSharedPrefs.edit();
                    editor.putString("access_token", facebook.getAccessToken());
                    editor.putLong("access_expires", facebook.getAccessExpires());
                    editor.commit();
                }
    
                @Override
                public void onFacebookError(FacebookError error) {Log.e(TAG,"Facebook error" + error.toString());}
    
                @Override
                public void onError(DialogError error) {Log.e(TAG,"Facebook dialog error" + error.toString());}
    
                @Override
                public void onCancel() {Log.e(TAG,"Facebook autentication cancelled");}
            });
        }
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        //Criteria criteria = new Criteria();
		//provider = locationManager.getBestProvider(criteria, false);
        provider = LocationManager.GPS_PROVIDER;
		Location location = locationManager.getLastKnownLocation(provider);

		// Initialize the location fields
		if (location != null) {
			System.out.println("Provider " + provider + " has been selected.");
			int lat = (int) (location.getLatitude());
			int lng = (int) (location.getLongitude());
			getData((float) location.getLatitude(), (float) location.getLongitude());
			Log.i(TAG,"latitude: "+ lat + "Longitude:"+lng);
		} else {
			Log.i(TAG,"Provider not available");	
		}        
			        
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }
    
    @Override
    protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(provider, 0, 0, this); // TODO : cannot have 0,0
	}
    
    @Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}
    
    
   /* private void updateUserLocation(){
    	
    }*/
	
	
	private void getData(float latitude, float longitude) {
		
		UpdateLocations locations = new UpdateLocations(getBaseContext(), facebook.getAccessToken());
		locations.execute(new String[] {locationScriptUrl, Float.toString(longitude), Float.toString(latitude)});
	}
	

	@Override
	public void onLocationChanged(Location location) {
		Log.i("onLoactionChanged","latitude: "+ location.getLatitude() + "Longitude:"+location.getLongitude());
		getData((float)location.getLongitude(), (float)location.getLatitude());
	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "Disabled provider " + provider,
				Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "Enabled new provider " + provider,
				Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}	
}