package com.cooksys.CookPayroll.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.cooksys.CookPayroll.objects.Entry;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class EntryService {

	private final DB db;
	private final DBCollection collection;
	
	public EntryService(DB db){
		this.db = db;
		this.collection = db.getCollection("cycle");
	}
	
	public List<Entry> findAll(){
		List<Entry> todos = new ArrayList<Entry>();
		DBCursor dbObjects = collection.find();
		
		while (dbObjects.hasNext()){
			DBObject dbObject = dbObjects.next();
			todos.add(new Entry((BasicDBObject) dbObject));
		}
		
		return todos;
	}
	
	//TODO delete this
	public void createNewEntry(String body){
		///Entry entry = new Gson().fromJson(body, Entry.class);
		
//		collection.insert(new BasicDBObject("day", entry.getDay())
//			.append("billableHour",  entry.getBillableHours())
//			.append("nonBillableHours", entry.getNonBillableHours())
//			.append("ptoHours", entry.getPtoHours())
//			.append("clientNameId", entry.getClientNameId())
//			.append("projectNameId", entry.getProjectNameId())
//			.append("comments", entry.getComments())		
//			.append("createdOn", new Date()));
	}
	
	public Entry find(String id){
		return new Entry((BasicDBObject) collection.findOne(new BasicDBObject("_id", new ObjectId(id))));
	}
	
	public Entry update(String entryId, String body){
		Entry entry = new Gson().fromJson(body, Entry.class);
		
		collection.update(
				new BasicDBObject("_id",  new ObjectId(entryId)), 
				new BasicDBObject("$set", new BasicDBObject("day", entry.getDay())));
		
		return this.find(entryId);
	}
	
}
