package com.sciamlab.it.cata.classifier;
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
	
	public abstract Map<Theme, Double> termsProd(List<String> corpus);
	
	public abstract ClassifiedEntry predict(PredictionEntry entry, FeatureExtractor fe) throws Exception ;
	
	public abstract ClassifiedEntry predict(List<String> featuresToPredict) throws Exception ;
}
