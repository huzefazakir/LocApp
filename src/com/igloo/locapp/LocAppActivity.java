package com.igloo.locapp;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.igloo.locapp.contentprovider.LocationContentProvider;


public class LocAppActivity extends FragmentActivity implements
		LocationListener, OnClickListener {
	
	//private static final String locationScriptUrl = "http://10.2.19.184/getLocation.php";
	private static final String locationScriptUrl = "http://igloo.dyndns-server.com/scripts/getLocation.php";
	private Button refreshButton; 
	private LocationManager locationManager;
	private String provider;
	private static final String TAG = "LocAppActivity";
	private static final int TIME_BETWEEN_UPDATES = 15000;
	private static final int DISTANCE_BETWEEN_UPDATES = 1000;
	public static Spinner spinner;
	
	private static final int SPLASH = 0;
	private static final int LIST = 1;
	private static final int SETTINGS = 2;
	private static final int FRAGMENT_COUNT = SETTINGS + 1;
	private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
	private boolean isResumed = false;
	private MenuItem settings;
	private UiLifecycleHelper uiHelper;
	//private UpdateLocations locations;
	private double longitude = 360;
	private double latitude = 360;
	boolean clearUserDb = false;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		
		
		//code to print out the key hash
	   /* try {
	        PackageInfo info = getPackageManager().getPackageInfo(
	                "com.igloo.locapp", 
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            }
	    } catch (NameNotFoundException e) {

	    } catch (NoSuchAlgorithmException e) {

	    }*/
		
		
		
		
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		setContentView(R.layout.location_list_fragment_v2);
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// Criteria criteria = new Criteria();
		// provider = locationManager.getBestProvider(criteria, false);
		provider = LocationManager.GPS_PROVIDER;
		
		
		// hiding the fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragments[SPLASH] = fragmentManager.findFragmentById(R.id.splashFragment);
		fragments[LIST] = fragmentManager.findFragmentById(R.id.listFragment);
		fragments[SETTINGS] = fragmentManager.findFragmentById(R.id.userSettingsFragment);
		
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		for(int i = 0; i < fragments.length; i++) {
			transaction.hide(fragments[i]);
		}
		transaction.commit();
		
		Log.e(TAG,"Fragments hidden");
		
		//visibilityButton = (ToggleButton) findViewById(R.id.toggleButton1);
		//visibilityButton.setOnClickListener(this);
		refreshButton = (Button) findViewById(R.id.refresh);
		refreshButton.setOnClickListener(this);
		//spinner = (Spinner) findViewById(R.id.spinner1);
		// Create an ArrayAdapter using the string array and a default spinner layout
		//ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		       // R.array.array_radius_miles, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		//adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		//spinner.setAdapter(adapter);
		
		Log.e(TAG,"Activity created");
		

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	protected void onResume() {
		super.onResume();
		isResumed = true;
		
		Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }
	    
	    //updateUIWithExistingLocation();
		//updateUIWithLastKnownLocation();
		locationManager.requestLocationUpdates(provider, TIME_BETWEEN_UPDATES, DISTANCE_BETWEEN_UPDATES, this);
		uiHelper.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		uiHelper.onPause();
		locationManager.removeUpdates(this);	
		isResumed = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}
	

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (fragments[SPLASH].isHidden()) {
			if(menu.size() == 0)
				settings = menu.add(R.string.settings);
			return true;
		} else {
			menu.clear();
			settings = null;
		}
		return false;
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.equals(settings)) {
			showFragment(SETTINGS, true);
			return true;
		}
		return false;
	}

	private void getData(double latitude, double longitude, String accessToken) {

		UpdateLocations locations = new UpdateLocations(getBaseContext());
		locations.execute(new String[] { locationScriptUrl,
				Double.toString(longitude), Double.toString(latitude), accessToken });
		updateUserLocationOnServer(latitude, longitude, accessToken);
	}


	@Override
	public void onLocationChanged(Location location) {
		
		Session session = getFacebookActiveSession();
		    if (session != null) {
		    		Log.i("onLoactionChanged", "latitude: " + location.getLatitude()
		    				+ "Longitude:" + location.getLongitude());
		    		latitude = location.getLatitude();
		    		longitude = location.getLongitude();
		    		getData(latitude, longitude, session.getAccessToken());
		    }
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

	@Override
	public void onClick(View v) {
		//Log.d(TAG,"Visibilty:" + visibilityButton.getText().toString());
		//updateVisibilityOnServer(visibilityButton.getText().toString());
		Log.i(TAG,"Refresh");
		updateUIWithExistingLocation();
	}
	
	/*private void updateVisibilityOnServer(String visibilityState){
		UpdateVisibility visibility = new UpdateVisibility("hello");
		visibility.execute(visibilityState);
	}*/
	
	private void updateUserLocationOnServer(double latitude, double longitude, String accessToken){
		Log.d(TAG,"in update spinner entries");
		GeoFinder geoInfo = new GeoFinder(this);
		geoInfo.execute(Double.toString(latitude), Double.toString(longitude), accessToken);	
	}
	
	
	private void showFragment(int fragmentIndex, boolean addToBackStack) {
		Log.e(TAG, "show fragment, index: "+fragmentIndex);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		for(int i=0; i < fragments.length; i++ ) {
			if (fragmentIndex == SPLASH) {
				Log.e(TAG,"clearing DB");
				int rowsDeleted =  getContentResolver().delete(LocationContentProvider.CONTENT_URI, null, null);
				Log.e(TAG,"rows deleted: " + rowsDeleted);
			}
			if (i==fragmentIndex) {
				transaction.show(fragments[i]);
			}
			else 
				transaction.hide(fragments[i]);
		}
		if (addToBackStack) 
			transaction.addToBackStack(null);
		
		if (fragmentIndex == LIST)
			updateUIWithLastKnownLocation();
		
		transaction.commit();
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	
		if (isResumed) {
			FragmentManager manager = getSupportFragmentManager();
			int backStackSize = manager.getBackStackEntryCount();
			
			for(int i = 0; i< backStackSize; i++)
				manager.popBackStack();
		
			if (state.isOpened()) {
				showFragment(LIST, false);
			}
			else if (state.isClosed())
				showFragment(SPLASH, false);
				
			
		}
	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		Session session = Session.getActiveSession();
		
		if (session != null && session.isOpened())
			showFragment(LIST, false);
		else
			showFragment(SPLASH, false);
		
	}
	
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
		
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	
	private void updateUIWithLastKnownLocation() {

		List<String> providers = locationManager.getProviders(true);
		Location lastKnownLoc = null;
		
		
		for (int i=providers.size()-1; i>=0; i--) {
            lastKnownLoc = locationManager.getLastKnownLocation(providers.get(i));
            if (lastKnownLoc != null) break;
		}
    
		if (lastKnownLoc != null) {
			Session session = getFacebookActiveSession();
			latitude = lastKnownLoc.getLatitude();
			longitude = lastKnownLoc.getLongitude();
			
			Log.i(TAG, "latitude: " + latitude
    				+ "Longitude:" + longitude);
			
			if(session != null)
				getData(latitude, longitude, session.getAccessToken());
		} else {
			Log.e(TAG,"last known location not available");
		}
		
		
	}
	
	private void updateUIWithExistingLocation() {
		Session session = getFacebookActiveSession();
		if (session != null && latitude != 360 && latitude != 360) {
			getData(latitude, longitude, session.getAccessToken());
		}
	}
	
	private Session getFacebookActiveSession() {
		Session session = Session.getActiveSession();
	    if (session != null && session.isOpened())
	    	return session;
	    else 
	    	return null;
	}			
	
}