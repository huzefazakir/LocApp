package com.igloo.locapp;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LocationTable {
	
	public static final String TABLE_LOCATION = "location";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_USER_ID = "user_id";
	public static final String COLUMN_DISTANCE = "distance";
	public static final String COLUMN_TIME ="time";
	
	
	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_LOCATION + "( " + COLUMN_ID
			+ " integer primary key, " + COLUMN_USER_ID
			+ " text null, " +COLUMN_DISTANCE + " integer not null, " + COLUMN_TIME + " text not null);";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(LocationTable.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
		onCreate(db);
	}

}
