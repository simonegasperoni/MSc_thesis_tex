package com.sciamlab.it.cata;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.common.util.SciamlabStreamUtils;
import com.sciamlab.it.cata.classifier.Classifier;
import com.sciamlab.it.cata.classifier.PredictionEntry;
import com.sciamlab.it.cata.evaluation.OpenDataHubTest;
import com.sciamlab.it.cata.feature.StemFeatureExtractor;
import com.sciamlab.it.cata.selector.ChiSquareSelector;
import com.sciamlab.it.cata.classifier.BayesMultinomialWF;
import com.sciamlab.it.cata.training.AcquisTrainingSource;
import com.sciamlab.it.cata.training.DatasetTrainingSource;
import com.sciamlab.it.cata.training.TrainingSet;
import com.sciamlab.it.cata.training.TrainingSource;
import com.sciamlab.it.cata.retraining.Retrainer;
import com.sciamlab.it.cata.retraining.SOLrQuery;

public class Cata {

	private static final Logger logger = Logger.getLogger(AcquisTrainingSource.class);
	public static final Properties PROPS = new Properties();

	static {
		try {
			PROPS.load(SciamlabStreamUtils.getInputStream("cata.properties"));
			logger.info("ok properties");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) throws Exception {
	}
	
	public static void test() throws ClassNotFoundException, SQLException, Exception {
		try (TrainingSource acquisTrainingSource = new AcquisTrainingSource();) {
			TrainingSet acquis = acquisTrainingSource.getTrainingSet();
			acquis.filter(new ChiSquareSelector(2000));
			System.out.println("acquis size: "+acquis.getDf().size());
			OpenDataHubTest test=new OpenDataHubTest(acquis);
			test.evaluate(BayesMultinomialWF.class);
		}
	}

	public static void run() throws ClassNotFoundException, SQLException, Exception{
		List<SOLrQuery> queries=Retrainer.getQueries();
		
		try (TrainingSource acquisTrainingSource = new AcquisTrainingSource();) {
			
			TrainingSet acquis = acquisTrainingSource.getTrainingSet();
			System.out.println("acquis size: "+acquis.getDf().size());
			
			// filter top 500 features per category
			TrainingSet acquis500 = acquis.clone().filter(new ChiSquareSelector(500));
			System.out.println("acquis500 size: "+acquis500.getDf().size());
			
			
			
			try (DatasetTrainingSource ds = new DatasetTrainingSource();) {
				for(SOLrQuery query : queries){
					TrainingSet ts_ckan_query = ds.getTrainingSet(query.getPublisher(),query.getTags(),query.getCategories());
					System.out.println("BEFORE ckan ts size: "+ts_ckan_query.getDf().size());
					ts_ckan_query.remove(acquis500);
					System.out.println("AFTER ckan ts size: "+ts_ckan_query.getDf().size());
					acquis.merge(ts_ckan_query);
				}
			}
			
			// filter top 2000 features per category
			acquis.filter(new ChiSquareSelector(2000));
			System.out.println("acquis2000 size: "+acquis.getDf().size());
			
			Class<BayesMultinomialWF> clazz = BayesMultinomialWF.class;
			Classifier classifier = Classifier.Factory.build(clazz, acquis);

			PredictionEntry pe = new PredictionEntry.Builder("PTPR - Tav. B - Rispetto centri storici"
					+ "Beni paesaggistici (art. 134 com. 1 lett. c Dlvo 42 2004) - Immobili e le aree tipizzati, "
					+ "individuati e sottoposti a tutela dal piano: fascia di rispetto degli insediamenti urbani storici e "
					+ "citta di fondazione 150 ml. ptpr, storici, centri").build();

			Theme ce1 = classifier.predictFirst(pe, new StemFeatureExtractor());
			System.out.println(ce1);

		}	
	}
}
