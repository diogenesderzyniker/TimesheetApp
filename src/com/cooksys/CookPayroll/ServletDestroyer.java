package com.cooksys.CookPayroll;

import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import spark.Spark;

@WebListener
public class ServletDestroyer implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

		Spark.stop();
		
		System.out.println("SESSION IS BEING DESTROYED!!@QWEJRLKJDLKGAF$#(UR#OJERQ");
		try {
			if (Bootstrap.getExService().awaitTermination(3000, TimeUnit.MILLISECONDS)) {
				System.out.println("Termination was awaited!!!!!");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Bootstrap.getExService().shutdownNow();
		
		try {
			Bootstrap.getMongoClient().close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		System.out.println("Threads are ANNIHILATED!!!");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("WELCOME TO THE NEW CONTEXT!");
	}

}
