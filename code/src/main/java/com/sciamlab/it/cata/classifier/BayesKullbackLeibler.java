package com.sciamlab.it.cata.classifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.it.cata.feature.FeatureExtractor;
import com.sciamlab.it.cata.selector.ChiSquareSelector;
import com.sciamlab.it.cata.selector.GenericFeatureSelector;
import com.sciamlab.it.cata.training.TrainingSet;

public class BayesKullbackLeibler extends Bayes{
	
	public BayesKullbackLeibler(TrainingSet trainingSet){
		
		//feature selecting
		GenericFeatureSelector gfs=new ChiSquareSelector();
		gfs.filter(trainingSet, 1000);

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
		Set<String> voc=featureToCategoryCountMap.keySet();
		
		int d=0;
		Map<String, Integer> dcount=new HashMap<String, Integer>();
		for(String s:corpus){
			if(voc.contains(s)){
				d++;
				if(dcount.get(s)==null) dcount.put(s, 1);
				else dcount.put(s,dcount.get(s)+1);
			}	
		}
		
		
		double onefractd=1.0/(new Double(d));
		
		tp.put(Theme.AGRI, onefractd*Math.log(pc.get(Theme.AGRI)));
		tp.put(Theme.ENER, onefractd*Math.log(pc.get(Theme.ENER)));
		tp.put(Theme.GOVE, onefractd*Math.log(pc.get(Theme.GOVE)));
		tp.put(Theme.INTR, onefractd*Math.log(pc.get(Theme.INTR)));
		tp.put(Theme.JUST, onefractd*Math.log(pc.get(Theme.JUST)));
		tp.put(Theme.ECON, onefractd*Math.log(pc.get(Theme.ECON)));
		tp.put(Theme.SOCI, onefractd*Math.log(pc.get(Theme.SOCI)));
		tp.put(Theme.EDUC, onefractd*Math.log(pc.get(Theme.EDUC)));
		tp.put(Theme.TECH, onefractd*Math.log(pc.get(Theme.TECH)));
		tp.put(Theme.TRAN, onefractd*Math.log(pc.get(Theme.TRAN)));
		tp.put(Theme.ENVI, onefractd*Math.log(pc.get(Theme.ENVI)));
		tp.put(Theme.REGI, onefractd*Math.log(pc.get(Theme.REGI)));
		tp.put(Theme.HEAL, onefractd*Math.log(pc.get(Theme.HEAL)));

		for(Theme t:tp.keySet()){
			int categoryWholeCount = categoryWholeCountMap.get(t);

			for(String feature:dcount.keySet()){
				Map<Theme,Integer> categoryCount = featureToCategoryCountMap.get(feature);
				
				double pwd=(new Double(dcount.get(feature)))/(new Double(d));
				
				tp.put(t, tp.get(t)- pwd*(Math.log(pwd/(new Double(categoryCount.get(t))/categoryWholeCount))));
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
		
//		Double max=Collections.max(results.values());
//		Double min=Collections.min(results.values());
//		Double thr=max-((max-min)*(1.0/9.0));
//		
//		for(Theme t:Theme.values()) 
//			if(results.get(t)<thr) results.remove(t);
		
		return new ClassifiedEntry(featuresToPredict, results);
	}
}
