package com.sciamlab.it.cata;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import com.sciamlab.common.util.SciamlabStreamUtils;
import com.sciamlab.it.cata.evaluation.OpenDataHubTest;
import com.sciamlab.it.cata.selector.ChiSquareSelector;
import com.sciamlab.it.cata.selector.MutualInformationSelector;
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
		TrainingSet ts;
		
		ts=createTS(50);
		test(ts,"db2");
		
		ts=createTS(100);
		test(ts,"db2");
		
		ts=createTS(500);
		test(ts,"db2");
		
		ts=createTS(1000);
		test(ts,"db2");
		
		ts=createTS(2000);
		test(ts,"db2");
		
		ts=createTS(2500);
		test(ts,"db2");
		
		ts=createTS(3000);
		test(ts,"db2");
		
		ts=createTS(5000);
		test(ts,"db2");
		
		ts=createTS(10000);
		test(ts,"db2");
		
		ts=createTS(20000);
		test(ts,"db2");
		
		ts=createTS(70000);
		test(ts,"db2");
		
		ts=createTS(80000);
		test(ts,"db2");
		
//		System.out.println("|||||||||||||||---------------------------lecce:");
//		test(ts,"odh22lecce");
//		System.out.println("|||||||||||||||---------------------------lazio:");
//		test(ts,"odh22lazio");
//		System.out.println("|||||||||||||||---------------------------infrastrutture:");
//		test(ts,"odh22infrastrutture");
//		System.out.println("|||||||||||||||---------------------------lombardia:");
//		test(ts,"odh22lombardia");
//		System.out.println("|||||||||||||||---------------------------ortofoto:");
//		test(ts,"odh22ortofoto");
//		System.out.println("|||||||||||||||---------------------------ricettivita:");
//		test(ts,"odh22ricettivita");
//		System.out.println("|||||||||||||||---------------------------autostrada:");
//		test(ts,"odh22autostrada");
//		System.out.println("|||||||||||||||---------------------------scuole:");
//		test(ts,"odh22scuole");
//		System.out.println("|||||||||||||||---------------------------trentino:");
//		test(ts,"odh22trentino");
		
	}
	
	public static void test(TrainingSet set, String table) throws ClassNotFoundException, SQLException, Exception {
		OpenDataHubTest test=new OpenDataHubTest(set, table);
		test.evaluate(BayesMultinomialWF.class);
		
	}

	public static TrainingSet createTS(int k) throws ClassNotFoundException, SQLException, Exception{
		List<SOLrQuery> queries=Retrainer.getQueries();
		TrainingSet acquis;
		try (TrainingSource acquisTrainingSource = new AcquisTrainingSource();) {
			
			acquis = acquisTrainingSource.getTrainingSet();
			System.out.println("acquis size: "+acquis.getDf().size());
			
			// filter top 500 features per category
//			TrainingSet acquis500 = acquis.clone().filter(new ChiSquareSelector(100));
//			System.out.println("acquis500 size: "+acquis500.getDf().size());
			
			
			
			try (DatasetTrainingSource ds = new DatasetTrainingSource();) {
				for(SOLrQuery query : queries){
					TrainingSet ts_ckan_query = ds.getTrainingSet(query.getPublisher(),query.getTags(),query.getCategories());
//					System.out.println("BEFORE ckan ts size: "+ts_ckan_query.getDf().size());
//					ts_ckan_query.remove(acquis500);
//					System.out.println("AFTER ckan ts size: "+ts_ckan_query.getDf().size());
					acquis.merge(ts_ckan_query);
				}
			}
			
			// filter top 2000 features per category
			acquis.filter(new MutualInformationSelector(k));
			System.out.println("acquis2000 size: "+acquis.getDf().size());

		}
		return acquis;
	}
}
