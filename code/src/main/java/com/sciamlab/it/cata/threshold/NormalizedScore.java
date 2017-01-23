package com.sciamlab.it.cata.threshold;
import java.util.HashMap;
import java.util.Map;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;

public class NormalizedScore implements Thresholder {
	private double threshold;
	
	public NormalizedScore(double threshold){
		this.threshold=threshold;
	}
	
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
	
	public double getThreshold() {
		return threshold;
	}
	
	public Map<Theme, Double> getScore(Map<Theme, Double> results) {
		Map<Theme, Double> r=new HashMap<Theme, Double>();
		
		double sum=0.0;
		for(Double d:results.values()) sum=sum+d; 
		
		for(Theme t:Theme.values()) 
			if((results.get(t)/sum)>=threshold) r.put(t,results.get(t)/sum);
		return r;
	}

}
