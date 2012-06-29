package com.igloo.locapp;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.igloo.locapp.contentprovider.LocationContentProvider;


public class LocAppActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor>, LocationListener {
    
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
        setContentView(R.layout.location_list);
        this.getListView().setDividerHeight(2); //TODO: check what is this.
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
		fillData();
			        
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
	
	private void fillData() {

		// Fields from the database (projection)
		// Must include the _id column for the adapter to work
		String[] from = new String[] { LocationTable.COLUMN_ID, LocationTable.COLUMN_USER_ID, LocationTable.COLUMN_DISTANCE, LocationTable.COLUMN_TIME };
		// Fields on the UI to which we map
		int[] to = new int[] { R.id.label1, R.id.label2, R.id.label3, R.id.label4  };

		getLoaderManager().initLoader(0, null, this);
		adapter = new SimpleCursorAdapter(this, R.layout.location_row, null, from,
				to, 0);

		setListAdapter(adapter);
	}
	
	private void getData(float latitude, float longitude) {
		UpdateLocations locations = new UpdateLocations(getBaseContext());
		locations.execute(new String[] {scriptUrl, Float.toString(longitude), Float.toString(latitude)});
	}
	

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
		String[] projection = { LocationTable.COLUMN_ID, LocationTable.COLUMN_USER_ID, LocationTable.COLUMN_DISTANCE, LocationTable.COLUMN_TIME };
		CursorLoader cursorLoader = new CursorLoader(this,
				LocationContentProvider.CONTENT_URI, projection, null, null, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		adapter.swapCursor(data);		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// data is not available anymore, delete reference
		adapter.swapCursor(null);
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