package com.sciamlab.it.cata.training;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.it.cata.ckan.CKANsciamlab;
import com.sciamlab.it.cata.classifier.ClassifiedEntry;
import com.sciamlab.it.cata.classifier.PredictionEntry;
import com.sciamlab.it.cata.feature.FeatureExtractor;
import com.sciamlab.it.cata.feature.StemFeatureExtractor;

public class DatasetTrainingSource implements TrainingSource {

	private Map<String, ClassifiedEntry> docMap;
	private TrainingSet trainingSet;
	private List<PredictionEntry> dataset;
	private String tag;
	private Theme t;
	private FeatureExtractor fe;
	
	public DatasetTrainingSource(String tag, Theme t) throws Exception{
		docMap= new HashMap<String, ClassifiedEntry>();
		dataset=new ArrayList<PredictionEntry>();
		fe=new StemFeatureExtractor();
		this.tag=tag;
		this.t=t;
		loadData();
	}
	
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadData() throws Exception {
		// TODO Auto-generated method stub
		CKANsciamlab ckan=new CKANsciamlab();
		int i=0;
		dataset=ckan.getDatasetByTag(tag);
		for(PredictionEntry pe:dataset){
			List<String> features=fe.extract(pe);
			HashMap<Theme, Double> map=new HashMap<Theme, Double>();
			map.put(t,0.0);
			docMap.put(t+": "+i,new ClassifiedEntry(features, map));
			i++;
		}
		
	}

	public TrainingSet getTrainingSet() {
		trainingSet = new TrainingSetImpl(docMap);
		return this.trainingSet;		
	}
	
}
