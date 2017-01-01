package com.sciamlab.it.cata.selector;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.it.cata.training.TrainingSet;

public class ChiSquareSelector extends GenericFeatureSelector {

	public void filter(TrainingSet ts) {
		System.out.println(this.getClass().toString()+": chi square calculus");
		this.buildData(ts);	
		//data: Map<String, Map<Theme, Map<String, Integer>>>
		//chi-score: Map<String, Map<Theme, Double>>
		Map<String, Map<Theme, Double>> chiscore = new HashMap<String, Map<Theme, Double>>();
		
		for(String feature:data.keySet()){
			Map<Theme, Map<String, Integer>> n=data.get(feature);
			chiscore.put(feature, new HashMap<Theme, Double>());
			for(Theme theme:n.keySet()){
				Double n00=(double)n.get(theme).get("n00");
				Double n01=(double)n.get(theme).get("n01");
				Double n10=(double)n.get(theme).get("n10");
				Double n11=(double)n.get(theme).get("n11");
				double score=((n11+n10+n01+n00)
						*Math.pow((n11*n00)-(n10*n01),2))
						/((n11+n01)*(n11+n10)*(n10+n00)*(n01+n00));
				chiscore.get(feature).put(theme, score);
			}
		}
		this.test("statist", chiscore);
		this.test("decision", chiscore);
		this.test("pesc", chiscore);
		this.test("banc", chiscore);
		this.test("finalizz", chiscore);
		this.test("font", chiscore);
		
		
		
	}
	
	public void test(String feature, Map<String, Map<Theme, Double>> chiscore){
		System.out.println(this.getClass().toString()+": chi square calculus");
		double sum=0.0; 
		System.out.println(chiscore.get(feature));
		Collection<Double> set=chiscore.get(feature).values();
		for(double d:set) sum=sum+d;
		System.out.println("average for "+feature+": "+sum/set.size());
	}

}
