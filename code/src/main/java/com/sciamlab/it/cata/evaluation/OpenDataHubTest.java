package com.sciamlab.it.cata.evaluation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.it.acquis.initdb.Psql;
import com.sciamlab.it.cata.classifier.ClassifiedEntry;
import com.sciamlab.it.cata.classifier.Classifier;
import com.sciamlab.it.cata.classifier.PredictionEntry;
import com.sciamlab.it.cata.feature.BasicFeatureExtractor;
import com.sciamlab.it.cata.training.TrainingSet;

class OdhEntry{
	private String title;
	private String description;
	private Set<String> tags;
	private Theme category;
	public OdhEntry(String title, String description, Set<String> tags, Theme category){
		this.description=description;
		this.title=title;
		this.tags=tags;
		this.category=category;
	}
	
	public Theme getCategory() {
		return category;
	}
	public String getDescription() {
		return description;
	}
	public Set<String> getTags() {
		return tags;
	}
	public String getTitle() {
		return title;
	}
}

public class OpenDataHubTest implements Evaluator {
	
	private Psql psql;
	private List<OdhEntry> odh;
	private TrainingSet ts;
	
	public OpenDataHubTest(TrainingSet ts){
		this.psql=new Psql("indexdb","postgres","postgres");
		this.odh=new ArrayList<OdhEntry>();
		this.ts=ts;
	}
	
	public void loadData() throws SQLException{
		PreparedStatement stmt = psql.getC().prepareStatement("SELECT * from odh");
		//logger.info(stmt.toString());
		stmt.execute();
		ResultSet rs = stmt.getResultSet();
		
		while(rs.next()){
			String title = rs.getString("title");
			String description = rs.getString("description");
			
			Set<String> tags = new HashSet<String>();
			for(String tag : Arrays.asList((String[])rs.getArray("tags").getArray()))
				tags.add(tag);
		
			Theme t=Theme.valueOf(rs.getString("category"));
			
			odh.add(new OdhEntry(title,description,tags,t));	
		}
		
	}
	

	@Override
	public void evaluate(Class<? extends Classifier> clazz) throws Exception {
		// TODO Auto-generated method stub
		
		Classifier classifier=Classifier.Factory.build(clazz, ts);
		
		double i=0.0;
		for(OdhEntry odhe:this.odh){
			System.out.println("------------------------");
			System.out.println(odhe.getTitle());
			System.out.println(odhe.getDescription());
			System.out.println(odhe.getTags());
			
			ClassifiedEntry ce=classifier.predict(new PredictionEntry(odhe.getTitle(), odhe.getDescription(), odhe.getTags()), 0.0, new BasicFeatureExtractor());
			
			System.out.println(ce.getCategories());
			System.out.println(odhe.getCategory());
			System.out.println("------------------------");
			
			if(ce.getCategories().keySet().contains(odhe.getCategory())) i++;
		}
		System.out.println(new Double(i/odh.size()));
		
	}

	@Override
	public double getPrecision() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getRecall() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getAccuracy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getFmeasure() {
		// TODO Auto-generated method stub
		return 0;
	}
	
//	public static void main(String[] args) throws SQLException {
//		// TODO Auto-generated method stub
//		OpenDataHubTest odh=new OpenDataHubTest();
//		odh.loadData();
//	}
}
