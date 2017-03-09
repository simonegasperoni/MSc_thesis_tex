package com.sciamlab.it.amministrazioni;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.sciamlab.it.cata.classifier.PredictionEntry;

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
	    System.out.println("Loading IndicePA...");
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
	private Set<RecordPA> getByRegione(String regione){
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
	private Set<RecordPA> getByProvincia(String provincia){
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
	
	public PredictionEntry filterPA(PredictionEntry pe){
		Set<RecordPA> recordpa=new HashSet<RecordPA>();
		Set<String> stop=new HashSet<String>();
		if(comuni.containsKey(pe.publisher)){
			recordpa=new HashSet<RecordPA>();
			recordpa.add(comuni.get(pe.publisher));
		}
		else if(regioni.containsKey(pe.publisher)){
			recordpa=getByRegione(regioni.get(pe.publisher).getRegione());
		}
		else if(province.containsKey(pe.publisher)){
			recordpa=getByProvincia(province.get(pe.publisher).getProvincia());
		}
		

		
		for(RecordPA rpa:recordpa){
			String c=rpa.getComune().toLowerCase();
			stop.add(c);
			stop.add(c.replaceAll(" ",""));
			stop.add(c.replaceAll("\\P{L}+", ""));
			
		}
		String ti="";
		String de="";
		Set<String> ta=new HashSet<String>();
		if(pe.title!=null) ti=pe.title.replaceAll("\\P{L}+", " ").toLowerCase();
		if(pe.description!=null) de=pe.description.replaceAll("\\P{L}+", " ").toLowerCase();
		if(pe.tags!=null) ta=pe.tags;
		
		
		for(String stopw:stop){
			ti=ti.replaceAll(" "+stopw+" "," ");
			de=de.replaceAll(" "+stopw+" "," ");
			ti=ti.replaceAll(stopw+" "," ");
			de=de.replaceAll(stopw+" "," ");
			ti=ti.replaceAll(" "+stopw," ");
			de=de.replaceAll(" "+stopw," ");

		}
		pe=new PredictionEntry.Builder(de).title(ti).tag(ta).publisher(pe.publisher).id(pe.id).build();
		return pe;
	}
	
//	public static void main(String[] args) throws IOException{
//		IndicePA index=new IndicePA();
//		
//		HashSet<String> tag=new HashSet<String>();
//		tag.add("ciampino");
//		tag.add("sanita");
//		PredictionEntry pe=new PredictionEntry.Builder("benvenuti nel bellissimo comune di fiave").publisher("c_d565").build();
//		System.out.println(index.filterPA(pe));
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
