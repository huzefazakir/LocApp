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
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class UpdateAccess{

	private String scriptUrl = "http://10.2.19.184/updateAccess.php";
	private static final String TAG ="UpdateAccess";
	
	public int updateTokenOnServer(String accessToken){
		
		InputStream is = null;
		String result = "";
		int success = 0;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(scriptUrl);
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    nameValuePairs.add(new BasicNameValuePair("accessToken", accessToken));
	    
	  //HTTP post
	    try{
	    	    Log.e(TAG, "connecting to server");
	    	    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            HttpResponse response = httpclient.execute(httppost);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();

	    }catch(Exception e){
	            Log.e(TAG, "Error in http connection "+e.toString());
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
	            Log.i (TAG,"http Result:" +result);
	    }catch(Exception e){
	            Log.e(TAG, "Error converting result "+e.toString());
	    }
	    
	    try{
            //JSONArray jArray = new JSONArray(result);
            //for(int i=0;i<jArray.length();i++){
                JSONObject json_data = new JSONObject(result);
                Log.i(TAG,"success: "+json_data.getString("success"));
                success = json_data.getInt("success");
            //}        
	    } catch(JSONException e){
            Log.e(TAG, "Error parsing data "+e.toString());
	    }
	    return success;
	    	
	}
}
