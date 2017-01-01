package com.sciamlab.it.acquis.initdb;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import com.sciamlab.common.nlp.EurovocThesaurus;
import com.sciamlab.it.acquis.xquery.FScrawling;
import com.sciamlab.it.acquis.xquery.XQmain;
import com.sciamlab.it.cata.feature.BasicFeatureExtractor;
import com.sciamlab.it.cata.feature.FeatureExtractor;
import opennlp.tools.util.InvalidFormatException;
import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Initdb {
	private ArrayList<String> files;
	private FScrawling fsc;
	private EurovocThesaurus thesaurusInfo;
	private Psql db;
	private static final int batchSize=200;

	//private static final int batchSize=150;
	public Initdb(String inputdir, Psql db, EurovocThesaurus thesaurusInfo) throws InvalidFormatException, IOException {
		this.fsc=new FScrawling(inputdir);
		this.files=fsc.crawl();
		this.thesaurusInfo=thesaurusInfo;
		this.db=db;
	}

	public ArrayList<String> getFiles() {
		return files;
	}

	public void createACQUIS3table() throws SQLException{
		PreparedStatement ps = db.getC().prepareStatement("CREATE TABLE acquisTableBasic"+
				"( doc text not null,"+
				"dcats text[],"+
				"features text[],"+
				"PRIMARY KEY(doc));");
		ps.executeUpdate();
		ps.close();
	}

	//-------------------------------------------------------------------
	//DOCFEATURES
	//-------------------------------------------------------------------
	//init index acquis id dcats features
	public void initIndexDOCFEATURES(FeatureExtractor fe) 
			throws Exception{
		int i=0;
		Connection c=this.db.getC();
		PreparedStatement insert = (c.prepareStatement("insert into acquisTableBasic values (?,?,?)"));
		for(String file:files){
			HashSet<String> dcats=XQmain.dcatsFile(file,"C:/Users/simone/Desktop", thesaurusInfo);

			if(dcats.size()!=0){			
				String corpus=XQmain.corpusFile(file, "C:/Users/simone/Desktop");
				List<String> arraypg=fe.execute(corpus);
				Array features = c.createArrayOf("text", arraypg.toArray());
				insert.setString(1, file);
				Array dcatspg = c.createArrayOf("text", dcats.toArray());
				insert.setArray(2, dcatspg);
				insert.setArray(3, features);
				insert.addBatch();
				i++;
				if(i%batchSize==0){
					insert.executeBatch();
					System.out.println("batch executed, batch size:"+batchSize);
				}
				insert.executeBatch();
			}
		}
	}

	public static void main (String[] args) throws Exception{


		System.out.print("Loading thesaurus information... ");
		File f=new File("src/main/resources/eurovoc/eurovoc_xml");
		EurovocThesaurus thesaurusInfo = new EurovocThesaurus.Builder(f, "it").build();
		System.out.println("done");
		System.out.println("domainMap: "+thesaurusInfo.domainMap.size());
		System.out.println("microThesaurusMap: "+thesaurusInfo.microThesaurusMap.size());
		System.out.println("conceptMap: "+thesaurusInfo.conceptMap.size());


		Psql db=new Psql("indexdb","postgres","postgres");
		Initdb initdb=new Initdb("C:/Users/simone/Desktop/it-acquis3", db, thesaurusInfo);
		initdb.createACQUIS3table();
		initdb.initIndexDOCFEATURES(new BasicFeatureExtractor());

	}
}

