package com.cooksys.CookPayroll.services;

import java.util.ArrayList;
import java.util.List;

import com.cooksys.CookPayroll.objects.Employee;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

public class EmployeeService {

	private DB db;
	private DBCollection dbColl = null;
	
	public EmployeeService(DB db) {
		this.db = db;
		if (dbColl == null) {
			// get Employee collection snapshot
			db.getCollection("Employee");
		}
	}
	
	/**
	 * Gets Employee collection 
	 * @return Returns the Employees collection
	 */
	public List<Employee> getEmployees() {
		List<Employee> empList = new ArrayList<>();
		
		dbColl = db.getCollection("Employees");
		DBCursor cursor = dbColl.find();
		
		while (cursor.hasNext()) {
			empList.add(new Employee((BasicDBObject) cursor.next()));
		}
		
		System.out.println("There are " + dbColl.getCount() + "employees in Employees Collection");
		
		return empList;
	}
	
	public Employee getEmployeeByID(String empID) {
		DBCollection coll = db.getCollection("Employees");
		Gson g = new GsonBuilder().create();
		Employee e = g.fromJson(empID, Employee.class);
		System.out.println(e.getId());
		
		DBObject query = new BasicDBObject("_id", e.getId());
		DBCursor cursor = coll.find(query);
		
		e = new Employee((BasicDBObject) cursor.next());
		
		return e;
	}
	
	// method under construction
	public void updateEmployee(String jsonEmp) {
		Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		Employee emp = g.fromJson(jsonEmp, Employee.class);
		// if this is a password change request, hash the password
		emp.hashPass();
		
		System.out.println("update BSON : " + emp.bsonFromPojo());
		
		DBObject match = new BasicDBObject("_id", emp.getId());
		DBObject update = new BasicDBObject("$set", emp.getEditableBSON());
		
		System.out.println("MATCH OBJECT " + match.toString());
		System.out.println("UPDATE OBJECT " + update.toString());
		
		dbColl = db.getCollection("Employees");
		WriteResult wr = dbColl.update(match, update);
		System.out.println("Employee Update Completed : " + wr.toString());
	}
}
