package com.cooksys.CookPayroll.resources;

import static spark.Spark.get;
import static spark.Spark.put;
import static spark.Spark.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.CookPayroll.Bootstrap;
import com.cooksys.CookPayroll.JsonTransformer;
import com.cooksys.CookPayroll.objects.Timesheet;
import com.cooksys.CookPayroll.services.TimesheetService;

public class TimesheetResource {

	private TimesheetService timesheetService;
	private static final String API_CONTEXT = "/api/v1";
	final static Logger LOGR = LoggerFactory.getLogger(TimesheetResource.class);
	
	public TimesheetResource(TimesheetService timesheetService){
		this.timesheetService = timesheetService;
		setupEndpoints();
	}
	
	private void setupEndpoints() {
//		Bootstrap.getExService().execute(new Runnable() {
//			@Override
//			public void run() {
//				get(API_CONTEXT + "/timesheet/:employeeId", "application/json", 
//						(request, response) -> {
//							LOGR.info("/timesheet/:employeeId GET request : " + request.body());
//							return timesheetService.getTimesheet(request.body());
//						},new JsonTransformer());
//			}
//		});
		Bootstrap.getExService().execute(new Runnable() {
			@Override
			public void run() {
				post(API_CONTEXT + "/timesheet", "application/json", 
						(request, response) -> {
							LOGR.info("/timesheet POST request : " + request.body());
							Timesheet timesheet = timesheetService.getTimesheet(request.body());
							response.status(201);
							return timesheet;
						}, new JsonTransformer());
			}
		});
		Bootstrap.getExService().execute(new Runnable() {
			@Override
			public void run() {
				put(API_CONTEXT + "/timesheet", "application/json", 
						(request, response) -> {
							LOGR.info("/timesheet/ PUT request : " + request.body());
							return timesheetService.addNewTimesheet(request.body());
						},new JsonTransformer());
			}
		});
		Bootstrap.getExService().execute(new Runnable() {
			@Override
			public void run() {
				post(API_CONTEXT + "/timesheet_approval", "application/json",
						(request, response) -> {
							LOGR.info("/timesheet_approval POST request : " + request.body());
							timesheetService.addNewTimesheet(request.body());
							response.status(200);
							return true;
						}, new JsonTransformer());
			}
		});
		Bootstrap.getExService().execute(new Runnable() {
			@Override
			public void run() {
				post(API_CONTEXT + "/history", "application/json",
						(request, response) -> {
							LOGR.info("/history POST request : " + request.body());
							
							return timesheetService.getHistory(request.body());
						}, new JsonTransformer());
			}
		});
	}

	public TimesheetService getTimesheetService() {
		return timesheetService;
	}

	public void setTimesheetService(TimesheetService timesheetService) {
		this.timesheetService = timesheetService;
	}
}
