package com.sciamlab.it.cata.evaluation;
import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.it.cata.classifier.PredictionEntry;
import com.sciamlab.it.cata.training.DatasetTrainingSource;

class Psql {
	Connection c = null;

	public Psql(String db, String id, String pwd) {
		
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection(db, id, pwd);
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

class Indexdb{
	Psql db=new Psql("jdbc:postgresql://localhost:5432/indexdb","postgres","postgres");
	PreparedStatement insert;
	String tabella;
	public Indexdb(String tabella) throws SQLException{
		this.tabella=tabella;
		Connection c=db.getC();
		insert = (c.prepareStatement("insert into "+tabella+" values (?,?,?,?,?,?)"));
	}
	
	public Psql getP(){
		return db;
	}
	
	public void createOdhTable() throws SQLException{
		PreparedStatement ps = db.getC().prepareStatement(""
				+ "DROP TABLE IF EXISTS "+tabella+";"
				+ "CREATE TABLE "+tabella+" "+
				"( id text not null,"+
				"category text[] not null,"+
				"title text not null,"+
				"description text,"+
				"tags text[] not null,"+
				"publisher text,"+
				"PRIMARY KEY(id));");
		ps.executeUpdate();
		ps.close();
	}
	
	public void addEntry(String id, Array category, String title, String desc, Array tags, String publisher) throws SQLException{
		insert.setString(1, id);
		insert.setArray(2, category);
		insert.setString(3, title);
		insert.setString(4, desc);
		insert.setArray(5, tags);
		insert.setString(6, publisher);
		
		insert.addBatch();
	}
	public void exBatch() throws SQLException{
		insert.executeBatch();
	}
	
}
public class TestSetFactory {
	
	public static void main(String[] arg) throws IOException, SQLException{
		
		
		List<String> t=new ArrayList<String>();
		t.add("ortofoto");
		DatasetTrainingSource ex=new DatasetTrainingSource(null, t);
		
		List<PredictionEntry> l=ex.getDatasetsBySOLr();
		Indexdb db=new Indexdb("odh22ortofoto");
		db.createOdhTable();
		
		
		Set<Theme> cats=new HashSet<Theme>();
		cats.add(Theme.TECH);
		Array cs=db.getP().getC().createArrayOf("text", cats.toArray());
		
		
		for(PredictionEntry p:l){
			Array ts=db.getP().getC().createArrayOf("text", p.tags.toArray());
			
			db.addEntry(p.id, cs, p.title, p.description, ts, p.publisher);
		}
		db.exBatch();
		
		
	}
}
