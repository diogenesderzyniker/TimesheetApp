package com.cooksys.CookPayroll;

import static spark.Spark.post;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.codec.binary.Hex;
import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Spark;

import com.cooksys.CookPayroll.objects.LoginResponse;
import com.cooksys.CookPayroll.objects.Session;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class UserRegistration {
	
	private static final String API_CONTEXT = "/api/v1";
	
	Logger LOGR  = LoggerFactory.getLogger(UserRegistration.class);
	
	private DB db;
	
	public UserRegistration(DB db) {
		this.db = db;
	
		setupEndpoints();
	}
	
	private synchronized boolean logUserIn(String username, String password) {
		
//		MongoCredential cred = MongoCredential.createCredential(username, "cookpayroll", password.toCharArray());
		try {
			MongoClient client = Bootstrap.getMongoClient();
			
			System.out.println("Client? : " + client.toString());
			System.out.println("isAuthenticated: " + client.getDB("cookpayroll").isAuthenticated());

			return true;
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	private void setupEndpoints() {
		Bootstrap.getExService().execute(new Runnable() {
	
			/**
			 * This method is processed before all requests. It checks the access token against the SessionMap
			 * and determines whether the session has expired before allowing the request to proceed.
			 * Jesse Durham, 4/30/2015
			 */
			@Override
			public void run() {
				Spark.before((request, response) -> {
					Gson gson = new GsonBuilder().create();
					if (!(request.uri().contains("/login") || request.uri().contains("/register") || request.uri().contains("/img")
							|| request.uri().contains("/logout"))) {
						// use the below if statement to quickly turn accessToken checking on/off
						if (true) {
						UUID AToken = null;
						String jsonBody = request.body();
						LOGR.info("Request body is : " + jsonBody);
						String[] jsonArray = jsonBody.split(",");
						String jsonUUID = "";
						for(int i=0;i<jsonArray.length;i++){
							if (jsonArray[i].contains("accessToken")) {
								int start = jsonArray[i].indexOf(":");
								int end = jsonArray[i].contains("}") ? jsonArray[i].indexOf("}") : jsonArray[i].length();
								jsonUUID = jsonArray[i].substring(start + 1, end);
								break;
							}
						}
						try {
							LOGR.info("JSON to UUID-ify : " + jsonUUID);
							AToken = gson.fromJson(jsonUUID, UUID.class);
							if (AToken != null) {LOGR.info("ACCESS TOKEN : " + AToken.toString());}
						} catch(NullPointerException | IllegalArgumentException ex) {
							ex.printStackTrace();
							Spark.halt(401, "the UUID was null");
						}
						Set<UUID> accessTokens = SessionManager.getSessionMap().keySet();
						for (UUID s : accessTokens) {
							LOGR.info("Registered Session Access Token " + s.toString());
						}
						if (!(request.uri().contains("/login") || request.uri().contains("/register"))) {
							if (SessionManager.getSessionMap().containsKey(AToken)) {
								LOGR.info("found the UUID in sessionMap");
								Session session = SessionManager.getSessionMap().get(AToken);
								if (session.isExpired()) {
									LOGR.info("Session expired. Thread is : " + Thread.currentThread().getName());
									Spark.halt(401, "Your session has expired. Try logging in again.");
								} else {session.refresh();}
							} else {
								LOGR.info("No matching access token found");
								Spark.halt(401, "You cannot be authenticated");
							}
						}
						}
					}
				});
			}
		});
		
		Bootstrap.getExService().execute(new Runnable(){
			@Override
			public void run() {
				post(API_CONTEXT + "/login", "application/json", 
						(request,response) -> {
							
							LOGR.info("Request: " + request.body());
							Gson gson = new GsonBuilder().create();
							Credentials creds = gson.fromJson(request.body(), Credentials.class);
							creds.setPassword(new String(Hex.encodeHex(getHash(creds.getPassword()))));
							
							System.out.println(creds.getPassword());
							
							// verify the credentials
							if (db != null){
								List<DBObject> dbos = null;
								DBCollection coll = db.getCollection("Employees");
								dbos = coll.find().toArray();
								
								for (DBObject dbo : dbos) {
									System.out.println(dbo.get("username"));
									String username = dbo.get("username").toString();
									if (username.toUpperCase().equals(creds.getUsername().toUpperCase())) {
										// username exists. check password and authenticate
										if (dbo.get("password").equals(creds.getPassword())) {
											response.body(dbo.get("_id").toString());
											// password match found. login.
											if (logUserIn(creds.getUsername(), creds.getPassword())) {
												
												System.out.println("auth success");
												// create a login response object, and send it back in json form
												LoginResponse loginResponse = new LoginResponse();
												loginResponse.setLoginSuccess(true);
												loginResponse.setEmployeeId(new ObjectId(dbo.get("_id").toString()));
												if (dbo.get("role") != null) loginResponse.setRole(dbo.get("role").toString());
												else loginResponse.setRole("plebe");
												Session session = new Session(creds.getUsername());
												loginResponse.setAccessToken(session.getSessionToken());
												SessionManager.getSessionMap().put(session.getSessionToken(), session);
												
												LOGR.info("Response: " + response.body());
												return loginResponse;
											}
										}	
										
										System.out.println("login failed");
										System.out.println("LoginThread : " + Thread.currentThread().getName());
										
										// invalid username/password
										response.status(401);
										LOGR.info("Response: " + response.body());
										return false;
									}
								} 
								
								// no such user. need to register
								response.status(401);
								LOGR.info("Response: " + response.body());
								return false;
							} else {throw new NullPointerException();}
						}, new JsonTransformer());
			}
		});
		
		Bootstrap.getExService().execute(new Runnable(){
			@Override
			public void run() {
				post(API_CONTEXT + "/register", "application/json", 
						(request,response) -> {
							LOGR.info("Request: " + request.body());
							// need to pull password and username from body of request
							
							Gson gson = new GsonBuilder().create();
							Credentials creds = gson.fromJson(request.body(), Credentials.class);
							creds.setPassword(Hex.encodeHex(getHash(creds.getPassword())).toString());
							
							// get db
							if (db != null){
								DBCollection coll = db.getCollection("Employees");
								DBCursor cursor = coll.find();
								List<DBObject> dbolist = cursor.toArray();
								for (DBObject dbo : dbolist) {
									if (dbo.get("username").equals(creds.getUsername())) {
										System.out.println("user already exists!");
										LOGR.info("Response: " + response.body());
										response.status(406);
										response.body("u need to use the login screen, homie");
										return false;
									}
								}
							} else {throw new NullPointerException();}
							
							logUserIn(creds.getUsername(),creds.getPassword());
							DBCollection emps = db.getCollection("Employees");
							emps.insert(new BasicDBObject("username",creds.getUsername()).append("password", creds.getPassword()));
							
							DBObject dbo = new BasicDBObject("username",creds.getUsername());
							
							DBCursor cursor = emps.find(dbo);
							LoginResponse loginResponse = new LoginResponse();
							loginResponse.setLoginSuccess(true);
							loginResponse.setEmployeeId((new ObjectId(cursor.next().get("_id").toString())));
							loginResponse.setRole("none");
							cursor.close();
						
							Session session = new Session(creds.getUsername());
							loginResponse.setAccessToken(session.getSessionToken());
							SessionManager.getSessionMap().put(session.getSessionToken(), session);
							
							LOGR.info("Response: " + loginResponse.toString());
							return loginResponse;
						});
			}
		});
		
		Bootstrap.getExService().execute(new Runnable() {
			@Override
			public void run() {
				post(API_CONTEXT + "/logout/:accessToken", "application/json", 
						(request, response) -> {
							LOGR.info("Request: " + request.body());
							System.out.println("logout : " + db.command("logout").toString());
							
							String username = "";
							
							Collection<Session> sessionColl = SessionManager.getSessionMap().values();
							for (Session s : sessionColl) {
								if (s.getSessionToken() == request.attribute(":accessToken")) {
									username = s.getUsername();
								}
							}
							
							LOGR.info("Thread : " + Thread.currentThread().getName());
							LOGR.info("Thread State : " + Thread.currentThread().getState());
							LOGR.info("Active Thread Count : " + Thread.activeCount());
							
							SessionManager.getSessionMap().remove(username);
							
							LOGR.info("Response: User has been logged out. " + response.body());
							return true;
						});
			}
		});		
	}
	
	public void addEmployee(String username, String password) {
		DBCollection coll = db.getCollection("Employees");
		BasicDBObject dbo = new BasicDBObject();
		dbo.put("username", username);
		dbo.put("password", password);
		coll.insert(dbo);
	}
	
	public static byte[] getHash(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.reset();
			return md.digest(password.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public enum Permission {
		READWRITE, READONLY
	}
}
