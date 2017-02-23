package com.sciamlab.it.cata.training;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sciamlab.ckan4j.CKANApiClient;
import com.sciamlab.ckan4j.CKANApiClient.CKANApiClientBuilder;
import com.sciamlab.ckan4j.exception.CKANException;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.it.cata.classifier.ClassifiedEntry;
import com.sciamlab.it.cata.classifier.PredictionEntry;
import com.sciamlab.it.cata.feature.FeatureExtractor;
import com.sciamlab.it.cata.feature.StemFeatureExtractor;

public class DatasetTrainingSource implements TrainingSource {

//	private Map<String, ClassifiedEntry> docMap;
	private TrainingSet trainingSet;
//	private String tag;
//	private Theme t;
	private FeatureExtractor fe;
	
	private CKANApiClient ckan;
	
	public DatasetTrainingSource() throws Exception{
		this.fe = new StemFeatureExtractor();
//		loadData();
		
		System.out.println("Connecting to Open data hub ...");
		this.ckan = CKANApiClientBuilder
			.init("https://data.sciamlab.com/api/ckan/v3")
			.apiKey("76804e45-2d86-4ac9-8de4-4b5bc4b6b300")
			.build();
	}
	
//	public void setDocMap(Map<String, ClassifiedEntry> docMap) {
//		this.docMap = docMap;
//	}
	
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadData() throws Exception {
		
		
	}

	public TrainingSet getTrainingSet(Object... objects) throws Exception {
		String tag = (String) objects[0];
		Theme theme = (Theme) objects[1];
		Map<String, ClassifiedEntry> docMap = new HashMap<String, ClassifiedEntry>();
		int i=0;
		List<PredictionEntry> dataset = this.getDatasetByTag(tag);
		for(PredictionEntry pe : dataset){
			List<String> features = fe.extract(pe);
			HashMap<Theme, Double> map=new HashMap<Theme, Double>();
			map.put(theme,0.0);
			docMap.put(tag+": "+i,new ClassifiedEntry(features, map));
			i++;
		}
		trainingSet = new TrainingSetImpl(docMap);
		return this.trainingSet;		
	}
	
	public List<PredictionEntry> getDatasetByTag(String tag) throws JSONException, CKANException{
		List<PredictionEntry> datasets=new ArrayList<PredictionEntry>();
		JSONArray jsa=new JSONArray();
		jsa=ckan.tagShow(tag, true).getJSONArray("packages");
		for(int i=0; i<jsa.length(); i++){
			
			JSONObject j=jsa.getJSONObject(i);
			//System.out.println(j.get("id"));
			String title=j.get("title").toString();
			String notes=j.get("notes").toString();
			String id=j.get("name").toString();
			
			JSONArray t=j.getJSONArray("tags");
			Set<String> tags=new HashSet<String>();
			for(int k=0; k<t.length(); k++){
				tags.add(t.getJSONObject(k).get("name").toString());
			}
			datasets.add(new PredictionEntry.Builder(notes).title(title).tag(tags).id(id).build());
		}
		return datasets;
	}
	
	public static void main(String[] args) throws Exception{
		DatasetTrainingSource data = new DatasetTrainingSource();
		System.out.println(data.getDatasetByTag("ortofoto").size());
		
//		https://data.sciamlab.com/api/ckan/v3/action/tag_show?id=inps&key=76804e45-2d86-4ac9-8de4-4b5bc4b6b300
//		for(PredictionEntry pe:data.getDatasetByTag("ortofoto")){
//			System.out.println(pe);
//		}
	}
	
}
