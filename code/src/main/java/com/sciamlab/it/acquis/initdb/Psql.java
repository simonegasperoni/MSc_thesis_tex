package com.sciamlab.it.acquis.initdb;
import java.sql.Connection;
import java.sql.DriverManager;

public class Psql {

	Connection c = null;

	public Psql(String db, String id, String pwd) {
		
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+db+"", id, pwd);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully");
	}

	public Connection getC() {
		
		return c;
	}
}