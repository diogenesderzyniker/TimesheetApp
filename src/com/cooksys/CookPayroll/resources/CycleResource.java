package com.cooksys.CookPayroll.resources;

import static spark.Spark.get;
import static spark.Spark.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.CookPayroll.services.CycleService;
import com.cooksys.CookPayroll.Bootstrap;
import com.cooksys.CookPayroll.JsonTransformer;;

public class CycleResource {

	private static final String API_CONTEXT = "/api/v1";
	final static Logger LOGR = LoggerFactory.getLogger(CycleResource.class);
	
	private final CycleService cycleService;
	
	public CycleResource(CycleService cycleService){
		this.cycleService = cycleService;
		setupEndpoints();
	}
	
	private void setupEndpoints() {
		Bootstrap.getExService().execute(new Runnable() {
			@Override
			public void run() {
				post(API_CONTEXT + "/cycle/date", "application/json",
						(request, response) -> { 
							LOGR.info("/cycle/date POST request : " + request.body());
							//cycleService.getCycleByDate(request.body());
							LOGR.info("/cycle/date POST response : " + response.body());
							response.status(201);
							return response;
						},new JsonTransformer());
			}
		});
		Bootstrap.getExService().execute(new Runnable() {
			@Override
			public void run() {
				get(API_CONTEXT + "/currentCycle", "application/json",
						(request, response) -> {
							LOGR.info("/currentCycle GET request : " + request.body());
							return cycleService.getCurrentCycle();
						},new JsonTransformer());
			}
		});
		Bootstrap.getExService().execute(new Runnable() {
			@Override
			public void run() {
				get(API_CONTEXT + "/cycle/:id", "application/json",
						(request, response) -> {
							LOGR.info("/cycle/:id get request : " + request.body());
							//return cycleService.findByCycleId(request.params(":id"));
							return null;
						},new JsonTransformer());
			}
		});
		Bootstrap.getExService().execute(new Runnable() {
			@Override
			public void run() {
				get(API_CONTEXT + "/cycles", "application/json",
						(request, response) -> {
							LOGR.info("/cycles POST request : " + request.body());
							//return cycleService.findAll();
							return null;
						},new JsonTransformer());
			}
		});
	}
}
