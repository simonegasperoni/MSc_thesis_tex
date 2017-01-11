package com.sciamlab.it.cata.classifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.it.cata.feature.FeatureExtractor;
import com.sciamlab.it.cata.training.TrainingSet;

public abstract class Bayes implements Classifier{
	protected TrainingSet trainingSet;
	protected Map<Theme, Integer> categoryWholeCountMap;
	protected Map<String, Map<Theme, Integer>> featureToCategoryCountMap;
	protected Map<Theme, Integer> docCounter;
	
	// P(c) calculus - BAYES classifier
	private Map<Theme, Double> P_c(){
		HashMap<Theme, Double> pc=new HashMap<Theme, Double>();
		double tot=0.0;
		for(Integer i:docCounter.values()){
			tot=tot+i;
		}
		for(Theme t:docCounter.keySet()){
			pc.put(t,docCounter.get(t)/tot);
		}
		return pc;
	}
	
	private Map<Theme, Double> termsProd(List<String> corpus){
		HashMap<Theme, Double> tp = new HashMap<Theme, Double>();
		tp.put(Theme.AGRI, 0.0);
		tp.put(Theme.ENER, 0.0);
		tp.put(Theme.GOVE, 0.0);
		tp.put(Theme.INTR, 0.0);
		tp.put(Theme.JUST, 0.0);
		tp.put(Theme.ECON, 0.0);
		tp.put(Theme.SOCI, 0.0);
		tp.put(Theme.EDUC, 0.0);
		tp.put(Theme.TECH, 0.0);
		tp.put(Theme.TRAN, 0.0);
		tp.put(Theme.ENVI, 0.0);
		tp.put(Theme.REGI, 0.0);
		tp.put(Theme.HEAL, 0.0);

		for(Theme t:tp.keySet()){
			int categoryWholeCount = categoryWholeCountMap.get(t);

			for(String feature:corpus){
				Map<Theme,Integer> categoryCount = featureToCategoryCountMap.get(feature);
				
				if(categoryCount==null) continue;
				tp.put(t, tp.get(t)+Math.log(new Double(categoryCount.get(t))/categoryWholeCount));
			}
		}
		return tp;
	}
	
//	//thresholding and normalizing (unity-based normalization)
//	private static Map<Theme,Double> normalize(Map<Theme,Double> results, double threshold){
//		//System.out.println(results);
//		Map<Theme,Double> normres=new HashMap<Theme,Double>();
//		double max=(Collections.max(results.values()));
//		double min=(Collections.min(results.values()));
//		
//		for(Theme t:results.keySet()){
//			
//			double score=round((results.get(t)-min)/(max-min),2);
//			if(score>=threshold)
//				normres.put(t,score);
//		}
//		return normres;
//	}
//	
//	private static double round(double value, int places) {
//	    if (places < 0) throw new IllegalArgumentException();
//	    //System.out.println(value);
//	    BigDecimal bd = new BigDecimal(value);
//	    bd = bd.setScale(places, RoundingMode.HALF_UP);
//	    return bd.doubleValue();
//	}
	
	
	public ClassifiedEntry predict(PredictionEntry entry, FeatureExtractor fe) throws Exception {
		List<String> featuresToPredict=fe.extract(entry);
		return this.predict(featuresToPredict);
	}
	
	public ClassifiedEntry predict(List<String> featuresToPredict) throws Exception {
		Map<Theme,Double> tp=termsProd(featuresToPredict);
		Map<Theme,Double> pc=P_c();
		Map<Theme,Double> results=new HashMap<Theme,Double>();
		for(Theme t:tp.keySet()) 
			results.put(t, Math.log(pc.get(t))+tp.get(t));
		
		Double max=Collections.max(results.values());
		//System.out.println(max);
		Double min=Collections.min(results.values());
		//System.out.println(min);
		Double thr=max-((max-min)*(1.0/9.0));
		//System.out.println(thr);
		
		for(Theme t:Theme.values()) 
			if(results.get(t)<thr) results.remove(t);
		
		return new ClassifiedEntry(featuresToPredict, results);
	}
}
