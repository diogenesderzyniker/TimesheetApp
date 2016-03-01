package com.cooksys.CookPayroll.objects;

public class TimeSheetPostRequest {
	
	private String accessToken;
	private Timesheet timesheet;

	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public Timesheet getTimesheet() {
		return timesheet;
	}
	public void setTimesheet(Timesheet timesheet) {
		this.timesheet = timesheet;
	}
	
	

}
