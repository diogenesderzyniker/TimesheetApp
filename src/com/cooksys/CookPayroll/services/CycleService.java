package com.cooksys.CookPayroll.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.LogRecord;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.CookPayroll.Bootstrap;
import com.cooksys.CookPayroll.objects.Cycle;
import com.cooksys.CookPayroll.objects.Timesheet;
import com.cooksys.CookPayroll.utils.CookPayrollUtil;
import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class CycleService {
	final static Logger LOGR = LoggerFactory.getLogger(CycleService.class);

	private final DB db;
	private final DBCollection collection;
	
	public CycleService(DB db){
		this.db = db;
		this.collection = db.getCollection("cycle");
	}
	
//	public List<Cycle> findAll(){
//		List<Cycle> cycles = new ArrayList<Cycle>();
//		DBCursor dbObjects = collection.find();
//		
//		while (dbObjects.hasNext()){
//			DBObject dbObject = dbObjects.next();
//			cycles.add(new Cycle((BasicDBObject) dbObject));
//		}
//		
//		return cycles;
//	}
	
//	public Cycle find(String id){
//		return new Cycle((BasicDBObject) collection.findOne(new BasicDBObject("_id", new ObjectId(id))));
//	}
//	
//	public Cycle findByCycleId(String id){
//		
//		int cycleId = Integer.parseInt(id);
//		return new Cycle((BasicDBObject) collection.findOne(new BasicDBObject("_id", cycleId)));
//	}
	
	public Integer getCurrentCycle(){
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int year = cal.get(Calendar.YEAR);
		
		
		System.out.println(day);
		int half = day > 15 ? 2 : 1;
				
		Cycle cycle = getCycle(collection, year, month, half);
		
		return cycle.getCycleNum();
	}
	
//	public Cycle getCycleByDate(String body){
//		Cycle cycle = new Gson().fromJson(body, Cycle.class);
//		
//		return new Cycle((BasicDBObject) collection.findOne(new BasicDBObject("half", cycle.getCycle())
//				.append("month", cycle.getMonth())
//				.append("year", cycle.getYear())));
//	}
	
	
	public static Cycle getCycle(DBCollection collection, Integer cycleNum){
		
		BasicDBObject cycleMatch = new BasicDBObject();
		
		cycleMatch.put("cycleNum", cycleNum);
		
		DBCursor cursor = collection.find(cycleMatch);
		
		BasicDBObject dBCycle = null;
				
		while(cursor.hasNext()){
			dBCycle = (BasicDBObject) cursor.next();
			LOGR.info("found a cycle num :" + dBCycle.getString("cycleNum"));
			Cycle cycle = new Cycle();
			cycle.makePojoFromBson(dBCycle);			
			return cycle;						
		}
		
		LOGR.info("could not find cycle num: " + cycleNum +". Creating a new Cycle");
		
		Cycle cycle = new Cycle();
		cycle.generateId();
		cycle.setCycleNum(cycleNum);
		cycle.setHalf(CookPayrollUtil.cycleNumToHalf(cycleNum));
		cycle.setMonth(CookPayrollUtil.cycleNumToMonth(cycleNum));
		cycle.setYear(CookPayrollUtil.cycleNumToYear(cycleNum));
		cycle.setTimesheets(new ArrayList<Timesheet>());
		
		collection.insert(cycle.bsonFromPojo());
		
		
		return cycle;
	}
	public static Cycle getCycle(DBCollection collection, Integer year, Integer month, Integer half){
		return getCycle(collection, CookPayrollUtil.cycleNum(year, month, half));
	}
	
	
	
}