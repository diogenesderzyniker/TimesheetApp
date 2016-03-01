package com.cooksys.CookPayroll.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.CookPayroll.objects.Cycle;
import com.cooksys.CookPayroll.objects.Day;
import com.cooksys.CookPayroll.objects.Entry;
import com.cooksys.CookPayroll.objects.LoginResponse;
import com.cooksys.CookPayroll.objects.TimeSheetRequest;
import com.cooksys.CookPayroll.objects.Timesheet;
import com.cooksys.CookPayroll.utils.CookPayrollUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

public class TimesheetService {
	final static Logger LOGR = LoggerFactory.getLogger(TimesheetService.class);
	
	private DB db;
	private DBCollection collection;

	public TimesheetService(DB mongodb) {
		this.db = mongodb;
		this.collection = db.getCollection("cycle");
		

		LOGR.debug("DB name : " + db.getName());
		LOGR.debug("testData exists : " + db.collectionExists("cycle"));

	}
	
	/**
	 * Adds new timesheet if no matching timesheet already exists. Else the matching timesheet is updated.
	 * @param body
	 * @return Timesheet
	 */
	public Timesheet addNewTimesheet(String body) {
		Gson g = new Gson();
		Timesheet timesheet = g.fromJson(body, Timesheet.class);
		LOGR.info("passed timesheet cycle Num:" + timesheet.getCycleNum());
		
		Cycle dbCycle = CycleService.getCycle(collection, timesheet.getCycleNum());
		
		BasicDBObject match = (BasicDBObject) dbCycle.bsonFromPojo();
		
		BasicDBList timesheets = (BasicDBList) match.get("timesheets");
		
		Iterator<Object> itor = timesheets.iterator();
		
		while(itor.hasNext()){
			BasicDBObject timesheetBasicDBObject = (BasicDBObject) itor.next();
			Timesheet ts = new Timesheet();
			ts.makePojoFromBson(timesheetBasicDBObject);
			if(ts.getEmployeeId().equals(timesheet.getEmployeeId())){
				LOGR.info("found a matching time sheet, updating it");
				match.put("timesheets._id", ts.get_id());
				
				System.out.println("MATCHING TIMESHEET ID : " + match.toString());
				
				BasicDBObject timesheetSpec = new BasicDBObject();
				timesheetSpec.put("timesheets.$.days", JSON.parse(g.toJson(timesheet.getDays())));

				if (timesheet.getStatus() != null) {
					timesheetSpec.put("timesheets.$.status", timesheet.getStatus());
					timesheetSpec.put("timesheets.$.validatorId", timesheet.getValidatorId());
					timesheetSpec.put("timesheets.$.comment", timesheet.getComment());
				}
				BasicDBObject update = new BasicDBObject();
				update.put("$set", timesheetSpec);
				
				System.out.println("UPDATE TIMESHEET : " + update.toString());
				
				WriteResult wr = collection.update(match, update);
				System.out.println("TIMESHEET UPDATE WRITE RESULT : " + wr.toString());
				return ts;
			}
			
		}
		
		LOGR.info("could not find a time sheet for the employee with the ID:" + timesheet.getEmployeeId() + ". saving a new one to the data base");
		timesheet.generateId();
		BasicDBObject timesheetBasicDBObject = (BasicDBObject) timesheet.bsonFromPojo();
		
		BasicDBObject update = new BasicDBObject();
		update.put("$push", new BasicDBObject("timesheets",timesheetBasicDBObject));
		
		collection.update(match, update);
		


		return timesheet;
	}


	public Timesheet getTimesheet(String body) {
		Gson g = new GsonBuilder().create();
		TimeSheetRequest timeSheetRequest = new Gson().fromJson(body, TimeSheetRequest.class);
		LOGR.info("getting time sheet");
		
		System.out.println("EmpID " + timeSheetRequest.getEmployeeId().toString());
		
		Cycle dbCycle = CycleService.getCycle(collection, timeSheetRequest.getYear(), timeSheetRequest.getMonth(), timeSheetRequest.getHalf());

		if (dbCycle == null) {
			LOGR.info("Cycle was not found, this is a problem");
			return null;
		}
		
		DBObject match = dbCycle.bsonFromPojo();
		
		@SuppressWarnings("unchecked")
		List<Timesheet> timesheetList =  (List) match.get("timesheets");
		
		BasicDBList timesheets = new BasicDBList();
		timesheets.addAll(timesheetList);
		
		Iterator<Object> itor = timesheets.iterator();
		
		while(itor.hasNext()){
			BasicDBObject timesheetBasicDBObject = (BasicDBObject) itor.next();
			Timesheet ts = new Timesheet();
			ts.makePojoFromBson(timesheetBasicDBObject);
			if (ts.getEmployeeId() != null) {
				if(ts.getEmployeeId()
						.equals(timeSheetRequest.getEmployeeId().toString())){
					LOGR.info("found a matching time sheet");				
					return ts;
				}
			}
		}
		
		return createNewTimeSheet(timeSheetRequest);
	}

	public Timesheet createNewTimeSheet(TimeSheetRequest tsr) {

		Timesheet timesheet = new Timesheet();
		timesheet.generateId();
		timesheet.setCycleNum(CookPayrollUtil.cycleNum(tsr.getYear(), tsr.getMonth(), tsr.getHalf()));
		timesheet.setEmployeeId(tsr.getEmployeeId());
		List<Day> days = new ArrayList<Day>();
		Calendar cal = new GregorianCalendar(tsr.getYear(), tsr.getMonth(), 1);

		int start = 1;
		int end = 15;
		if (tsr.getHalf() == 2) {
			start = 16;
			end = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		}
		for (int i = start; i <= end; i++) {
			cal.set(tsr.getYear(), tsr.getMonth(), i);
			Day d = new Day();
			d.setDate(cal.getTime());
			d.setYear(tsr.getYear());
			d.setMonth(tsr.getMonth());
			d.setDay(i);
			d.getEntries().add(new Entry());
			days.add(d);

		}

		timesheet.setDays(days);
		
		return timesheet;
	}

	public List<Timesheet> getHistory(String json) {
		List<Timesheet> history = new ArrayList<>();
		Gson g = new GsonBuilder().create();
		LoginResponse lr = g.fromJson(json, LoginResponse.class);
		
		System.out.println("POST.History.EMPLOYEEID : " + lr.getEmployeeId());
		
		DBObject query = new BasicDBObject("timesheets.employeeId", lr.getEmployeeId());
		DBCursor cursor = collection.find(query);
		System.out.println("cycles containing timesheet with this employeeId : " + cursor.count());
		// add relevant cycles to list
		while (cursor.hasNext()) {
			
			BasicDBList tempList = (BasicDBList) cursor.next().get("timesheets");
			Iterator<Object> it = tempList.iterator();

			// add matching timesheets to list
			while(it.hasNext()) {
				DBObject dbo = (BasicDBObject) it.next();
				Timesheet t = new Timesheet();
				t.makePojoFromBson(dbo);
				if (t.getEmployeeId() != null) {
					System.out.println("found a timesheet with EmpID : " + t.getEmployeeId());
					if (t.getEmployeeId().toString().equals(lr.getEmployeeId().toString())) {
						history.add(t);
						System.out.println("ADDED MATCHING TIMESHEET TO HISTORY : " + t.getCycleNum());
					}
				}
			}
		}
		cursor.close();
		history = history.stream().sorted(new Comparator<Timesheet>() {
			public int compare(Timesheet t1, Timesheet t2) {
				if (t1.getCycleNum() < t2.getCycleNum()){
					return -1;
				} else if (t1.getCycleNum() > t2.getCycleNum()) {
					return 1;
				} else return 0;
			}
		}).collect(Collectors.toList());
		
		return history;
	}
	
}
