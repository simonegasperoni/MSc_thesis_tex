package com.sciamlab.it;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.it.cata.classifier.ClassifiedEntry;
import com.sciamlab.it.cata.selector.ChiSquareSelector;
import com.sciamlab.it.cata.training.AcquisTrainingSource;
import com.sciamlab.it.cata.training.TrainingSet;

public class Ngenerator {
	
//	public void getN(String feature, Map<String, Map<Theme, Double>> map){
//		System.out.println(this.getClass().toString()+": chi square calculus");
//		double sum=0.0; 
//		System.out.println(map.get(feature));
//		Collection<Double> set=map.get(feature).values();
//		for(double d:set) sum=sum+d;
//		System.out.println("average for "+feature+": "+sum/set.size());
//	}
	
	public Map<String, Integer> counter(TrainingSet set, String term, Theme category){
		Map<String, Integer> map=new HashMap<String, Integer>();
		map.put("n00", 0);
		map.put("n01", 0);
		map.put("n10", 0);
		map.put("n11", 0);
		
		Map<String, ClassifiedEntry> docmap=set.getDocMap();
		for(String id:docmap.keySet()){
			Set<Theme> categories=docmap.get(id).getCategories().keySet();
			List<String> features=docmap.get(id).getFeatureSet();
			if(!categories.contains(category)&&!features.contains(term)) map.put("n00", map.get("n00")+1);
			else if(!categories.contains(category)&&features.contains(term)) map.put("n10", map.get("n10")+1);
			else if(categories.contains(category)&&!features.contains(term)) map.put("n01", map.get("n01")+1);
			else if(categories.contains(category)&&features.contains(term)) map.put("n11", map.get("n11")+1);
		}
		return map;
	}
	
	@Test
	public void test() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
		final String term="banc";
		final Theme category=Theme.ENVI;
		
		@SuppressWarnings("resource")
		AcquisTrainingSource acquisTrainingSource = new AcquisTrainingSource();
		TrainingSet ts=acquisTrainingSource.getTrainingSet();
		ChiSquareSelector gfs=new ChiSquareSelector(2000);
		gfs.buildData(ts);
		Map<String, Map<Theme, Map<String, Integer>>> map=gfs.getData();
		Map<String,Integer> method=map.get(term).get(category);
		Map<String,Integer> tester=this.counter(ts, term, category);
		System.out.println(method);
		System.out.println(tester);
		Assert.assertEquals(method.get("n00"), tester.get("n00"));
		Assert.assertEquals(method.get("n01"), tester.get("n01"));
		Assert.assertEquals(method.get("n10"), tester.get("n10"));
		Assert.assertEquals(method.get("n11"), tester.get("n11"));
	}

}
