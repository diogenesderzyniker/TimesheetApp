package com.cooksys.CookPayroll.objects;

import java.util.UUID;

public class Session {

	private String username = "";
	private UUID accessToken = null;
	private long timeStarted = 0;
	private long timeElapsed = 0;
	// default session length is 20 minutes
	private final long sessionExpiry = 1200000;
			
	private boolean isExpired = false;
	
	public Session(String username) {
		accessToken = UUID.randomUUID();
		timeStarted = System.currentTimeMillis();
		this.username = username;
	}

	public void refresh() {
		setTimeStarted(System.currentTimeMillis());
	}
	
	public UUID getSessionToken() {
		return accessToken;
	}

	private void setTimeStarted(long timeStarted) {
		this.timeStarted = timeStarted;
	}

	private long getTimeElapsed() {
		
		timeElapsed = System.currentTimeMillis() - timeStarted;
		
		System.out.println("Session Time Elapsed : " + timeElapsed);
		
		return timeElapsed;
	}

	public boolean isExpired() {
		if (getTimeElapsed() < sessionExpiry) {
			isExpired = false;
		} else {isExpired = true;}
		
		return isExpired;
	}

	public void setExpired(boolean isExpired) {
		this.isExpired = isExpired;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
