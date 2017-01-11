package com.sciamlab.it.cata.selector;
import java.util.HashMap;
import java.util.Map;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.it.cata.training.TrainingSet;

public class ChiSquareSelector extends GenericFeatureSelector {

	public Map<String, Map<Theme, Double>> getScores(TrainingSet ts){
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
		return chiscore;
	}
	

}
