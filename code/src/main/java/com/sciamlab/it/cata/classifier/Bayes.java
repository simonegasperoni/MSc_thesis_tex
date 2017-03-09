package com.sciamlab.it.cata.classifier;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.it.amministrazioni.IndicePA;
import com.sciamlab.it.cata.feature.FeatureExtractor;
import com.sciamlab.it.cata.threshold.MaxMinProportionalScore;
import com.sciamlab.it.cata.threshold.Thresholder;
import com.sciamlab.it.cata.training.TrainingSet;

public abstract class Bayes implements Classifier{
	protected TrainingSet trainingSet;
	protected Map<Theme, Integer> categoryWholeCountMap;
	protected Map<String, Map<Theme, Integer>> featureToCategoryCountMap;
	protected Map<Theme, Integer> docCounter;
	Thresholder thr=new MaxMinProportionalScore(1.0/9.0);
	protected IndicePA pa;
	
	public Bayes() throws IOException{
		this.pa=new IndicePA();
	}
	
	// P(c) calculus - BAYES classifier
	protected Map<Theme, Double> P_c(){
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
	
	public ClassifiedEntry predictFirst(PredictionEntry entry, FeatureExtractor fe) throws Exception {
		PredictionEntry p=pa.filterPA(entry);
		//System.out.println(p);
		List<String> featuresToPredict=fe.extract(p);
		Map<Theme,Double> tp=termsProd(featuresToPredict);
		Map<Theme,Double> pc=P_c();
		Map<Theme,Double> results=new HashMap<Theme,Double>();
		for(Theme t:tp.keySet()) 
			results.put(t, Math.log(pc.get(t))+tp.get(t));
		Map<Theme,Double> set=new HashMap<Theme,Double>();
		set.put(thr.getFirst(results),0.0);
		ClassifiedEntry ce=new ClassifiedEntry(featuresToPredict, set);
		return ce;
	}
	
	public abstract Map<Theme, Double> termsProd(List<String> corpus);
	
	public abstract ClassifiedEntry predict(PredictionEntry entry, FeatureExtractor fe) throws Exception ;
	
	public abstract ClassifiedEntry predict(List<String> featuresToPredict) throws Exception ;
}
