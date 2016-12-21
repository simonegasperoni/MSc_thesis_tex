package com.sciamlab.it.cata;
import java.util.Properties;
import org.apache.log4j.Logger;
import com.sciamlab.common.util.SciamlabStreamUtils;
import com.sciamlab.it.cata.classifier.ClassifiedEntry;
import com.sciamlab.it.cata.classifier.Classifier;
import com.sciamlab.it.cata.classifier.MultinomialBayes;
import com.sciamlab.it.cata.classifier.PredictionEntry;
import com.sciamlab.it.cata.feature.FeatureExtractor;
import com.sciamlab.it.cata.feature.OpenNlpExtractor;
import com.sciamlab.it.cata.feature.BasicFeatureExtractor;
import com.sciamlab.it.cata.training.AcquisTrainingSource;
import com.sciamlab.it.cata.training.FeatureSelector;
import com.sciamlab.it.cata.training.FeatureSelectorImpl;
import com.sciamlab.it.cata.training.TrainingSet;
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

	public static void main(String[] args) throws Exception {
		PROPS.load(SciamlabStreamUtils.getInputStream("cata.properties"));
		logger.info("ok properties");
		
		FeatureExtractor fe=new OpenNlpExtractor();
		AcquisTrainingSource acquisTrainingSource = new AcquisTrainingSource();
		TrainingSet ts=acquisTrainingSource.getTrainingSet();
	    
		Classifier bayes=new MultinomialBayes(ts,fe);
	    
		PredictionEntry pe=new PredictionEntry(null, "Depositi e impieghi bancari per abitante in alcuni Comuni della "
				+ "Provincia di Roma 2010. Depositi e impieghi bancari per abitante nei Comuni delle colline litoranee "
				+ "dei Colli Albani e nella pianura di Anzio e Nettuno - 31 dicembre 2010.", null);
		
		ClassifiedEntry ce=bayes.predict(pe, 0.5);
		System.out.println(ce);
	}

	public Cata() throws Exception{
		FeatureSelector featureSelector = new FeatureSelectorImpl();
		FeatureExtractor featureExtractor = new BasicFeatureExtractor();
		try (TrainingSource acquisTrainingSource = new AcquisTrainingSource()){
			TrainingSet trainingSet = acquisTrainingSource.getTrainingSet();			
			trainingSet = featureSelector.clean(trainingSet);
			classifier = Classifier.Factory.build(MultinomialBayes.class, trainingSet, featureExtractor);
		}
	}
}
