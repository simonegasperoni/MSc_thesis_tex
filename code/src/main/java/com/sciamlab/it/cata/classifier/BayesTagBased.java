package com.sciamlab.it.cata.classifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.it.cata.feature.FeatureExtractor;
import com.sciamlab.it.cata.training.TrainingSet;

public class BayesTagBased extends Bayes{
	
	public BayesTagBased(TrainingSet trainingSet){
		this.featureSelector.filter(trainingSet);

		this.featureToCategoryCountMap=trainingSet.getDf();
		System.out.println("df map size: "+featureToCategoryCountMap.size());
		trainingSet.createTF();
		this.featureToCategoryCountMap=trainingSet.getTf();
		System.out.println("tf map size: "+featureToCategoryCountMap.size());
		trainingSet.createDoccounter();
		
		this.docCounter=trainingSet.getDoccounter();
		trainingSet.createSumTF();
		this.categoryWholeCountMap=trainingSet.getSumTF();
	}
	
	
	public ClassifiedEntry predict(PredictionEntry entry, FeatureExtractor fe) throws Exception {
		Set<String> tag=new HashSet<String>();
		Set<String> tagaux=new HashSet<String>();
		if(entry.getTitle()!=null){
			tagaux.addAll(fe.execute(entry.getTitle()));
		}
		for(String s: entry.getTags()){
			tagaux.addAll(fe.execute(s));
		}
		for(String t:tagaux){
			if(featureToCategoryCountMap.get(t)!=null){
				tag.add(t);
			}
		}
		
		System.out.println("tagset: "+tag);
		
		List<String> corpus=fe.extract(entry);
		
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
		
		for(Theme t:tp.keySet()){
			int categoryWholeCount = categoryWholeCountMap.get(t);

			for(String feature:corpus){
				Map<Theme,Integer> categoryCount = featureToCategoryCountMap.get(feature);
				if(categoryCount==null) continue;
				if(tag.contains(feature)){
					tp.put(t, tp.get(t)+Math.log(new Double(categoryCount.get(t))/categoryWholeCount));
				}
				else{
					tp.put(t, tp.get(t)+(Math.log((2.0/new Double(tag.size()))*(new Double(categoryCount.get(t))/categoryWholeCount))));
				}
			}
		}
		
		
		return new ClassifiedEntry(corpus,thr.getScore(tp));
	}
	
	public ClassifiedEntry predict(List<String> featuresToPredict) throws Exception {
		return null;
	}
	public Map<Theme, Double> termsProd(List<String> corpus) {
		return null;
	}
}
