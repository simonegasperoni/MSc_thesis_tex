package com.sciamlab.it.cata;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import com.sciamlab.common.util.SciamlabStreamUtils;
import com.sciamlab.it.cata.classifier.Classifier;
import com.sciamlab.it.cata.classifier.PredictionEntry;
import com.sciamlab.it.cata.classifier.Bayes;
import com.sciamlab.it.cata.classifier.BayesMultinomialWF;
import com.sciamlab.it.cata.classifier.BayesTagBased;
import com.sciamlab.it.cata.classifier.ClassifiedEntry;
import com.sciamlab.it.cata.feature.StemFeatureExtractor;
import com.sciamlab.it.cata.training.AcquisTrainingSource;
import com.sciamlab.it.cata.training.TrainingSet;

public class Cata{

	private static final Logger logger = Logger.getLogger(AcquisTrainingSource.class);
	public static final Properties PROPS = new Properties();
	@SuppressWarnings("unused")
	private Classifier classifier;

	
//	public static void writ(String file, Map<String, Map<Theme, Integer>> map) 
//			throws UnsupportedEncodingException, FileNotFoundException, IOException{
//		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
//				new FileOutputStream(file), "utf-8"))) {
//			
//			for(String k:map.keySet()){
//				writer.write(k+" \n");
//				writer.write(map.get(k).keySet().toString()+" \n");
//				writer.write(map.get(k).values().toString()+" \n");
//			}
//		}
//	}
	
	static{
		try {
			PROPS.load(SciamlabStreamUtils.getInputStream("cata.properties"));
			logger.info("ok properties");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws Exception {
		
		@SuppressWarnings("resource")
		AcquisTrainingSource acquisTrainingSource = new AcquisTrainingSource();
		TrainingSet ts=acquisTrainingSource.getTrainingSet();
		
		Set<String> set=new HashSet<String>(); 
		set.add("contributi");
		PredictionEntry pe=new PredictionEntry("Aliquote contributive",
				"", set);
		
		//Bayes bayes=new BayesBernoulli(ts);
		//Bayes bayes=new BayesMultinomialWO(ts);
		Bayes bayes=new BayesMultinomialWF(ts);
		//Bayes bayes=new BayesKullbackLeibler(ts);
		//Bayes bayes=new BayesTagBased(ts);		
		
		ClassifiedEntry ce=bayes.predict(pe, new StemFeatureExtractor());
		System.out.println(ce);

		
		
//		Set<String> set=new HashSet<String>(); 
//		set.add("nazionalità");
//		set.add("imprese straniere");
//		
//		
//		Bayes bayes=new BayesBernoulli(ts);
//				PredictionEntry pe=new PredictionEntry(
//						"Imprese Individuali gestite da imprenditori extracomunitari della Provincia di Lecce - II Trimestre 2016",
//						"a o", set);
//		
//		ClassifiedEntry ce=bayes.predict(pe, new BasicFeatureExtractor());
//		System.out.println(ce);
		
				
		
	}

//	public Cata() throws Exception{
//		FeatureSelector featureSelector = new FeatureSelectorImpl();
//		try (TrainingSource acquisTrainingSource = new AcquisTrainingSource()){
//			TrainingSet trainingSet = acquisTrainingSource.getTrainingSet();			
//			trainingSet = featureSelector.clean(trainingSet);
//			classifier = Classifier.Factory.build(BayesMultinomial.class, trainingSet);
//		}
//	}
}
