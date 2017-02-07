package com.sciamlab.it.cata.ckan;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.sciamlab.ckan4j.CKANApiClient;
import com.sciamlab.ckan4j.CKANApiClient.CKANApiClientBuilder;
import com.sciamlab.ckan4j.exception.CKANException;
import com.sciamlab.it.cata.classifier.PredictionEntry;

public class CKANsciamlab {
	private CKANApiClient ckan;
	public CKANsciamlab() throws MalformedURLException{
		System.out.println("Connecting to Open data hub ...");
		ckan=CKANApiClientBuilder
			.init("https://data.sciamlab.com/api/ckan/v3")
			.apiKey("")
			.build();
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
			
			JSONArray t=j.getJSONArray("tags");
			Set<String> set=new HashSet<String>();
			for(int k=0; k<t.length(); k++){
				set.add(t.getJSONObject(k).get("name").toString());
			}
			datasets.add(new PredictionEntry(title, notes, set));
		}
		return datasets;
	}
	
	public static void main(String[] args) throws MalformedURLException, CKANException{
		CKANsciamlab data=new CKANsciamlab();
		System.out.println(data.getDatasetByTag("ortofoto").size());
		
//		https://data.sciamlab.com/api/ckan/v3/action/tag_show?id=inps&key=76804e45-2d86-4ac9-8de4-4b5bc4b6b300
//		for(PredictionEntry pe:data.getDatasetByTag("ortofoto")){
//			System.out.println(pe);
//		}
	}
}
