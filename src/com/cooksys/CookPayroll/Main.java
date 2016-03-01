package com.cooksys.CookPayroll;

import static spark.SparkBase.staticFileLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;

import spark.servlet.SparkApplication;

public class Main implements SparkApplication {
	final static Logger LOGR = LoggerFactory.getLogger(Main.class);
	
	public static ServiceRegistration serviceRegistration = null;
	@Override
	public void init() {
		
		staticFileLocation("/public");
        DB db;
		try {
			LOGR.debug("Starting service");
			
			db = Bootstrap.mongo();
			new UserRegistration(db);
			Main.serviceRegistration = new ServiceRegistration(db);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
	}
	public static ServiceRegistration getServiceRegistration() {
		return serviceRegistration;
	}
	public void setServiceRegistration(ServiceRegistration serviceRegistration) {
		this.serviceRegistration = serviceRegistration;
	}

}
