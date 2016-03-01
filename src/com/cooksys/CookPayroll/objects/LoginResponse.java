package com.cooksys.CookPayroll.objects;

import java.util.UUID;

import org.bson.types.ObjectId;

public class LoginResponse {
	private ObjectId employeeId;
	private String role;
	private boolean loginSuccess;
	private UUID accessToken;
	
	public ObjectId getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(ObjectId employeeId) {
		this.employeeId = employeeId;
	}
	public boolean isLoginSuccess() {
		return loginSuccess;
	}
	public void setLoginSuccess(boolean loginSuccess) {
		this.loginSuccess = loginSuccess;
	}
	public UUID getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(UUID accessToken) {
		this.accessToken = accessToken;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	

}
