package com.sciamlab.it.acquis.initdb;

import java.util.ArrayList;

import com.sciamlab.it.cata.feature.FeatureExtractor;

public class MyThread extends Thread {

	private ArrayList<String> list;
	private String tokenmodel;
	private String posmodel;
	private FeatureExtractor fe;

	public MyThread(ArrayList<String> list, FeatureExtractor fe, String tokenmodel, String posmodel){
		this.list = list;
		this.tokenmodel=tokenmodel;
		this.posmodel=posmodel;
		this.fe=fe;
	}
	
	public ArrayList<String> getList(){
		return list;
	}
	
	public FeatureExtractor getExtractor() {
		return fe;
	}
	
	public String getPosmodel(){
		return posmodel;
	}
	
	public String getTokenmodel(){
		return tokenmodel;
	}
}