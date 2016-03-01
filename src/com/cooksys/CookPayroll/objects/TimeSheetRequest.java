package com.cooksys.CookPayroll.objects;

import org.bson.types.ObjectId;

public class TimeSheetRequest {
	
	private ObjectId employeeId;
	private String accessToken;
	private Integer year;
	private Integer month;	
	private Integer half;
	
	public ObjectId getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(ObjectId employeeId) {
		this.employeeId = employeeId;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public Integer getHalf() {
		return half;
	}
	public void setHalf(Integer half) {
		this.half = half;
	}
	
	
	

}
