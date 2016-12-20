package com.sciamlab.it.acquis.initdb;

import java.util.ArrayList;

public class MyThread extends Thread {

	private ArrayList<String> list;
	private String tokenmodel;
	private String posmodel;

	public MyThread(ArrayList<String> list, String tokenmodel, String posmodel){
		this.list = list;
		this.tokenmodel=tokenmodel;
		this.posmodel=posmodel;
	}
	
	public ArrayList<String> getList(){
		return list;
	}
	
	public String getPosmodel(){
		return posmodel;
	}
	
	public String getTokenmodel(){
		return tokenmodel;
	}
}