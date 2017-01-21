package com.sciamlab.it.cata;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import com.sciamlab.common.util.SciamlabStreamUtils;
import com.sciamlab.it.cata.classifier.Classifier;
import com.sciamlab.it.cata.classifier.PredictionEntry;
import com.sciamlab.it.cata.classifier.Bayes;
import com.sciamlab.it.cata.classifier.BayesKullbackLeibler;
import com.sciamlab.it.cata.classifier.BayesMultinomialWF;
import com.sciamlab.it.cata.classifier.BayesMultinomialWO;
import com.sciamlab.it.cata.classifier.ClassifiedEntry;
import com.sciamlab.it.cata.evaluation.OpenDataHubTest;
import com.sciamlab.it.cata.feature.BasicFeatureExtractor;
import com.sciamlab.it.cata.training.AcquisTrainingSource;
import com.sciamlab.it.cata.training.TrainingSet;

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
//		set.add("lavoro");
//		set.add("cessazioni");
//		
//		Bayes bayes=new BayesMultivariate(ts);
//				PredictionEntry pe=new PredictionEntry(
//						null,
//						"Comunicazioni Obbligatorie - Cessazioni Le Comunicazioni Obbligatorie (CO) sono quelle che tutti "
//						+ "i datori di lavoro, pubblici e privati, devono trasmettere in caso di assunzione, proroga, "
//						+ "trasformazione e cessazione dei rapporti di lavoro e variazione della ragione sociale delle aziende. "
//						+ "Il Sistema CO dall'11 gennaio 2008 connette tutti i Centri per l'impiego, le Province le Regioni "
//						+ "d'Italia ed il Ministero del lavoro, oltre ad altri enti - come INPS ed INAIL. "
//						+ "Le nuove disposizioni sono state introdotte dalla L. n. 296 2007 (legge finanziaria per il 2007)."
//						+ "Successivamente, con il Decreto Ministeriale 30 ottobre 2007 sono stati approvati i nuovi modelli "
//						+ "per le comunicazioni datoriali ed avviata la modalita telematica di invio delle stesse.", set);
//		
//		ClassifiedEntry ce=bayes.predict(pe, new BasicFeatureExtractor());
//		System.out.println(ce);

		Set<String> set=new HashSet<String>(); 
		set.add("nazionalità");
		set.add("imprese straniere");
		
		
		Bayes bayes=new BayesKullbackLeibler(ts);
				PredictionEntry pe=new PredictionEntry(
						"Imprese Individuali gestite da imprenditori extracomunitari della Provincia di Lecce - II Trimestre 2016",
						"a o", set);
		
		ClassifiedEntry ce=bayes.predict(pe, new BasicFeatureExtractor());
		System.out.println(ce);
		
		
		
		
//		GenericFeatureSelector gfs=new MutualInformationSelector();
//		
//		gfs.filter(ts, 2800);
//		System.out.println("df post: "+ts.getDf().size());
		
//		Evaluator k=new KfoldEvaluator(ts, 10);
//		k.evaluate(BayesMultivariate.class);
		
//		OpenDataHubTest o=new OpenDataHubTest(ts);
//		o.loadData();
//		o.evaluate(BayesMultivariate.class);
		
		
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
