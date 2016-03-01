package com.cooksys.CookPayroll;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import spark.servlet.SparkApplication;

import com.mongodb.DB;

public class SparkApp implements SparkApplication {
	
	@Override
	public void init() {
		DB db;
		try {
			db = Bootstrap.mongo();
			new UserRegistration(db);
	        new ServiceRegistration(db);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
