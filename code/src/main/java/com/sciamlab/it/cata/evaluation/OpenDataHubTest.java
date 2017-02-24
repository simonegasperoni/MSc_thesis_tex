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
import com.sciamlab.it.cata.classifier.Classifier;
import com.sciamlab.it.cata.classifier.PredictionEntry;
import com.sciamlab.it.cata.feature.StemFeatureExtractor;
import com.sciamlab.it.cata.training.TrainingSet;


public class OpenDataHubTest implements Evaluator {

	private Psql psql;
	private List<OdhEntry> odh;
	private TrainingSet ts;
	private Classifier classifier;


	public OpenDataHubTest(TrainingSet ts){
		this.psql=new Psql("indexdb","postgres","postgres");
		this.odh=new ArrayList<OdhEntry>();
		this.ts=ts;
		classifier=null;
	}

	public void loadData() throws SQLException{
		PreparedStatement stmt = psql.getC().prepareStatement("SELECT * from odh2lecce");
		//logger.info(stmt.toString());
		stmt.execute();
		ResultSet rs = stmt.getResultSet();

		while(rs.next()){
			String id = rs.getString("id");
			String title = rs.getString("title");
			String description = rs.getString("description");
			Set<Theme> categories = new HashSet<Theme>();
			Set<String> tags = new HashSet<String>();
			for(String tag : Arrays.asList((String[])rs.getArray("tags").getArray()))
				tags.add(tag);
			for(String cat : Arrays.asList((String[])rs.getArray("category").getArray()))
				categories.add(Theme.valueOf(cat));			
			odh.add(new OdhEntry(id,title,description,tags,categories));	
		}

	}


	@Override
	public void evaluate(Class<? extends Classifier> clazz) throws Exception {
		this.classifier = Classifier.Factory.build(clazz, ts);
		this.loadData();
		//Printlog pl=new Printlog("C:/Users/simone/Desktop/validationlog80000");
		int i=0;
		for(OdhEntry odhe:this.odh){
			//pl.printDataset(odhe);
			
			
			Theme t=classifier.predictFirst(
					new PredictionEntry.Builder(odhe.getDescription())
					.tag(odhe.getTags())
					.title(odhe.getTitle())
					.build(), new StemFeatureExtractor());
			if(odhe.getCategories().contains(t)) i++;
			//pl.printTheme(t);
		}
		System.out.println(new Double(i)/new Double(odh.size()));
		//pl.close();

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
}

//class Db{
//	Psql db=new Psql("indexdb","postgres","postgres");
//	PreparedStatement insert;
//	public Db() throws SQLException{
//		Connection c=db.getC();
//		insert = (c.prepareStatement("insert into odh2lecce values (?,?,?,?,?)"));
//	}
//	
//	public Psql getPsql(){ return db; }
//
//	public void createOdhTable() throws SQLException{
//		PreparedStatement ps = db.getC().prepareStatement(""
//				+ "DROP TABLE IF EXISTS odh2lecce;"
//				+ "CREATE TABLE odh2lecce"+
//				"( id text not null,"+
//				"category text[] not null,"+
//				"title text not null,"+
//				"description text,"+
//				"tags text[] not null,"+
//				"PRIMARY KEY(id));");
//		ps.executeUpdate();
//		ps.close();
//	}
//
//	public void addEntry(String id, Array category, String title, String desc, Array tags) throws SQLException{
//		insert.setString(1, id);
//		insert.setArray(2, category);
//		insert.setString(3, title);
//		insert.setString(4, desc);
//		insert.setArray(5, tags);
//		insert.addBatch();
//	}
//	public void exBatch() throws SQLException{
//		insert.executeBatch();
//	}
//}