package com.sciamlab.it.cata.training;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.it.cata.classifier.ClassifiedEntry;

public class TrainingSetImpl implements TrainingSet {
	private Map<String, Map<Theme, Integer>> tf;
	private Map<String, Map<Theme, Integer>> df;
	private Map<Theme, Integer> doccounter;
	private Map<String, ClassifiedEntry> docMap;
	private Map<Theme, Integer> sumDF;
	private Map<Theme, Integer> sumTF;
	
	public TrainingSetImpl(Map<String, ClassifiedEntry> docMap){
		this.docMap=docMap;
	}

	public Map<String, Map<Theme, Integer>> createTF(){
		tf=new HashMap<String, Map<Theme, Integer>>();
		for(ClassifiedEntry c:docMap.values()){
			for(String f:c.featureSet){
				Map<Theme, Integer> m = tf.get(f);
				if(m==null){
					m = new HashMap<Theme, Integer>();
					m.put(EUNamedAuthorityDataTheme.Theme.AGRI, 1);
					m.put(EUNamedAuthorityDataTheme.Theme.ENER, 1);
					m.put(EUNamedAuthorityDataTheme.Theme.GOVE, 1);
					m.put(EUNamedAuthorityDataTheme.Theme.INTR, 1);
					m.put(EUNamedAuthorityDataTheme.Theme.JUST, 1);
					m.put(EUNamedAuthorityDataTheme.Theme.ECON, 1);
					m.put(EUNamedAuthorityDataTheme.Theme.SOCI, 1);
					m.put(EUNamedAuthorityDataTheme.Theme.EDUC, 1);
					m.put(EUNamedAuthorityDataTheme.Theme.TECH, 1);
					m.put(EUNamedAuthorityDataTheme.Theme.TRAN, 1);
					m.put(EUNamedAuthorityDataTheme.Theme.ENVI, 1);
					m.put(EUNamedAuthorityDataTheme.Theme.REGI, 1);
					m.put(EUNamedAuthorityDataTheme.Theme.HEAL, 1);
					tf.put(f, m);
				}
				for(Theme cat:c.categories.keySet()){
					m.put(cat, m.get(cat)+1) ;
				}
			}
		}
		return tf;
	}

	public Map<String, Map<Theme, Integer>> createDF(){
		df=new HashMap<String, Map<Theme, Integer>>();
		for(ClassifiedEntry c:docMap.values()){
			List<String> fs =new ArrayList<>(new LinkedHashSet<>(c.featureSet));
			for(String f:fs){
				Map<Theme, Integer> m = df.get(f);
				if(m==null){
					m = new HashMap<Theme, Integer>();
					m.put(Theme.AGRI, 1);
					m.put(Theme.ENER, 1);
					m.put(Theme.GOVE, 1);
					m.put(Theme.INTR, 1);
					m.put(Theme.JUST, 1);
					m.put(Theme.ECON, 1);
					m.put(Theme.SOCI, 1);
					m.put(Theme.EDUC, 1);
					m.put(Theme.TECH, 1);
					m.put(Theme.TRAN, 1);
					m.put(Theme.ENVI, 1);
					m.put(Theme.REGI, 1);
					m.put(Theme.HEAL, 1);
					df.put(f, m);
				}
				for(Theme cat:c.categories.keySet()){
					m.put(cat, m.get(cat)+1) ;
				}
			}
		}
		return df;
	}
	
	public Map<Theme, Integer> createDoccounter(){
		doccounter=new HashMap<Theme, Integer>(); 
		doccounter.put(Theme.AGRI, 0);
		doccounter.put(Theme.ENER, 0);
		doccounter.put(Theme.GOVE, 0);
		doccounter.put(Theme.INTR, 0);
		doccounter.put(Theme.JUST, 0);
		doccounter.put(Theme.ECON, 0);
		doccounter.put(Theme.SOCI, 0);
		doccounter.put(Theme.EDUC, 0);
		doccounter.put(Theme.TECH, 0);
		doccounter.put(Theme.TRAN, 0);
		doccounter.put(Theme.ENVI, 0);
		doccounter.put(Theme.REGI, 0);
		doccounter.put(Theme.HEAL, 0);
		for(ClassifiedEntry c:docMap.values()){
			for(Theme cat:c.categories.keySet()){
				doccounter.put(cat, doccounter.get(cat)+1);
			}
		}
		return doccounter;
	}
	
	public Map<Theme, Integer> createSumTF() {
		sumTF=new HashMap<Theme, Integer>();
		sumTF.put(Theme.AGRI, 0);
		sumTF.put(Theme.ENER, 0);
		sumTF.put(Theme.GOVE, 0);
		sumTF.put(Theme.INTR, 0);
		sumTF.put(Theme.JUST, 0);
		sumTF.put(Theme.ECON, 0);
		sumTF.put(Theme.SOCI, 0);
		sumTF.put(Theme.EDUC, 0);
		sumTF.put(Theme.TECH, 0);
		sumTF.put(Theme.TRAN, 0);
		sumTF.put(Theme.ENVI, 0);
		sumTF.put(Theme.REGI, 0);
		sumTF.put(Theme.HEAL, 0);		
		for(Theme t:sumTF.keySet()){
			for(String s:tf.keySet()){
				sumTF.put(t, sumTF.get(t)+tf.get(s).get(t));
			}
		}
		return sumTF;
	}

	public Map<Theme, Integer> createSumDF() {
		sumDF=new HashMap<Theme, Integer>();
		sumDF.put(Theme.AGRI, 0);
		sumDF.put(Theme.ENER, 0);
		sumDF.put(Theme.GOVE, 0);
		sumDF.put(Theme.INTR, 0);
		sumDF.put(Theme.JUST, 0);
		sumDF.put(Theme.ECON, 0);
		sumDF.put(Theme.SOCI, 0);
		sumDF.put(Theme.EDUC, 0);
		sumDF.put(Theme.TECH, 0);
		sumDF.put(Theme.TRAN, 0);
		sumDF.put(Theme.ENVI, 0);
		sumDF.put(Theme.REGI, 0);
		sumDF.put(Theme.HEAL, 0);		
		for(Theme t:sumDF.keySet()){
			for(String s:df.keySet()){
				sumDF.put(t, sumDF.get(t)+df.get(s).get(t));
			}
		}
		return sumDF;
	}
	
	public Map<String, Map<Theme, Integer>> getDf() {
		return df;
	}
	
	public Map<String, Map<Theme, Integer>> getTf() {
		return tf;
	}
	
	public Map<Theme, Integer> getDoccounter() {
		return doccounter;
	}
	
	public Map<String, ClassifiedEntry> getDocMap() {
		return docMap;
	}
	
	public Map<Theme, Integer> getSumDF() {
		return sumDF;
	}
	
	public Map<Theme, Integer> getSumTF() {
		return sumTF;
	}
	
}
