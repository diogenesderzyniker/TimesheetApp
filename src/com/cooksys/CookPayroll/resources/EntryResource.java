package com.cooksys.CookPayroll.resources;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.CookPayroll.services.EntryService;
import com.cooksys.CookPayroll.Bootstrap;
import com.cooksys.CookPayroll.JsonTransformer;

public class EntryResource {

	private static final String API_CONTEXT = "/api/v1";
	final static Logger LOGR = LoggerFactory.getLogger(EntryResource.class);
	
	private final EntryService entryService;
	
	public EntryResource(EntryService entryService){
		this.entryService = entryService;
		setupEndpoints();
	}
	
	private void setupEndpoints() {
		Bootstrap.getExService().execute(new Runnable() {
			@Override
			public void run() {
				post(API_CONTEXT + "/entries", "application/json",
						(request, response) -> {
							LOGR.info("/entries POST request : " + request.body());
							entryService.createNewEntry(request.body());
							LOGR.info("/entries POST response : " + response.body());
							response.status(201);
							return response;
						}, new JsonTransformer());
			}
		});
		Bootstrap.getExService().execute(new Runnable() {
			@Override
			public void run() {
				get(API_CONTEXT + "/entries/:id", "application/json",
						(request, response) -> {
							LOGR.info("/entries/:id POST request : " + request.body());
							return entryService.find(request.params(":id"));
						},new JsonTransformer());
			}
		});
		Bootstrap.getExService().execute(new Runnable() {
			
			@Override
			public void run() {
				get(API_CONTEXT + "/entries", "application/json",
						(request, response) -> {
							LOGR.info("/entries GET request : " + request.body());
							return entryService.findAll();
						},
						new JsonTransformer());
			}
		});
		Bootstrap.getExService().execute(new Runnable() {
			@Override
			public void run() {
				put(API_CONTEXT + "/entries/:id", "application/json",
						(request, response) -> {
							LOGR.info("/entries PUT request : " + request.body());
							return entryService.update(request.params(":id"), request.body());
						},new JsonTransformer());
			}
		});
	}
}
