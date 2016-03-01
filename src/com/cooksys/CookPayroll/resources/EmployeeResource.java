package com.cooksys.CookPayroll.resources;

import static spark.Spark.post;
import static spark.Spark.put;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.CookPayroll.Bootstrap;
import com.cooksys.CookPayroll.JsonTransformer;
import com.cooksys.CookPayroll.services.EmployeeService;


public class EmployeeResource {

	final static Logger LOGR = LoggerFactory.getLogger(TimesheetResource.class);
	private static final String API_CONTEXT = "/api/v1";
	private EmployeeService empService;
	
	public EmployeeResource(EmployeeService service) {
		this.empService = service;
		setupEndpoints();
	}
	
	private void setupEndpoints(){
		Bootstrap.getExService().execute(new Runnable() {
			
			@Override
			public void run() {
			post(API_CONTEXT + "/all_employees", "application/json", (request, respone) -> {
				LOGR.info("retrieving employees");
				return empService.getEmployees();
			}, new JsonTransformer());
				
			}
		});
		
		Bootstrap.getExService().execute(new Runnable() {
			
			@Override
			public void run() {
			put(API_CONTEXT + "/update_employee", "application/json", (request, response) -> {
				LOGR.info("update_employee request : " + request.body());
				empService.updateEmployee(request.body());
				return true;
			}, new JsonTransformer());
			}
		});
		
		Bootstrap.getExService().execute(new Runnable() {
			
			@Override
			public void run() {
			post(API_CONTEXT + "/find_employee", "application/json", (request, response) -> {
				LOGR.info("find_employee request : " + request.body());
				return empService.getEmployeeByID(request.body());
			}, new JsonTransformer());
			}
		});
	}
}
