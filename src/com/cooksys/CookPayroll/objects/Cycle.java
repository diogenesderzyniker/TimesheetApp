package com.cooksys.CookPayroll.objects;

import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.CookPayroll.services.CycleService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Cycle {
	
	final static Logger LOGR = LoggerFactory.getLogger(Cycle.class);
	
	private ObjectId _id;
	private int cycleNum;
	private int month;
	private int half;
	private int year;
	
	private List<Timesheet> timesheets;
	
	
	public Cycle() {		
	}
	
	public DBObject bsonFromPojo(){
		BasicDBObject document = new BasicDBObject();

		document.put("_id", this._id);
		document.put("cycleNum", this.cycleNum);
		document.put("year",this.year);
		document.put("month", this.month);
		document.put("half", this.half);
		document.put("timesheets",this.timesheets);

		return document;
		
	}
	
	@SuppressWarnings("unchecked")
	public Cycle makePojoFromBson(DBObject bson){
		BasicDBObject b = (BasicDBObject) bson;
		this._id = b.getObjectId("_id");
		this.cycleNum = b.getInt("cycleNum");
		this.year = b.getInt("year");
		this.month = b.getInt("month");
		this.half = b.getInt("half");
		this.timesheets = (List<Timesheet>) b.get("timesheets");
		
		return this;
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

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getHalf() {
		return half;
	}

	public void setHalf(int half) {
		this.half = half;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public List<Timesheet> getTimesheets() {
		return timesheets;
	}

	public void setTimesheets(List<Timesheet> timesheets) {
		this.timesheets = timesheets;
	}
	
	
	
	
	
}
