package com.sciamlab.it.cata.selector;
import java.util.HashMap;
import java.util.Map;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.it.cata.training.TrainingSet;

public class MutualInformationSelector extends GenericFeatureSelector {

	public MutualInformationSelector(int num){
		this.num=num;
	}
	
	private static Double log2(Double n){
		Double res=Math.log10(n)/Math.log10(2);
		return res;
	}
	
	public Map<String, Map<Theme, Double>> getScores(TrainingSet ts){
		System.out.println(this.getClass().toString()+": mutual information calculus");
		this.buildData(ts);
		Map<String, Map<Theme, Double>> mi = new HashMap<String, Map<Theme, Double>>();
		
		for(String feature:data.keySet()){
			Map<Theme, Map<String, Integer>> n=data.get(feature);
			mi.put(feature, new HashMap<Theme, Double>());
			for(Theme theme:n.keySet()){
				Double x00=0.0;
				Double x01=0.0;
				Double x10=0.0;
				Double x11=0.0;
				Double n00=new Double(n.get(theme).get("n00"));
				Double n01=new Double(n.get(theme).get("n01"));
				Double n10=new Double(n.get(theme).get("n10"));
				Double n11=new Double(n.get(theme).get("n11"));
				
				if(n11!=0) x11=(n11/sizeTS)*log2(sizeTS*n11/((n11+n01)*(n11+n10)));
				if(n10!=0) x10=(n10/sizeTS)*log2(sizeTS*n10/((n10+n00)*(n11+n10)));
				if(n01!=0) x01=(n01/sizeTS)*log2(sizeTS*n01/((n11+n01)*(n01+n00)));
				if(n00!=0) x00=(n00/sizeTS)*log2(sizeTS*n00/((n10+n00)*(n01+n00)));
				Double score=x11+x00+x01+x10;
				
				mi.get(feature).put(theme, score);
			}
		}
		
		return mi;
	}
	


}
