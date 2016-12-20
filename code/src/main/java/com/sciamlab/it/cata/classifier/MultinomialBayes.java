package com.sciamlab.it.cata.classifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.it.cata.feature.FeatureExtractor;
import com.sciamlab.it.cata.training.TrainingSet;

public class MultinomialBayes implements Classifier{

	private TrainingSet trainingSet;
	private FeatureExtractor fe;
	
	public MultinomialBayes(TrainingSet trainingSet, FeatureExtractor featureExtractor){
		this.trainingSet=trainingSet;
		this.fe=featureExtractor;
		trainingSet.createTF();
		trainingSet.createDoccounter();
		trainingSet.createSumTF();
	}
	
	// P(c) calculus - BAYES classifier
	private Map<Theme, Double> P_c(){
		HashMap<Theme, Double> pc=new HashMap<Theme, Double>();
		Map<Theme, Integer> sum=trainingSet.getDoccounter();
		double tot=0.0;
		for(Integer i:sum.values()){
			tot=tot+i;
		}
		for(Theme t:sum.keySet()){
			pc.put(t,sum.get(t)/tot);
		}
		return pc;
	}
	
	private Map<Theme, Double> termsProd(List<String> corpus){
		HashMap<Theme, Double> tp = new HashMap<Theme, Double>();
		Map<Theme, Integer> sumsTf = trainingSet.getSumTF();
		Map<String, Map<Theme, Integer>> tf=trainingSet.getTf();
		tp.put(Theme.AGRI, 1.0);
		tp.put(Theme.ENER, 1.0);
		tp.put(Theme.GOVE, 1.0);
		tp.put(Theme.INTR, 1.0);
		tp.put(Theme.JUST, 1.0);
		tp.put(Theme.ECON, 1.0);
		tp.put(Theme.SOCI, 1.0);
		tp.put(Theme.EDUC, 1.0);
		tp.put(Theme.TECH, 1.0);
		tp.put(Theme.TRAN, 1.0);
		tp.put(Theme.ENVI, 1.0);
		tp.put(Theme.REGI, 1.0);
		tp.put(Theme.HEAL, 1.0);
		for(String feature:corpus){
			if(tp.containsKey(feature)) corpus.remove(feature);
		}	
		for(Theme t:tp.keySet()){
			int sum=sumsTf.get(t);
			for(String feature:corpus){
				tp.put(t, (tp.get(t)*(tf.get(feature).get(t))/sum));
			}
		}
		return tp;
	}
		
	public ClassifiedEntry predict(PredictionEntry entry) throws Exception {
		
		List<String> featuresToPredict=fe.extract(entry);
		
		Map<Theme,Double> tp=termsProd(featuresToPredict);
		Map<Theme,Double> pc=P_c();
		Map<Theme,Double> results=new HashMap<Theme,Double>();
		
		for(Theme t:tp.keySet()) results.put(t, pc.get(t)*tp.get(t));
		return new ClassifiedEntry(featuresToPredict, results);
	}
}
