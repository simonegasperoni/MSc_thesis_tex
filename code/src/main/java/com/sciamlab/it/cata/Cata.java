package com.sciamlab.it.cata;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
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
		run();
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
		
		
		List<String> tags=new ArrayList<String>();
		tags.add("ptpr");
		Set<Theme> categories=new HashSet<Theme>();
		categories.add(Theme.ENVI);
		SOLrQuery q=new SOLrQuery("r_lazio", tags, categories);
		
		List<SOLrQuery> queries=new ArrayList<SOLrQuery>();
		
		// entry query for retrain: <lazio, t:<ptpr>, c:<envi>>
		queries.add(q);
		
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

class SOLrQuery{
	private String publisher;
	private List<String> tags;
	private Set<Theme> categories;
	
	public SOLrQuery(String publisher, List<String> tags, Set<Theme> categories){
		this.publisher=publisher;
		this.tags=tags;
		this.categories=categories;
	}
	
	public Set<Theme> getCategories() {
		return categories;
	}
	
	public String getPublisher() {
		return publisher;
	}
	
	public List<String> getTags() {
		return tags;
	}
}
