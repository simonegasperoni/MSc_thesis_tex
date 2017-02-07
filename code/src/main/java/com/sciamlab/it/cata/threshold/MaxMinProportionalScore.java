package com.sciamlab.it.cata.threshold;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;

public class MaxMinProportionalScore extends Thresholder {

	private double proportion;
	
	public MaxMinProportionalScore(double proportion){
		this.proportion=proportion;
	}
	
	public void setProportion(double proportion) {
		this.proportion=proportion;
	}
	
	public double getProportion() {
		return proportion;
	}
	
	@Override
	public Map<Theme, Double> getScore(Map<Theme, Double> results) {
		Map<Theme, Double> r=new HashMap<Theme, Double>();
		
		Double max=Collections.max(results.values());
		Double min=Collections.min(results.values());
		Double thr=max-((max-min)*(proportion));

		for(Theme t:Theme.values()) 
			if(results.get(t)>=thr) r.put(t, results.get(t));

		return r;
	}
}
