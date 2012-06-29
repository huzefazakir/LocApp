package com.igloo.locapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.igloo.locapp.contentprovider.LocationContentProvider;

public class UpdateLocations extends AsyncTask<String, Void, String> {
	
	private Uri locationUri;
	private Context locAppContext;
	private static String TAG = "UpdateLoactions"; 
	
	public UpdateLocations(Context context){
		locAppContext = context;
	}

	@Override
	protected String doInBackground(String... urls) {
		String response = "";
		getServerData(urls[0], urls[1], urls[2]);
			
		return response;
	}
	
	private void getServerData(String scriptUrl, String latitude, String longitude) {
	    
		   InputStream is = null;
		   String result = "";
		   HttpClient httpclient = new DefaultHttpClient();
		   HttpPost httppost = new HttpPost(scriptUrl);
		   
		    //the year data to send
		    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); // TODO:look at it on saturday
		    nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
		    nameValuePairs.add(new BasicNameValuePair("latitude",latitude));
		    nameValuePairs.add(new BasicNameValuePair("user_id","huzefazakir"));

		    //HTTP post
		    try{
		    	    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		            HttpResponse response = httpclient.execute(httppost);
		            HttpEntity entity = response.getEntity();
		            is = entity.getContent();

		    }catch(Exception e){
		            Log.e("log_tag", "Error in http connection "+e.toString());
		    }

		    //convert response to string
		    try{
		            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		            StringBuilder sb = new StringBuilder();
		            String line = null;
		            while ((line = reader.readLine()) != null) {
		                    sb.append(line + "\n");
		            }
		            is.close();
		            result=sb.toString();
		            Log.i ("Log_Tag",result);
		    }catch(Exception e){
		            Log.e("log_tag", "Error converting result "+e.toString());
		    }
		    //parse json data
		    try{
		            JSONArray jArray = new JSONArray(result);
		            for(int i=0;i<jArray.length();i++){
		                    JSONObject json_data = jArray.getJSONObject(i);
		                    Log.i("log_tag","id: "+json_data.getInt("id")+
		                            ", name: "+json_data.getString("user_id")+
		                            ", distance: "+json_data.getInt("distance")+
		                            ", date: "+json_data.getString("time_stamp")
		                    );
		                    
		                    ContentValues values = new ContentValues();
		                    //values.put(LocationTable.COLUMN_USER_ID, json_data.getString("user_id") );
		                    values.put(LocationTable.COLUMN_DISTANCE, json_data.getInt("distance"));
		                    values.put(LocationTable.COLUMN_TIME, json_data.getString("time_stamp"));
		                    
		                    String [] projection ={LocationTable.COLUMN_TIME};
		                    String selection = LocationTable.COLUMN_USER_ID + " = ?";
		                    String [] selectionArgs = {json_data.getString("user_id")};
		                    Cursor cursor = locAppContext.getContentResolver().query(LocationContentProvider.CONTENT_URI , projection, selection, selectionArgs, null);
		                    
		                    if (cursor != null){
		                    	if(cursor.getCount() > 1){
		                    		Log.e(TAG,"erroneous getCount:" + cursor.getCount());
		                    	}
		                    	
		                    	else if (cursor.getCount() == 1){
		                    		int rowsUpdated =  locAppContext.getContentResolver().update(LocationContentProvider.CONTENT_URI, values, selection, selectionArgs);
		                    		Log.d(TAG,"Number of rows updated: " + rowsUpdated );
		                    	}
		                    	
		                    	else {
		                    	    values.put(LocationTable.COLUMN_USER_ID, json_data.getString("user_id") );
		                            locationUri = locAppContext.getContentResolver().insert(LocationContentProvider.CONTENT_URI, values);
		                    	}
		                    	
		                    	cursor.close();
		                    	
		                    }
		                    else {
		                    	Log.e(TAG, "cursor null");
		                    }
		            }
		    }catch(JSONException e){
		            Log.e("log_tag", "Error parsing data "+e.toString());
		    } 
		}    
}
