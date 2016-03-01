package com.cooksys.CookPayroll;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.cooksys.CookPayroll.objects.Session;

public class SessionManager {

	private static Map<UUID, Session> sessionMap = new ConcurrentHashMap<>();
	
	public static Map<UUID, Session> getSessionMap() {
		return sessionMap;
	}

	public static void setSessionMap(Map<UUID, Session> sessionMap) {
		SessionManager.sessionMap = sessionMap;
	}

}
