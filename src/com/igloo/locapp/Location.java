package com.igloo.locapp;

public class Location {
	
	private long id;
	private String userID;
	private float distance; 
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserID() {
		return userID;
	}

	public void setComment(String comment) {
		this.userID = comment;
	}
	
	public float getDistance() {
		return distance;
	}
	
	public void setDistance(float distance) {
		 this.distance = distance;
	}

	// Will be used by the ArrayAdapter in the ListView
	/*@Override
	public String toString() {
		return comment;
	}*/
	

}
