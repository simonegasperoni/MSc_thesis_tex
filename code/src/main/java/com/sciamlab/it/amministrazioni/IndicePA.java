package com.sciamlab.it.amministrazioni;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import au.com.bytecode.opencsv.CSVReader;

// ammninistrazioni.txt @ 
// http://www.indicepa.gov.it/documentale/n-opendata.php
public class IndicePA{

	private Map<String,RecordPA> comuni;
	private Map<String,RecordPA> province;
	private Map<String,RecordPA> regioni;
	private Map<String,RecordPA> enti;
	private final String file ="src/main/resources/amministrazioni.csv";
	
	public IndicePA() throws IOException{
		System.out.println("Loading indicePA: 'pubbliche amministrazioni' database:");
		System.out.println("file: "+file);
		comuni=new HashMap<String,RecordPA>();
		province=new HashMap<String,RecordPA>();
		regioni=new HashMap<String,RecordPA>();
		enti=new HashMap<String,RecordPA>();
		CSVReader reader = new CSVReader(new FileReader(file), '\t');    
		String [] nextLine;
	    while ((nextLine = reader.readNext()) != null) {
	    	StringTokenizer tok = new StringTokenizer(nextLine[1], " ", true);
	    	String firsttoken=tok.nextToken().toLowerCase();
	    	if(firsttoken.equals("provincia")||firsttoken.equals("citta'")){
	    	   province.put(nextLine[0],new RecordPA(nextLine[0], firsttoken, nextLine[2], nextLine[6], nextLine[7]));
	    	}
	    	else if(firsttoken.equals("comune")){
		       comuni.put(nextLine[0],new RecordPA(nextLine[0], firsttoken, nextLine[2], nextLine[6], nextLine[7]));
		    }
	    	else if(firsttoken.equals("regione")){
			   regioni.put(nextLine[0],new RecordPA(nextLine[0], firsttoken, nextLine[2], nextLine[6], nextLine[7]));
			}
	    	else{
	    	   enti.put(nextLine[0],new RecordPA(nextLine[0], firsttoken, nextLine[2], nextLine[6], nextLine[7]));
	    	}
	    }
	    
	    System.out.println("province: "+province.size()+" items");
	    System.out.println("regioni: "+regioni.size()+" items");
	    System.out.println("comuni: "+comuni.size()+" items");
	    System.out.println("enti: "+enti.size()+" items");
	    
	    reader.close();
	}
	
	public Map<String, RecordPA> getComuni() {
		return comuni;
	}
	
	public Map<String, RecordPA> getProvince() {
		return province;
	}
	
	public Map<String, RecordPA> getRegioni() {
		return regioni;
	}
	
	public String getFile() {
		return file;
	}
	
	//insiemi di comuni province e città metropolitane di una regione
	public Set<RecordPA> getByRegione(String regione){
		Set<RecordPA> result=new HashSet<RecordPA>();
		for(String i:comuni.keySet()){
			RecordPA record=comuni.get(i);
			if(record.getRegione().equals(regione)){
				result.add(record);
			}
		}
		for(String i:province.keySet()){
			RecordPA record=province.get(i);
			if(record.getRegione().equals(regione)){
				result.add(record);
			}
		}
		return result;
	}
	
	//insiemi di comuni di una provincia
	public Set<RecordPA> getByProvincia(String provincia){
		Set<RecordPA> result=new HashSet<RecordPA>();
		for(String i:comuni.keySet()){
			RecordPA record=comuni.get(i);
			if(record.getProvincia().equals(provincia)){
				result.add(record);
			}
		}
		for(String i:province.keySet()){
			RecordPA record=province.get(i);
			if(record.getProvincia().equals(provincia)){
				result.add(record);
			}
		}
		return result;
	}
	
	public String getDescriptionByID(String id){
		return enti.get(id).getDescrizione();
	}
	
//	public static void main(String[] args) throws IOException{
//		IndicePA index=new IndicePA(); 
//		String x="VT";
//		Set<RecordPA> comuni=index.getByProvincia(x);
//		for(RecordPA rpa:comuni){
//			System.out.println(rpa);
//		}		
//	}
}

class RecordPA{
	private String id;
	private String descrizione;
	private String comune;
	private String provincia;
	private String regione;
	
	public RecordPA(String id, String descrizione, String comune, String provincia, String regione){
		this.id=id;
		this.descrizione=descrizione;
		this.comune=comune;
		this.provincia=provincia;
		this.regione=regione;
	}
	
	public String toString(){
		return 	this.id+":   "+
				this.descrizione+"   "+
				this.comune+"   "+
				this.provincia+"   "+
				this.regione;
	}
	
	public String getComune() {
		return comune;
	}
	
	public String getDescrizione() {
		return descrizione;
	}
	
	public String getProvincia() {
		return provincia;
	}
	
	public String getRegione() {
		return regione;
	}
	
	public String getId() {
		return id;
	}
	
}
