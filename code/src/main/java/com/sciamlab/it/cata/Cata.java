package com.sciamlab.it.cata;
import java.util.Properties;
import org.apache.log4j.Logger;
import com.sciamlab.common.util.SciamlabStreamUtils;
import com.sciamlab.it.cata.classifier.Classifier;
import com.sciamlab.it.cata.evaluation.OpenDataHubTest;
import com.sciamlab.it.cata.classifier.BayesMultinomialWF;
import com.sciamlab.it.cata.training.AcquisTrainingSource;
import com.sciamlab.it.cata.training.TrainingSet;

public class Cata{

	private static final Logger logger = Logger.getLogger(AcquisTrainingSource.class);
	public static final Properties PROPS = new Properties();
	@SuppressWarnings("unused")
	private Classifier classifier;

	static{
		try {
			PROPS.load(SciamlabStreamUtils.getInputStream("cata.properties"));
			logger.info("ok properties");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws Exception {

		//DatasetTrainingSource ds=new DatasetTrainingSource("imprese femminili", Theme.ECON);
		@SuppressWarnings("resource")
		AcquisTrainingSource acquisTrainingSource = new AcquisTrainingSource();
		TrainingSet ts=acquisTrainingSource.getTrainingSet();
		//ts.merge(ds.getTrainingSet());

		//PredictionEntry pe=new PredictionEntry(null,null,null);
		//Class<BayesMultinomialWF> clazz=BayesMultinomialWF.class;
		//Classifier classifier = Classifier.Factory.build(clazz, ts);
		//ClassifiedEntry ce=classifier.predict(pe, new StemFeatureExtractor());
		//System.out.println(ce);

		OpenDataHubTest test=new OpenDataHubTest(ts);
		test.evaluate(BayesMultinomialWF.class);

	}

	//public Cata() throws Exception{
	//	FeatureSelector featureSelector = new FeatureSelectorImpl();
	//	try (TrainingSource acquisTrainingSource = new AcquisTrainingSource()){
	//		TrainingSet trainingSet = acquisTrainingSource.getTrainingSet();			
	//		trainingSet = featureSelector.clean(trainingSet);
	//		classifier = Classifier.Factory.build(BayesMultinomial.class, trainingSet);
	//	}
	//}
}
