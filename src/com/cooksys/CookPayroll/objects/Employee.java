package com.cooksys.CookPayroll.objects;

import java.util.Date;

import org.apache.commons.codec.binary.Hex;
import org.bson.types.ObjectId;

import com.cooksys.CookPayroll.UserRegistration;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@SuppressWarnings("unused")
public class Employee {

	private ObjectId _id;
	private String username;
	private String password;
	private String employeeName;
	private String dob;
	private String address;
	private String city;
	private String state;
	private int zipCode;
	private String phoneNumber;
	private String position;
	private String role;
	
	public Employee() {
	}
	
	public Employee(BasicDBObject dbObject) {
		this._id = new ObjectId(dbObject.get("_id").toString());
		this.username = dbObject.getString("username");
		this.password = dbObject.getString("password");
		if (dbObject.containsField("employeeName")) this.employeeName = dbObject.getString("employeeName");
		if (dbObject.containsField("DOB")) this.dob = dbObject.getString("DOB");
		if (dbObject.containsField("address")) this.address = dbObject.getString("address");
		if (dbObject.containsField("city")) this.city = dbObject.getString("city");
		if (dbObject.containsField("state")) this.state = dbObject.getString("state");
		if (dbObject.containsField("zipCode")) this.zipCode = dbObject.getInt("zipCode");
		if (dbObject.containsField("phoneNumber")) this.phoneNumber = dbObject.getString("phoneNumber");
		if (dbObject.containsField("position")) this.position = dbObject.getString("position");
	}

	public Employee makePojoFromBson(DBObject bson){
		BasicDBObject b = (BasicDBObject) bson;
		this._id = (ObjectId) b.getObjectId("_id");
		this.username = b.getString("username");
		this.password = b.getString("password");
		if (b.containsField("employeeName")) this.employeeName = b.getString("employeeName");
		if (b.containsField("DOB")) this.dob = b.getString("DOB");
		if (b.containsField("address")) this.address = b.getString("address");
		if (b.containsField("city")) this.city = b.getString("city");
		if (b.containsField("state")) this.state = b.getString("state");
		if (b.containsField("zipCode")) this.zipCode = b.getInt("zipCode");
		if (b.containsField("phoneNumber")) this.phoneNumber = b.getString("phoneNumber");
		if (b.containsField("position")) this.position = b.getString("position");
		return this;
	}
	
	public DBObject getEditableBSON() {
		DBObject dbo = new BasicDBObject();
		
		if (this.employeeName != null) dbo.put("employeeName", this.employeeName);
		if (this.dob != null) dbo.put("DOB", this.dob);
		if (this.address != null) dbo.put("address", this.address);
		if (this.city != null) dbo.put("city", this.city);
		if (this.state != null) dbo.put("state", this.state);
		if (this.zipCode != 0) dbo.put("zipCode", this.zipCode);
		if (this.phoneNumber != null) dbo.put("phoneNumber", this.phoneNumber);
		if (this.position != null) dbo.put("position", this.position);
		if (this.password != null) dbo.put("password", this.password);
		return dbo;
	}
	
	public DBObject bsonFromPojo() {
		BasicDBObject document = new BasicDBObject();

		document.put("_id", this._id);
		document.put("username", this.username);
		document.put("password", this.password);
		if (this.employeeName != null) document.put("employeeName", this.employeeName);
		if (this.dob != null) document.put("DOB", this.dob);
		if (this.address != null) document.put("address", this.address);
		if (this.city != null) document.put("city", this.city);
		if (this.state != null) document.put("state", this.state);
		if (this.zipCode != 0) document.put("zipCode", this.zipCode);
		if (this.phoneNumber != null) document.put("phoneNumber", this.phoneNumber);
		if (this.position != null) document.put("position", this.position);
		return document;
	}
	
	public void hashPass() {
		if (this.password != null) {
			this.password = Hex.encodeHexString(UserRegistration.getHash(this.password));
		}
	}
	
	public ObjectId getId() {
		return _id;
	}

	public void setId(ObjectId id) {
		this._id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
