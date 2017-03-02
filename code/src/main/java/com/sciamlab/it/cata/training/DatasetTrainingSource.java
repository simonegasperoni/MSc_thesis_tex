package com.sciamlab.it.cata.training;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.common.util.HTTPClient;
import com.sciamlab.it.cata.classifier.ClassifiedEntry;
import com.sciamlab.it.cata.classifier.PredictionEntry;
import com.sciamlab.it.cata.feature.FeatureExtractor;
import com.sciamlab.it.cata.feature.StemFeatureExtractor;

public class DatasetTrainingSource implements TrainingSource {
	private TrainingSet trainingSet;
	private FeatureExtractor fe;
	private String publisherq;
	private List<String> tagsq;
	private Map<Theme,Double> catmap;
	
	public DatasetTrainingSource() throws IOException{
		fe=new StemFeatureExtractor();
	}
	
	@Override
	public void close() throws Exception {}

	@Override
	public void loadData() throws Exception {}

	//publisher, tag, categories
	public TrainingSet getTrainingSet(Object... objects) throws Exception {

		this.publisherq=(String)objects[0];
		this.tagsq=(ArrayList<String>)objects[1];
		Set<Theme> categories=(HashSet<Theme>)objects[2];
		catmap=new HashMap<Theme,Double>();
		
		for(Theme t:categories){
			catmap.put(t, 0.0);
		}
		
		Map<String, ClassifiedEntry> docMap = new HashMap<String, ClassifiedEntry>();
		
		List<PredictionEntry> dataset = this.getDatasetsBySOLr();
		for(PredictionEntry pe : dataset){
			//System.out.println(pe);
			List<String> features = fe.extract(pe);
			docMap.put(pe.id,new ClassifiedEntry(features, catmap));
		}
		//System.out.println(docMap.size());
		trainingSet = new TrainingSetImpl(docMap);
		return this.trainingSet;		
	}
	
	//if publisherq=null then it doesn't select by publisher
	public List<PredictionEntry> getDatasetsBySOLr() throws MalformedURLException{
		String url="http://www.sciamlab.com/solr/collection1/select?q=(";
		for(String tag:tagsq){
			url=url+"tags%3A"+tag+"+OR+";
		}
		url=url.substring(0, url.length()-4);
		url=url+")";
		if(publisherq!=null)
			url=url+"+AND+extras_publisher%3A"+publisherq;
		url=url+"&rows=2147483647&fl=name%2C+title%2Cnotes%2C+tags%2C+extras_publisher&wt=json&indent=true";
		
		//System.out.println(url);
		List<PredictionEntry> resqsolr=new ArrayList<PredictionEntry>();
		String json=new HTTPClient().doGET(new URL(url)).readEntity(String.class);
		JSONObject res=new JSONObject(json);
		
		//System.out.println(json);
		JSONArray response=res.getJSONObject("response").getJSONArray("docs");
		
		for(int i=0;i<response.length();i++){
			JSONObject obj= response.getJSONObject(i);
			
			JSONArray t=obj.getJSONArray("tags");
			Set<String> tags=new HashSet<String>();
			for(int k=0; k<t.length(); k++){
				tags.add(t.getString(k));
			}
			
			String id=obj.getString("name");
			//System.out.println(id);
			String title=obj.getString("title");
			String description=obj.getString("notes");
			
			String publisher=obj.getString("extras_publisher");

			resqsolr.add(new PredictionEntry.Builder(description).title(title).tag(tags).id(id).publisher(publisher).build());
		}
		
		System.out.println(this.toString()+", size select on SOLr(CKAN):"+resqsolr.size()+" [datasets], by tags:"+tagsq+", publisher:"+publisherq);
		return resqsolr;
		
	}
	
}
