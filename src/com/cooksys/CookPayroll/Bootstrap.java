package com.cooksys.CookPayroll;

import static spark.SparkBase.setIpAddress;
import static spark.SparkBase.setPort;
import static spark.SparkBase.staticFileLocation;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

public class Bootstrap {
	final static Logger LOGR = LoggerFactory.getLogger(Bootstrap.class);
	
	private static final String IP_ADDRESS = System.getenv("MONGOD_HOST_IP") != null ? System.getenv("MONGOD_HOST_IP") : "localhost";
    private static final int PORT = System.getenv("SPARK_PORT") != null ? Integer.parseInt(System.getenv("SPARK_PORT")) : 8080;
    private static MongoClient client = null;

    private static ExecutorService exService = Executors.newCachedThreadPool();
    
    public static void main(String[] args) throws Exception {
        setIpAddress(IP_ADDRESS);
        setPort(PORT);
        staticFileLocation("/public");
        DB db = mongo();
        
		new UserRegistration(db);
        new ServiceRegistration(db);

    }

    // This method will login as admin so that we have permission
    // to add new users to the DB via java driver
    public static DB mongo() throws Exception {
        String host = System.getenv("MONGOD_HOST_IP");
//        String host = "192.168.1.129";
        
        String adminDB = "admin";
        String adminpwd = "password";
        String adminname = "superadmin";
        
        if (host == null) {
        	System.out.println("host was null");
            MongoClient mongoClient = new MongoClient("localhost");
            System.out.println("failed to find environment var: MONGOD_HOST_IP; using localhost");
            return mongoClient.getDB("mydb");
        }
        int port = Integer.parseInt(System.getenv("MONGO_DB_PORT"));
        LOGR.info("PORT is : " + port);
        String dbname = System.getenv("MONGO_DB_APP_NAME");
        System.out.println("db name is: " + dbname);
        LOGR.info("DB name is : " + dbname);
        
        MongoCredential cred = MongoCredential.createCredential(adminname, adminDB, adminpwd.toCharArray());
        
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder().connectionsPerHost(100).build();
        
        MongoClient mongoClient = new MongoClient(new ServerAddress(host, port), Arrays.asList(cred), mongoClientOptions);
        mongoClient.setWriteConcern(WriteConcern.ACKNOWLEDGED);
        
        client = mongoClient;
        
        DB db = mongoClient.getDB(dbname);
        return db;
    }
    
    public static MongoClient getMongoClient() throws UnknownHostException {
    	return client;
//    	return new MongoClient(new ServerAddress(IP_ADDRESS, PORT), Arrays.asList(cred));
    }

	public static ExecutorService getExService() {
		return exService;
	}

	public static void setExService(ExecutorService exService) {
		Bootstrap.exService = exService;
	}
}
