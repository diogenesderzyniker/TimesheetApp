package com.cooksys.CookPayroll;

import java.io.UnsupportedEncodingException;
import java.io.ObjectInputStream.GetField;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Hex;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.util.JSON;

public class DBSeed {

	static List<DBObject> dbObjects = null;
	
	public static void main(String[] args) {
		 
		try {
 
			seedEmployees();
			
//			MongoCredential cred = MongoCredential.createCredential("BartonFink", "cookpayroll", "password".toCharArray());
//	        
//	        MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 27017), Arrays.asList(cred));
//	        mongoClient.setWriteConcern(WriteConcern.SAFE);
//	        DB db = mongoClient.getDB("cookpayroll");
//			
//			DBCollection collection = db.getCollection("cycle");
//			
//			collection.drop();
//			collection.getCollection("cycle");
//			
//			seed();
//			
//			for(DBObject dbObject: dbObjects){
//				collection.insert(dbObject);
//			}
//			
// 
//			DBCursor cursorDoc = collection.find();
//			while (cursorDoc.hasNext()) {
//				System.out.println(cursorDoc.next());
//			}
 
			System.out.println("Done");
 
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
	
	private static void seed(){
		// convert JSON to DBObject directly
		dbObjects = new ArrayList<DBObject>();
		dbObjects.add( (DBObject) JSON.parse(""
				+ "{'cycleId' : 1, "
				+ "'month': 0, "
				+ "'cycle':1, "
				+ "'year':2015,"
				+ "'timesheets' : [{ 'employeeId': '1235678',"
									+ "'createdOn' : 'January',"
									+ "'validator' : 'Edith'}] "
									+ "}"));
		dbObjects.add( (DBObject) JSON.parse("{'cycleId' : 2, 'month':0, 'cycle':2, 'year':2015}"));
		dbObjects.add( (DBObject) JSON.parse("{'cycleId' : 3, 'month':1, 'cycle':1, 'year':2015}"));
		dbObjects.add( (DBObject) JSON.parse("{'cycleId' : 4, 'month':1, 'cycle':2, 'year':2015}"));
		dbObjects.add( (DBObject) JSON.parse("{'cycleId' : 5, 'month':2, 'cycle':1, 'year':2015}"));
		dbObjects.add( (DBObject) JSON.parse("{'cycleId' : 6, 'month':2, 'cycle':2, 'year':2015}"));
		dbObjects.add( (DBObject) JSON.parse("{'cycleId' : 7, 'month':3, 'cycle':1, 'year':2015}"));
		dbObjects.add( (DBObject) JSON.parse("{'cycleId' : 8, 'month':3, 'cycle':2, 'year':2015}"));
		dbObjects.add( (DBObject) JSON.parse("{'cycleId' : 9, 'month':4, 'cycle':1, 'year':2015}"));
		dbObjects.add( (DBObject) JSON.parse("{'cycleId' : 10, 'month':4, 'cycle':2, 'year':2015}"));
		dbObjects.add( (DBObject) JSON.parse("{'cycleId' : 11, 'month':5, 'cycle':1, 'year':2015}"));
		dbObjects.add( (DBObject) JSON.parse("{'cycleId' : 12, 'month':5, 'cycle':2, 'year':2015}"));
		dbObjects.add( (DBObject) JSON.parse("{'cycleId' : 13, 'month':6, 'cycle':1, 'year':2015}"));
		dbObjects.add( (DBObject) JSON.parse("{'cycleId' : 14, 'month':6, 'cycle':2, 'year':2015}"));
		dbObjects.add( (DBObject) JSON.parse("{'cycleId' : 15, 'month':7, 'cycle':1, 'year':2015}"));
		dbObjects.add( (DBObject) JSON.parse("{'cycleId' : 16, 'month':7, 'cycle':2, 'year':2015}"));
		dbObjects.add( (DBObject) JSON.parse("{'cycleId' : 17, 'month':8, 'cycle':1, 'year':2015}"));
		dbObjects.add( (DBObject) JSON.parse("{'cycleId' : 18, 'month':8, 'cycle':2, 'year':2015}"));
		dbObjects.add( (DBObject) JSON.parse("{'cycleId' : 19, 'month':9, 'cycle':1, 'year':2015}"));
		dbObjects.add( (DBObject) JSON.parse("{'cycleId' : 20, 'month':9, 'cycle':2, 'year':2015}"));
		dbObjects.add( (DBObject) JSON.parse("{'cycleId' : 21, 'month':10, 'cycle':1, 'year':2015}"));
		dbObjects.add( (DBObject) JSON.parse("{'cycleId' : 22, 'month':10, 'cycle':2, 'year':2015}"));
		dbObjects.add( (DBObject) JSON.parse("{'cycleId' : 23, 'month':11, 'cycle':1, 'year':2015}"));
		dbObjects.add( (DBObject) JSON.parse("{'cycleId' : 24, 'month':11, 'cycle':2, 'year':2015}"));
		
	}
	
	public static void seedEmployees() throws UnknownHostException {
		String defaultPWD = "bondstone";
		
		String hashedPass = new String(getHash(defaultPWD));
		
		List<DBObject> emps = new ArrayList<DBObject>();
		emps.add((DBObject) JSON.parse(""
				+ "{'username' : 'AAyyasamy@cooksys.com',"
				+ "'password' : '" + hashedPass + "'}"));
		emps.add((DBObject) JSON.parse(""
				+ "{'username' : 'AArmstrong@cooksys.com',"
				+ "'password' : '" + hashedPass + "'}"));
		emps.add((DBObject) JSON.parse(""
				+ "{'username' : 'AMayur@cooksys.com',"
				+ "'password' : '" + hashedPass + "'}"));
		emps.add((DBObject) JSON.parse(""
				+ "{'username' : 'FTineo@cooksys.com',"
				+ "'password' : '" + hashedPass + "'}"));
		emps.add((DBObject) JSON.parse(""
				+ "{'username' : 'JDurham@cooksys.com',"
				+ "'password' : '" + hashedPass + "'}"));
		emps.add((DBObject) JSON.parse(""
				+ "{'username' : 'KVanama@cooksys.com',"
				+ "'password' : '" + hashedPass + "'}"));
		emps.add((DBObject) JSON.parse(""
				+ "{'username' : 'MGarmon@cooksys.com',"
				+ "'password' : '" + hashedPass + "'}"));
		emps.add((DBObject) JSON.parse(""
				+ "{'username' : 'PBhattacharya@cooksys.com',"
				+ "'password' : '" + hashedPass + "'}"));
		emps.add((DBObject) JSON.parse(""
				+ "{'username' : 'PMehta@cooksys.com',"
				+ "'password' : '" + hashedPass + "'}"));
		emps.add((DBObject) JSON.parse(""
				+ "{'username' : 'RRaman@cooksys.com',"
				+ "'password' : '" + hashedPass + "'}"));
		emps.add((DBObject) JSON.parse(""
				+ "{'username' : 'RErler@cooksys.com',"
				+ "'password' : '" + hashedPass + "'}"));
		emps.add((DBObject) JSON.parse(""
				+ "{'username' : 'SGundamaraju@cooksys.com',"
				+ "'password' : '" + hashedPass + "'}"));
		emps.add((DBObject) JSON.parse(""
				+ "{'username' : 'SKuppagiri@cooksys.com',"
				+ "'password' : '" + hashedPass + "'}"));
		emps.add((DBObject) JSON.parse(""
				+ "{'username' : 'SDublin@cooksys.com',"
				+ "'password' : '" + hashedPass + "'}"));
		emps.add((DBObject) JSON.parse(""
				+ "{'username' : 'SSangelkar@cooksys.com',"
				+ "'password' : '" + hashedPass + "'}"));
		emps.add((DBObject) JSON.parse(""
				+ "{'username' : 'TKarmakar@cooksys.com',"
				+ "'password' : '" + hashedPass + "'}"));
		emps.add((DBObject) JSON.parse(""
				+ "{'username' : 'CThomas@cooksys.com',"
				+ "'password' : '" + hashedPass + "'}"));
		emps.add((DBObject) JSON.parse(""
				+ "{'username' : 'CNeal@cooksys.com',"
				+ "'password' : '" + hashedPass + "'}"));
		emps.add((DBObject) JSON.parse(""
				+ "{'username' : 'DCallis@cooksys.com',"
				+ "'password' : '" + hashedPass + "'}"));
		emps.add((DBObject) JSON.parse(""
				+ "{'username' : 'JDurbin@cooksys.com',"
				+ "'password' : '" + hashedPass + "'}"));
		emps.add((DBObject) JSON.parse(""
				+ "{'username' : 'RDixon@cooksys.com',"
				+ "'password' : '" + hashedPass + "'}"));
		emps.add((DBObject) JSON.parse(""
				+ "{'username' : 'RMorton@cooksys.com',"
				+ "'password' : '" + hashedPass + "'}"));
		emps.add((DBObject) JSON.parse(""
				+ "{'username' : 'WLeath@cooksys.com',"
				+ "'password' : '" + hashedPass + "'}"));
		emps.add((DBObject) JSON.parse(""
				+ "{'username' : 'YThompson@cooksys.com',"
				+ "'password' : '" + hashedPass + "'}"));
		emps.add((DBObject) JSON.parse(""
				+ "{'username' : 'SClements@cooksys.com',"
				+ "'password' : '" + hashedPass + "'}"));
				

		MongoCredential cred = MongoCredential.createCredential("BartonFink", "cookpayroll", "password".toCharArray());
        
        MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 27017), Arrays.asList(cred));
        mongoClient.setWriteConcern(WriteConcern.SAFE);
        DB db = mongoClient.getDB("cookpayroll");
		
		DBCollection collection = db.getCollection("Employees");
		collection.drop();
		
		collection.insert(emps);
	}
	
	private static char[] getHash(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.reset();
			return Hex.encodeHex(md.digest(password.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
}
