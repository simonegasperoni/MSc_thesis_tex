package com.sciamlab.it.databases.pg;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import javax.xml.xquery.XQException;
import com.google.common.util.concurrent.AtomicLongMap;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme;
import com.sciamlab.common.nlp.EurovocThesaurus;
import com.sciamlab.it.acquis.xquery.FScrawling;
import com.sciamlab.it.acquis.xquery.FileWithoutEurovocTagException;
import com.sciamlab.it.acquis.xquery.XQmain;
import com.sciamlab.it.corpus.features.FeaturesExtr;
import opennlp.tools.util.InvalidFormatException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Initdb {
	private ArrayList<String> files;
	private FScrawling fsc;
	private EurovocThesaurus thesaurusInfo;
	private Psql db;
	private FeaturesExtr fe;
	private static final int batchSize=200;
	private static final String[] dcats={"agri", "ener", "gove", "intr", "just", "econ", "soci", "educ", "tech", "tran", "envi", "regi", "heal"};
	
	//private static final int batchSize=150;
	public Initdb(String inputdir, Psql db, EurovocThesaurus thesaurusInfo) throws InvalidFormatException, IOException {
		this.fsc=new FScrawling(inputdir);
		this.files=fsc.crawl();
		this.thesaurusInfo=thesaurusInfo;
		this.db=db;
		this.fe=new FeaturesExtr();
		fe.ignoreSet(FeaturesExtr.ONLY_NAMES_VERBS_ADJECTIVES);
	}

	public ArrayList<String> getFiles() {
		return files;
	}
	
	public void createDOCDCATS() throws SQLException{
		PreparedStatement ps = db.getC().prepareStatement("CREATE TABLE docdcats ( doc text not null,"+
																					"dcats text[],"+
																					"PRIMARY KEY(doc));");
		ps.executeUpdate();
		ps.close();
	}
	
	public void createDOCFEATURES() throws SQLException{
		PreparedStatement ps = db.getC().prepareStatement("CREATE TABLE docfeatures ( doc text not null,"+
																					"features text[],"+
																					"PRIMARY KEY(doc));");
		ps.executeUpdate();
		ps.close();
	}
	
	//testare
	public void createACQUIS() throws SQLException{
		PreparedStatement ps = db.getC().prepareStatement("create table acquis as ( "
															+ "select docfeatures.doc, docdcats.dcats, docfeatures.features "
															+ "from docfeatures, docdcats "
															+ "where docfeatures.doc=docdcats.doc )");
		ps.executeUpdate();
		ps.close();
	}
	
	public void createDCATSCOUNTER() throws SQLException{
		PreparedStatement ps = db.getC().prepareStatement("create table dcatscounter ( "
																				+ "voc text not null,"
																				+ EUNamedAuthorityDataTheme.Theme.AGRI + " integer not null,"
																				+ EUNamedAuthorityDataTheme.Theme.ENER +" integer not null,"
																				+ EUNamedAuthorityDataTheme.Theme.GOVE +" integer not null,"
																				+ EUNamedAuthorityDataTheme.Theme.INTR +" integer not null,"
																				+ EUNamedAuthorityDataTheme.Theme.JUST +" integer not null,"
																				+ EUNamedAuthorityDataTheme.Theme.ECON +" integer not null,"
																				+ EUNamedAuthorityDataTheme.Theme.SOCI +" integer not null,"
																				+ EUNamedAuthorityDataTheme.Theme.EDUC +" integer not null,"
																				+ EUNamedAuthorityDataTheme.Theme.TECH +" integer not null,"
																				+ EUNamedAuthorityDataTheme.Theme.TRAN +" integer not null,"
																				+ EUNamedAuthorityDataTheme.Theme.ENVI +" integer not null,"
																				+ EUNamedAuthorityDataTheme.Theme.REGI +" integer not null,"
																				+ EUNamedAuthorityDataTheme.Theme.HEAL +" integer not null,"
																				+ "PRIMARY KEY(voc));");
		ps.executeUpdate();
		ps.close();
	}
	
	
	public static AtomicLongMap<EUNamedAuthorityDataTheme.Theme> returnMap(){
		AtomicLongMap<EUNamedAuthorityDataTheme.Theme> map=AtomicLongMap.create();
		for(EUNamedAuthorityDataTheme.Theme d: Arrays.asList(EUNamedAuthorityDataTheme.Theme.values())) map.addAndGet(d, 1);
		return map;
	}
	
	//collection documents for each DCAT
	public void compileDCATSCOUNTER() throws SQLException{
		
		Connection conn=this.db.getC();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT dcats, features FROM acquis");
		
		
		
		HashMap<String, AtomicLongMap<EUNamedAuthorityDataTheme.Theme>> mappe=new HashMap<String, AtomicLongMap<EUNamedAuthorityDataTheme.Theme>>();
		HashSet<String> index=new HashSet<String>();
		
		while(rs.next()){
			Array features=rs.getArray("features");
			Array dcats=rs.getArray("dcats");
			String[] array=(String[])features.getArray();
			String[] dca=(String[])dcats.getArray();
			
			for(String f:array){
				for(String d:dca){
					index.add(f);
					if(!mappe.containsKey(f)){
						mappe.put(f, Initdb.returnMap());
					}
					mappe.get(f).getAndIncrement(EUNamedAuthorityDataTheme.Theme.valueOf(d));
				}
			}
		}
		
		//{"AGRI", "ENER", "GOVE", "INTR", "JUST", "ECON", "SOCI", "EDUC", "TECH", "TRAN", "ENVI", "REGI", "HEAL"}
		Iterator<String> i=index.iterator();
		PreparedStatement insert = (conn.prepareStatement("insert into DCATSCOUNTER values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)"));

		while(i.hasNext()){
			String voc=i.next();
			insert.setString(1, voc);
			insert.setLong(2, mappe.get(voc).get(EUNamedAuthorityDataTheme.Theme.AGRI));
			insert.setLong(3, mappe.get(voc).get(EUNamedAuthorityDataTheme.Theme.ENER));
			insert.setLong(4, mappe.get(voc).get(EUNamedAuthorityDataTheme.Theme.GOVE));
			insert.setLong(5, mappe.get(voc).get(EUNamedAuthorityDataTheme.Theme.INTR));
			insert.setLong(6, mappe.get(voc).get(EUNamedAuthorityDataTheme.Theme.JUST));
			insert.setLong(7, mappe.get(voc).get(EUNamedAuthorityDataTheme.Theme.ECON));
			insert.setLong(8, mappe.get(voc).get(EUNamedAuthorityDataTheme.Theme.SOCI));
			insert.setLong(9, mappe.get(voc).get(EUNamedAuthorityDataTheme.Theme.EDUC));
			insert.setLong(10, mappe.get(voc).get(EUNamedAuthorityDataTheme.Theme.TECH));
			insert.setLong(11, mappe.get(voc).get(EUNamedAuthorityDataTheme.Theme.TRAN));
			insert.setLong(12, mappe.get(voc).get(EUNamedAuthorityDataTheme.Theme.ENVI));
			insert.setLong(13, mappe.get(voc).get(EUNamedAuthorityDataTheme.Theme.REGI));
			insert.setLong(14, mappe.get(voc).get(EUNamedAuthorityDataTheme.Theme.HEAL));
			insert.addBatch();
		}
		insert.executeBatch();
		
	}

	//-------------------------------------------------------------------
	//DOCDCATS
	//-------------------------------------------------------------------
	//init index acquis documents(id) - set<dcats categories>
	public void initIndexDOCDCATS() 
			throws SQLException, FileNotFoundException, XQException, FileWithoutEurovocTagException {

		Connection c=this.db.getC();
		PreparedStatement insert = (c.prepareStatement("insert into DOCDCATS values (?,?)"));
		for(String file:files){
			HashSet<String> dcats=XQmain.dcatsFile(file,"C:/Users/simone/Desktop", thesaurusInfo);
			//BPI (Batched Prepared Inserts) - prepared inserts, executed in batches of various length
			if(dcats.size()!=0){
				Array dcatspg = c.createArrayOf("text", dcats.toArray());
				insert.setString(1, file);
				insert.setArray(2, dcatspg);
				insert.addBatch();
			}
		}
		insert.executeBatch();
	}

	//-------------------------------------------------------------------
	//DOCFEATURES
	//-------------------------------------------------------------------
	//init index acquis documents(id) - set<features>
	public void initIndexDOCFEATURES(ArrayList<String> files, String namethread, FeaturesExtr fe) 
			throws InvalidFormatException, SQLException, IOException, XQException, FileWithoutEurovocTagException{
		
		int i=0;
		Connection c=this.db.getC();
		PreparedStatement insert = (c.prepareStatement("insert into DOCFEATURES values (?,?)"));
		for(String file:files){
			String corpus=XQmain.corpusFile(file, "C:/Users/simone/Desktop");
			//BPI (Batched Prepared Inserts) - prepared inserts, executed in batches of various length
			//System.out.println("namethread:"+namethread +" "+corpus);
			ArrayList<String> arraypg=fe.execute(corpus);
			Array features = c.createArrayOf("text", arraypg.toArray());
			insert.setString(1, file);
			insert.setArray(2, features);
			insert.addBatch();
			i++;
			if(i%batchSize==0){
				insert.executeBatch();
				System.out.println("namethread:"+namethread +" batch executed, batch size:"+batchSize);
			}
			
		}
		insert.executeBatch();

	}
	
	public void concurrentInitIndexDOCFEATURES(int CPUs){
		MyThread threads[]=new MyThread[CPUs];
		ArrayList<ArrayList<String>> l=new ArrayList<ArrayList<String>>(); 
		
		int partLength=(int)((files.size()/CPUs));
		ArrayList<String> aux=new ArrayList<String>();
		for(int i=1; i<files.size()+1; i++){
			aux.add(files.get(i-1));
			if(i%partLength==0){
				l.add(aux);
				aux=new ArrayList<String>();
			}
		}
		l.add(aux);
		
		System.out.println(files.size());
		System.out.println("----");
		
		for(int i=0; i<CPUs; i++){
			System.out.println(l.get(i).size());
		}

		
		for(int i=0; i<CPUs; i++){
			threads[i] = new MyThread(l.get(i),"models/it-token.bin","models/it-pos-maxent.bin"){
				public void run() { 
					try {
						FeaturesExtr fe=new FeaturesExtr(this.getTokenmodel(), this.getPosmodel());
						fe.ignoreSet(FeaturesExtr.ONLY_NAMES_VERBS_ADJECTIVES);
						initIndexDOCFEATURES(this.getList(),this.getName(), fe);
					} catch (InvalidFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (XQException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (FileWithoutEurovocTagException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
		}
		//execute
		for(int i=0; i<CPUs; i++) threads[i].start();
	}
	
	
	// true if vocabulary contains voc - BAYES classifier
		public boolean contains(String voc) throws SQLException{
			Connection conn=this.db.getC();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM dcatscounter WHERE voc='"+voc+"';");
			if(rs.next()) return true;
			return false;
				
		}
	
	// P(c) calculus - BAYES classifier
	public HashMap<String, Double> P_c() throws SQLException{
		Connection conn=this.db.getC();
		HashMap<String, Double> map=new HashMap<String, Double>();
		Statement stmt = conn.createStatement();
		
		double sum=0;
		for(String d:dcats){
			ResultSet rs = stmt.executeQuery("SELECT * FROM doccounter WHERE name='"+d.toUpperCase()+"';");
			rs.next();
			double aux=rs.getInt("count");
			map.put(d,aux);
			sum=sum+aux;
		}
		
		for(String d:dcats){
			map.put(d,map.get(d)/sum);
		}
		
		return map;
	}
	
	// terms counter per category DCAT - BAYES classifier
	public HashMap<String, Integer> sums() throws SQLException, InvalidFormatException, IOException{
		//String corpus="";
		//AGRI, ENER, GOVE, INTR, JUST, ECON, SOCI, EDUC, TECH, TRAN, ENVI, REGI, HEAL
		Connection conn=this.db.getC();
		HashMap<String, Integer>sums=new HashMap<String, Integer>();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT "
										+ "sum(agri) as agri, "
										+ "sum(ener) as ener, "
										+ "sum(gove) as gove, "
										+ "sum(intr) as intr, "
										+ "sum(just) as just, "
										+ "sum(econ) as econ, "	
										+ "sum(soci) as soci, "
										+ "sum(educ) as educ, "
										+ "sum(tech) as tech, "
										+ "sum(tran) as tran, "
										+ "sum(envi) as envi, "
										+ "sum(regi) as regi, "
										+ "sum(heal) as heal "
										+ "FROM dcatscounter");
		rs.next();
		sums.put("agri",rs.getInt("agri"));
		sums.put("ener",rs.getInt("ener"));
		sums.put("gove",rs.getInt("gove"));
		sums.put("intr",rs.getInt("intr"));
		sums.put("just",rs.getInt("just"));
		sums.put("econ",rs.getInt("econ"));
		sums.put("soci",rs.getInt("soci"));
		sums.put("educ",rs.getInt("educ"));
		sums.put("tech",rs.getInt("tech"));
		sums.put("tran",rs.getInt("tran"));
		sums.put("envi",rs.getInt("envi"));
		sums.put("regi",rs.getInt("regi"));
		sums.put("heal",rs.getInt("heal"));
		return sums;
	}
	
	// term frequency per category - BAYES classifier
	public HashMap<String, Integer> tf(String voc) throws SQLException, InvalidFormatException, IOException{
		//String corpus="";
		//AGRI, ENER, GOVE, INTR, JUST, ECON, SOCI, EDUC, TECH, TRAN, ENVI, REGI, HEAL
		Connection conn=this.db.getC();
		HashMap<String, Integer>tf=new HashMap<String, Integer>();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * from dcatscounter where voc='"+voc+"';");
							
		rs.next();
		tf.put("agri",rs.getInt("agri"));
		tf.put("ener",rs.getInt("ener"));
		tf.put("gove",rs.getInt("gove"));
		tf.put("intr",rs.getInt("intr"));
		tf.put("just",rs.getInt("just"));
		tf.put("econ",rs.getInt("econ"));
		tf.put("soci",rs.getInt("soci"));
		tf.put("educ",rs.getInt("educ"));
		tf.put("tech",rs.getInt("tech"));
		tf.put("tran",rs.getInt("tran"));
		tf.put("envi",rs.getInt("envi"));
		tf.put("regi",rs.getInt("regi"));
		tf.put("heal",rs.getInt("heal"));
		
		return tf;
	}
	
	public HashMap<String, Double> bayesPrediction(String corpus) throws InvalidFormatException, IOException, SQLException{
		ArrayList<String> features=new ArrayList<String>();
		ArrayList<String> features2=fe.execute(corpus);
		for(String f:features2){
			if(contains(f)) features.add(f);
		}
		
		
		HashMap<String, Integer> sums=sums();
		
		
		HashMap<String, Double> pc=P_c();
		
		for(String f:features){
			System.out.println(f);
			HashMap<String, Integer> tf=tf(f);
			for(String d:dcats){
				pc.put(d,pc.get(d)*(tf.get(d))/(sums.get(d)));
			}
			
		}
		return pc;
	}
	
	
	public static void main (String[] args) 
			throws InvalidFormatException, IOException, SQLException, XQException, FileWithoutEurovocTagException{
	
		
		System.out.print("Loading thesaurus information... ");
		File f=new File("src/main/resources/eurovoc/eurovoc_xml");
		EurovocThesaurus thesaurusInfo = new EurovocThesaurus.Builder(f, "it").build();
		System.out.println("done");
		System.out.println("domainMap: "+thesaurusInfo.domainMap.size());
		System.out.println("microThesaurusMap: "+thesaurusInfo.microThesaurusMap.size());
		System.out.println("conceptMap: "+thesaurusInfo.conceptMap.size());
	
		
		Psql db=new Psql("indexdb","postgres","postgres");
		Initdb initdb=new Initdb("C:/Users/simone/Desktop/it-acquis3", db, thesaurusInfo);
		//initdb2.concurrentInitIndexDOCFEATURES(8);
		
	
		HashMap<String, Double> res=initdb.bayesPrediction(" Degenza per disciplina osp. Regina Apostolorum 2007 "
				+ "Giornate di degenza per disciplina nell'Ospedale Regina Apostolorum di Albano Laziale - 2007. "
				+ "Questo documento parla di sanità e degenza");
		double max=0.0;
		for(String d:dcats){
			double current=res.get(d);
			if (current>max) max=current;  
		}
		System.out.println(max);
	
	}
}

