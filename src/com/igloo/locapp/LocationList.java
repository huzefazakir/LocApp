package com.igloo.locapp;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

import com.igloo.locapp.contentprovider.LocationContentProvider;

public class LocationList extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
	boolean mDualPane;
	int currentCheckPosition;
	private SimpleCursorAdapter adapter;
	
	@Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        Log.i("fragment" ,"OnActivityCreated");   
        displayLocations();
	}
	
    private void displayLocations() {
    	String[] from = new String[] {LocationTable.COLUMN_USER_ID, LocationTable.COLUMN_DISTANCE, LocationTable.COLUMN_TIME };
		// Fields on the UI to which we map
		int[] to = new int[] { R.id.textView2, R.id.textView1, R.id.textView3};
		getActivity().getSupportLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(getActivity(),R.layout.location_row, null, from, to);
		setListAdapter(adapter);    	 
    }

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String[] projection = { LocationTable.COLUMN_ID, LocationTable.COLUMN_USER_ID, LocationTable.COLUMN_DISTANCE, LocationTable.COLUMN_TIME };
		CursorLoader cursorLoader = new CursorLoader(getActivity(), LocationContentProvider.CONTENT_URI, projection, null, null, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
		adapter.changeCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.changeCursor(null);
	}
}
