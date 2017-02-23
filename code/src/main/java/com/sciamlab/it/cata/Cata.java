package com.sciamlab.it.cata;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import org.apache.log4j.Logger;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.common.util.SciamlabStreamUtils;
import com.sciamlab.it.cata.classifier.Classifier;
import com.sciamlab.it.cata.classifier.PredictionEntry;
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

	public static void testLECCE() {
		// OpenDataHubTest test=new OpenDataHubTest(ts);
		// test.evaluate(BayesMultinomialWF.class);
	}

	public static void main(String[] args) throws Exception {

		try (TrainingSource acquisTrainingSource = new AcquisTrainingSource();) {
			
			TrainingSet acquis = acquisTrainingSource.getTrainingSet();
			System.out.println("acquis size: "+acquis.getDf().size());
			
			// filter top 500 features per category
			TrainingSet acquis500 = acquis.clone().filter(new ChiSquareSelector(500));
			System.out.println("acquis500 size: "+acquis500.getDf().size());
			
			@SuppressWarnings("serial")
			Map<String, Theme> ckan_queries = new HashMap<String, Theme>(){{
				put("boschi", Theme.ENVI);
				put("ortofoto", Theme.TECH);
				
			}};
			try (DatasetTrainingSource ds = new DatasetTrainingSource();) {
				for(Entry<String, Theme> query : ckan_queries.entrySet()){
					TrainingSet ts_ckan_query = ds.getTrainingSet(query.getKey(), query.getValue());
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

			PredictionEntry pe = new PredictionEntry.Builder("Ambiti inedificabili art.33 - Boschi - Gaby Carta delle "
					+ "aree boscate (ai sensi della L.R. n. 11 del 6 aprile 1998 art. 33). "
					+ "Il dato e stato approvato con deliberazione della Giunta regionale n 3572 del 28 10 2005. "
					+ "SCOPO: Individuare quelle aree in cui gli interventi edilizi o trasformativi sono vietati "
					+ "o regolamentati, ai sensi delle norme contenute al Titolo V, Capo I della L.R. 11 98. art. 33 boschi ").build();

			Theme ce1 = classifier.predictFirst(pe, new StemFeatureExtractor());
			System.out.println(ce1);
			
			PredictionEntry pe2 = new PredictionEntry.Builder(" Ppr - Zona fluviale interna (tav. P4)"
					+ "Il dato, areale, perimetra le zone fluviali interne, costituite dalle fasce A e B del PAI e "
					+ "dalle sponde o piedi degli argini per una fascia di 150 m ciascuna di fiumi, torrenti, "
					+ "corsi d'acqua iscritti negli elenchi previsti dal R.D. n. 1775 1933 fiumi fluviale "
					+ "idrografia interna opendata ppr rete idrografica rndt zona ").build();

			Theme ce2 = classifier.predictFirst(pe2, new StemFeatureExtractor());
			System.out.println(ce2);
		}

	}
	
}
