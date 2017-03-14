package com.sciamlab.it.cata.retraining;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.it.eurovoc.JEVoc;

public class Retrainer {

	public static List<SOLrQuery> getQueries() throws FileNotFoundException, IOException, ParseException {
		JEVoc ev=new JEVoc();
		List<SOLrQuery> queries=new ArrayList<SOLrQuery>();
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader("src/main/resources/retrains.json"));
        JSONObject json = (JSONObject) obj;
        JSONArray a = (JSONArray) json.get("retrains");
        for(int i=0; i<a.size(); i++){
        	
        	
        	JSONObject current=(JSONObject) a.get(i);
        	String p=(String)current.get("publisher");
        	HashSet<String> c=new HashSet<String>();
        	List<String> t=new ArrayList<String>();
        	
        	JSONArray tags = (JSONArray) current.get("tags");
        	for(int i1=0; i1<tags.size(); i1++){
        		t.add((String)tags.get(i1).toString().replaceAll(" ", "%20"));
        	}
        	
        	JSONArray categories = (JSONArray) current.get("categories");
        	for(int i2=0; i2<categories.size(); i2++){
        		String v=(String)categories.get(i2);
        		c.add(v);
        	}
        	
        	queries.add(new SOLrQuery(p,t,ev.returnsDCATCAT(c)));
        	
        }
        return queries;
	}
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException{
		System.out.println(Retrainer.getQueries());
	}
}
