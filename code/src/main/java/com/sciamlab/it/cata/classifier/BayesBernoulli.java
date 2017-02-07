package com.sciamlab.it.cata.classifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.it.cata.feature.FeatureExtractor;
import com.sciamlab.it.cata.training.TrainingSet;

public class BayesBernoulli extends Bayes{
	
	public BayesBernoulli(TrainingSet trainingSet){
		
		//feature selecting
		this.featureSelector.filter(trainingSet);
		
		this.featureToCategoryCountMap=trainingSet.getDf();
		System.out.println("df map size: "+featureToCategoryCountMap.size());
		trainingSet.createDoccounter();
		
		this.docCounter=trainingSet.getDoccounter();
		trainingSet.createSumDF();
		this.categoryWholeCountMap=trainingSet.getSumDF();
	}
	
	public Map<Theme, Double> termsProd(List<String> corpus){
		Map<Theme,Double> pc=P_c();
		HashMap<Theme, Double> tp = new HashMap<Theme, Double>();

		tp.put(Theme.AGRI, Math.log(pc.get(Theme.AGRI)));
		tp.put(Theme.ENER, Math.log(pc.get(Theme.ENER)));
		tp.put(Theme.GOVE, Math.log(pc.get(Theme.GOVE)));
		tp.put(Theme.INTR, Math.log(pc.get(Theme.INTR)));
		tp.put(Theme.JUST, Math.log(pc.get(Theme.JUST)));
		tp.put(Theme.ECON, Math.log(pc.get(Theme.ECON)));
		tp.put(Theme.SOCI, Math.log(pc.get(Theme.SOCI)));
		tp.put(Theme.EDUC, Math.log(pc.get(Theme.EDUC)));
		tp.put(Theme.TECH, Math.log(pc.get(Theme.TECH)));
		tp.put(Theme.TRAN, Math.log(pc.get(Theme.TRAN)));
		tp.put(Theme.ENVI, Math.log(pc.get(Theme.ENVI)));
		tp.put(Theme.REGI, Math.log(pc.get(Theme.REGI)));
		tp.put(Theme.HEAL, Math.log(pc.get(Theme.HEAL)));

		// data structs
		//-----------------------------------
		// featureToCategoryCountMap : [Map<<String, Map<Theme, Integer>>] : df map
		// categoryWholeCountMap : [Map<Theme, Integer>] : # documents per category
		// voc : [Set<String>] : vocabulary
		// b : [Set<String>] : terms in doc to classify
				
		Set<String> voc=featureToCategoryCountMap.keySet();
		Set<String> b=new HashSet<String>();
		for(String s:corpus) b.add(s);
		
		for(String v:voc){
			for(Theme t:Theme.values()){
				int docsPerCat = categoryWholeCountMap.get(t);
				int dfPerFeature=featureToCategoryCountMap.get(v).get(t);
				if(b.contains(v)){
					tp.put(t, tp.get(t)+ Math.log((new Double(dfPerFeature))/docsPerCat));
				}
				else{
					tp.put(t, tp.get(t)+ Math.log(1-(new Double(dfPerFeature))/docsPerCat));
				}
			}
		}
		return tp;
	}
	
	public ClassifiedEntry predict(PredictionEntry entry, FeatureExtractor fe) throws Exception {
		List<String> featuresToPredict=fe.extract(entry);
		return this.predict(featuresToPredict);
	}
	
	public ClassifiedEntry predict(List<String> featuresToPredict) throws Exception {
		Map<Theme,Double> tp=termsProd(featuresToPredict);
		
		Map<Theme,Double> results=new HashMap<Theme,Double>();
		for(Theme t:tp.keySet()) 
			results.put(t, tp.get(t));
		
		return new ClassifiedEntry(featuresToPredict, thr.getScore(results));
	}

}
