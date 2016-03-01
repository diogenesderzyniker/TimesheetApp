package com.cooksys.CookPayroll.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mongodb.BasicDBObject;

public class Day {
	
	private Date date;
	private Integer day;
	private Integer month;
	private Integer year;
	private List<Entry> entries = new ArrayList<Entry>();
	
	@SuppressWarnings("unchecked")
	public Day(BasicDBObject dbObject){
		this.date = dbObject.getDate("date");
		this.entries = ((List<Entry>) dbObject.get("entries"));
	
	}
	
	public Day(){
		
	}

	public List<Entry> getEntries(){
		return entries;
	}
	
	

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}
	
	
}
