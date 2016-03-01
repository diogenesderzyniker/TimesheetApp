package com.cooksys.CookPayroll.objects;

import java.util.List;

import org.bson.types.ObjectId;

import com.cooksys.CookPayroll.Main;
import com.cooksys.CookPayroll.resources.TimesheetResource;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class Timesheet {

	private ObjectId _id;
	private int cycleNum; // the cycle that that timesheet belongs to
	private ObjectId employeeId;
	private List<Day> days;
	private ObjectId validatorId;
	private String status = "Pending";
	private String comment;
	
	public Timesheet() {
		
	}

	public DBObject bsonFromPojo() {
		BasicDBObject document = new BasicDBObject();
		
		document.put("_id", this._id);
		document.put("cycleNum", this.cycleNum);
		document.put("employeeId", this.employeeId);
		document.put("status", this.status);
		Gson g = new Gson();
		
		document.put("days", JSON.parse(g.toJson(this.days)));

		return document;
	}

	@SuppressWarnings("unchecked")
	public void makePojoFromBson(DBObject bson) {
		BasicDBObject b = (BasicDBObject) bson;

		this._id 		= b.getObjectId("_id");
		this.cycleNum 	= b.getInt("cycleNum");
		try {
			this.employeeId = b.getObjectId("employeeId");
		} catch (ClassCastException cce) {
			cce.printStackTrace();
			System.out.println("This timesheet contains an EmployeeID as a String. We need to change or delete it.");
			this.employeeId = new ObjectId(b.getString("employeeId"));
			
			System.out.println("Replace the timesheet with objectID : " + get_id().toString());
		}
		this.days 		= (List<Day>) b.get("days");
		this.status		= b.getString("status");
		
		if (b.containsField("comment")) this.comment = b.getString("comment");
	}
	
	public void generateId(){
		if(this._id == null){
			this._id = new ObjectId();
		}
	}

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public int getCycleNum() {
		return cycleNum;
	}

	public void setCycleNum(int cycleNum) {
		this.cycleNum = cycleNum;
	}

	public ObjectId getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(ObjectId employeeId) {
		this.employeeId = employeeId;
	}

	public List<Day> getDays() {
		return days;
	}

	public void setDays(List<Day> days) {
		this.days = days;
	}

	public ObjectId getValidatorId() {
		return validatorId;
	}

	public void setValidatorId(ObjectId validator) {
		this.validatorId = validator;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
