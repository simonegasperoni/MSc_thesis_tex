package com.sciamlab.it.cata.classifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.it.cata.feature.FeatureExtractor;

public abstract class BayesMultinomial extends Bayes{
	
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
		
		return new ClassifiedEntry(featuresToPredict, thr.getScore(results));
	}
	
	public Map<Theme, Double> termsProd(List<String> corpus){
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

		for(Theme t:Theme.values()){
			int categoryWholeCount = categoryWholeCountMap.get(t);

			for(String feature:corpus){
				Map<Theme,Integer> categoryCount = featureToCategoryCountMap.get(feature);
				
				if(categoryCount==null) continue;		
				tp.put(t, tp.get(t)+Math.log(new Double(categoryCount.get(t))/categoryWholeCount));
			}
		}
		return tp;
	}
	
}
