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
	private String table;


	public OpenDataHubTest(TrainingSet ts, String table){
		this.table=table;
		this.psql=new Psql("indexdb","postgres","postgres");
		this.odh=new ArrayList<OdhEntry>();
		this.ts=ts;
		classifier=null;
	}

	private void loadData() throws SQLException{
		PreparedStatement stmt = psql.getC().prepareStatement("SELECT * from "+table);
		//logger.info(stmt.toString());
		stmt.execute();
		ResultSet rs = stmt.getResultSet();

		while(rs.next()){
			String id = rs.getString("id");
			String title = rs.getString("title");
			//String publisher = rs.getString("publisher");
			String description = rs.getString("description");
			Set<Theme> categories = new HashSet<Theme>();
			Set<String> tags = new HashSet<String>();
			for(String tag : Arrays.asList((String[])rs.getArray("tags").getArray()))
				tags.add(tag);
			for(String cat : Arrays.asList((String[])rs.getArray("category").getArray()))
				categories.add(Theme.valueOf(cat));			
			odh.add(new OdhEntry(id,title,description,tags,categories, "lecce"));	
		}

	}


	@Override
	public void evaluate(Class<? extends Classifier> clazz) throws Exception {
		this.classifier = Classifier.Factory.build(clazz, ts);
		this.loadData();
//		Db db=new Db();
//		db.createOdhTable();
//		Printlog pl=new Printlog("C:/Users/simone/Desktop/validationLAZIO");
		int i=0;
		for(OdhEntry odhe:this.odh){
//			pl.printDataset(odhe);
			
			
			Theme t=classifier.predictFirst(
					new PredictionEntry.Builder(odhe.getDescription())
					.tag(odhe.getTags())
					.title(odhe.getTitle())
					.build(), new StemFeatureExtractor());
			if(odhe.getCategories().contains(t)) i++;

//			pl.printClassifiedEntry(classifier.predict(new PredictionEntry.Builder(odhe.getDescription()).tag(odhe.getTags())
//					.title(odhe.getTitle()).build(), new StemFeatureExtractor()));
//			pl.printTheme(t);
//			odhe.getCategories().add(t);
//			db.addEntry(odhe.getId(), odhe.getCategories(), odhe.getTitle(), odhe.getDescription(), odhe.getTags(), odhe.getPublisher());
		}
		System.out.println(new Double(i)/new Double(odh.size()));
//		pl.close();
//		db.exBatch();

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
//		insert = (c.prepareStatement("insert into odh22lazio values (?,?,?,?,?,?)"));
//	}
//	
//	public Psql getPsql(){ return db; }
//
//	public void createOdhTable() throws SQLException{
//		PreparedStatement ps = db.getC().prepareStatement(""
//				+ "DROP TABLE IF EXISTS odh22lazio;"
//				+ "CREATE TABLE odh22lazio"+
//				"( id text not null,"+
//				"category text[] not null,"+
//				"title text not null,"+
//				"description text,"+
//				"tags text[] not null,"+
//				"publisher text,"+
//				"PRIMARY KEY(id));");
//		ps.executeUpdate();
//		ps.close();
//	}
//
//	public void addEntry(String id, Set<Theme> set, String title, String desc, Set<String> set2, String publisher) throws SQLException{
//		insert.setString(1, id);
//		insert.setArray(2, db.getC().createArrayOf("text", set.toArray()));
//		insert.setString(3, title);
//		insert.setString(4, desc);
//		insert.setArray(5, db.getC().createArrayOf("text", set2.toArray()));
//		insert.setString(6, publisher);
//		
//		insert.addBatch();
//	}
//	public void exBatch() throws SQLException{
//		insert.executeBatch();
//	}
//}