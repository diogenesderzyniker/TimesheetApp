package com.cooksys.CookPayroll.objects;

import java.util.Date;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;

public class Entry {
	
	
	private String id;
	private Date day;
	private int hours;
	public String hoursType = "";
	private String clientName = "";
	private String projectName ="";
	private String comments ="";
	
	
	public Entry(BasicDBObject dbObject){
		
		this.id = ((ObjectId) dbObject.get("_id")).toString();		
		this.day = dbObject.getDate("day");
		this.clientName = dbObject.getString("clientName");
		this.projectName = dbObject.getString("projectName");
		this.comments = dbObject.getString("comments");
		
	}
	
	public Entry(){
		
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public String getHoursType() {
		return hoursType;
	}

	public void setHoursType(String hoursType) {
		this.hoursType = hoursType;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientNameId(String clientName) {
		this.clientName = clientName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectNameId(String projectName) {
		this.projectName = projectName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	

	

}
