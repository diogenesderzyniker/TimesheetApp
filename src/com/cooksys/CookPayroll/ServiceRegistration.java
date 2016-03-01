package com.cooksys.CookPayroll;

import com.cooksys.CookPayroll.resources.CycleResource;
import com.cooksys.CookPayroll.resources.EmployeeResource;
import com.cooksys.CookPayroll.resources.EntryResource;
import com.cooksys.CookPayroll.resources.TimesheetResource;
import com.cooksys.CookPayroll.services.CycleService;
import com.cooksys.CookPayroll.services.EmployeeService;
import com.cooksys.CookPayroll.services.EntryService;
import com.cooksys.CookPayroll.services.TimesheetService;
import com.mongodb.DB;

public class ServiceRegistration {
		
	public ServiceRegistration(DB db) {
		
		// setting up service classes
		new EntryResource(new EntryService(db));
		new CycleResource(new CycleService(db));
		new TimesheetResource(new TimesheetService(db));
		new EmployeeResource(new EmployeeService(db));
	}
}
