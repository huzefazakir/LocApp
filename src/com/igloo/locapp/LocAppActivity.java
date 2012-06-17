package com.igloo.locapp;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

import com.igloo.locapp.contentprovider.LocationContentProvider;


public class LocAppActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    
	// private Cursor cursor;
	private SimpleCursorAdapter adapter;
	private static final String scriptUrl = "http://10.2.19.184/getLocation.php";
	
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_list);
        this.getListView().setDividerHeight(2);
        getData();
		fillData();
		
		
	/*	new Thread(new Runnable() { 
            public void run(){
            	String test = "";
            	//getServerData(test);
            	Log.d("test","final data: " + test);
            }
        }).start();*/
		
		
        
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
	
	private void getData() {
		UpdateLocations locations = new UpdateLocations(getBaseContext());
		locations.execute(new String[] {scriptUrl});
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
	
	
}