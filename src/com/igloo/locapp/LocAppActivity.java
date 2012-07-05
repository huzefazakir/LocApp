package com.igloo.locapp;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;


public class LocAppActivity extends FragmentActivity implements LocationListener {
    
	
	Facebook facebook = new Facebook("355719041162891");
	// private Cursor cursor;
	private SimpleCursorAdapter adapter;
	private static final String scriptUrl = "http://10.2.19.184/getLocation.php";
	private LocationManager locationManager;
	private String provider;
	private String TAG = "LocAppActivity";
	
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_list_fragment);
        
        facebook.authorize(this, new DialogListener() {
            @Override
            public void onComplete(Bundle values) {}

            @Override
            public void onFacebookError(FacebookError error) {}

            @Override
            public void onError(DialogError e) {}

            @Override
            public void onCancel() {}
        });
    
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
    
    
    private void updateUserLocation(){
    	
    }
	
	
	private void getData(float latitude, float longitude) {
		UpdateLocations locations = new UpdateLocations(getBaseContext());
		locations.execute(new String[] {scriptUrl, Float.toString(longitude), Float.toString(latitude)});
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