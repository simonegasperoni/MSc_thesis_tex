package com.sciamlab.it.cata;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.common.util.SciamlabStreamUtils;
import com.sciamlab.it.cata.classifier.Classifier;
import com.sciamlab.it.cata.classifier.PredictionEntry;
import com.sciamlab.it.cata.classifier.Bayes;
import com.sciamlab.it.cata.classifier.BayesMultinomial;
import com.sciamlab.it.cata.classifier.BayesMultivariate;
import com.sciamlab.it.cata.classifier.ClassifiedEntry;
import com.sciamlab.it.cata.evaluation.Evaluator;
import com.sciamlab.it.cata.evaluation.KfoldEvaluator;
import com.sciamlab.it.cata.evaluation.OpenDataHubTest;
import com.sciamlab.it.cata.feature.BasicFeatureExtractor;
import com.sciamlab.it.cata.feature.OpenNlpExtractor;
import com.sciamlab.it.cata.selector.ChiSquareSelector;
import com.sciamlab.it.cata.selector.GenericFeatureSelector;
import com.sciamlab.it.cata.selector.MutualInformationSelector;
import com.sciamlab.it.cata.training.AcquisTrainingSource;
import com.sciamlab.it.cata.training.FeatureSelector;
import com.sciamlab.it.cata.training.FeatureSelectorImpl;
import com.sciamlab.it.cata.training.TrainingSet;
import com.sciamlab.it.cata.training.TrainingSetImpl;
import com.sciamlab.it.cata.training.TrainingSource;

public class Cata{

	private static final Logger logger = Logger.getLogger(AcquisTrainingSource.class);
	public static final Properties PROPS = new Properties();
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
		
		AcquisTrainingSource acquisTrainingSource = new AcquisTrainingSource();
		TrainingSet ts=acquisTrainingSource.getTrainingSet();
//		TrainingSet ts2=acquisTrainingSource.getTrainingSet();
//		
//		ts2.createDF();
//		System.out.println("df pre: "+ts2.getDf().size());
		
//		Set<String> set=new HashSet<String>(); 
//		set.add("agevolazioni");
//		set.add("servizi sociali");
//		set.add("trasporto");
//		
//	    //co.tr.al mobilita trasporti trasporto pubblico 
//		
//		Bayes bayes=new BayesMultivariate(ts);
//				PredictionEntry pe=new PredictionEntry(
//						" Agevolazioni trasporto",
//						" Numero domande presentate ed accolte"
//						+ " per agevolazioni trasporto (CO.TRA.L), dal 2011 al 2013", set);
//		
//		ClassifiedEntry ce=bayes.predict(pe, 0.94, new BasicFeatureExtractor());
//		System.out.println(ce);

//		GenericFeatureSelector gfs=new MutualInformationSelector();
//		
//		gfs.filter(ts, 2800);
//		System.out.println("df post: "+ts.getDf().size());
		
//		Evaluator k=new KfoldEvaluator(ts, 10);
//		k.evaluate(BayesMultivariate.class);
		
		OpenDataHubTest o=new OpenDataHubTest(ts);
		o.loadData();
		o.evaluate(BayesMultivariate.class);
		
		
	}

	public Cata() throws Exception{
		FeatureSelector featureSelector = new FeatureSelectorImpl();
		try (TrainingSource acquisTrainingSource = new AcquisTrainingSource()){
			TrainingSet trainingSet = acquisTrainingSource.getTrainingSet();			
			trainingSet = featureSelector.clean(trainingSet);
			classifier = Classifier.Factory.build(BayesMultinomial.class, trainingSet);
		}
	}
}
