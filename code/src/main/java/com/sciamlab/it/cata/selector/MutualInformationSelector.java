package com.sciamlab.it.cata.selector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.it.cata.training.TrainingSet;

public class MutualInformationSelector extends GenericFeatureSelector {

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
	
	private Map<Theme, Double> getThresholds(Map<String, Map<Theme, Double>> mi, int k){
		Map<Theme, Double> thresholds=new HashMap<Theme, Double>();
		Map<Theme, List<Double>> values=new HashMap<Theme, List<Double>>();
		
		values.put(Theme.AGRI,new ArrayList<Double>());
		values.put(Theme.ENER,new ArrayList<Double>());
		values.put(Theme.GOVE,new ArrayList<Double>());
		values.put(Theme.INTR,new ArrayList<Double>());
		values.put(Theme.JUST,new ArrayList<Double>());
		values.put(Theme.ECON,new ArrayList<Double>());
		values.put(Theme.SOCI,new ArrayList<Double>());
		values.put(Theme.EDUC,new ArrayList<Double>());
		values.put(Theme.TECH,new ArrayList<Double>());
		values.put(Theme.TRAN,new ArrayList<Double>());
		values.put(Theme.ENVI,new ArrayList<Double>());
		values.put(Theme.REGI,new ArrayList<Double>());
		values.put(Theme.HEAL,new ArrayList<Double>());
		

		for(Theme t:Theme.values()){
			for(String feature:mi.keySet()){
				List<Double> list=values.get(t);
				list.add(mi.get(feature).get(t));
			}
			Collections.sort(values.get(t));
			Collections.reverse(values.get(t));
		}
				
		for(Theme t:Theme.values()){
			List<Double> list=values.get(t);
			thresholds.put(t, list.get(k));
		}
		
		return thresholds;
	}
	
	public void filter(TrainingSet ts, int k) {
		System.out.println(this.getClass().toString()+": mutual information filtering");

		
		Map<String, Map<Theme, Double>> score=this.getScores(ts);
		Map<Theme, Double> thresholds=this.getThresholds(score, k);
		
		//System.out.println(thresholds);
		Map<String,Map<Theme,Integer>> df=ts.getDf();
		
		for(String feature:score.keySet()){
			
			 Map<Theme,Double>map =score.get(feature);
			 if(map.get(Theme.AGRI)>thresholds.get(Theme.AGRI)) continue;
			 if(map.get(Theme.ENER)>thresholds.get(Theme.ENER)) continue;
			 if(map.get(Theme.GOVE)>thresholds.get(Theme.GOVE)) continue;
			 if(map.get(Theme.INTR)>thresholds.get(Theme.INTR)) continue;
			 if(map.get(Theme.JUST)>thresholds.get(Theme.JUST)) continue;
			 if(map.get(Theme.ECON)>thresholds.get(Theme.ECON)) continue;
			 if(map.get(Theme.SOCI)>thresholds.get(Theme.SOCI)) continue;
			 if(map.get(Theme.EDUC)>thresholds.get(Theme.EDUC)) continue;
			 if(map.get(Theme.TECH)>thresholds.get(Theme.TECH)) continue;
			 if(map.get(Theme.TRAN)>thresholds.get(Theme.TRAN)) continue;
			 if(map.get(Theme.ENVI)>thresholds.get(Theme.ENVI)) continue;
			 if(map.get(Theme.REGI)>thresholds.get(Theme.REGI)) continue;
			 if(map.get(Theme.HEAL)>thresholds.get(Theme.HEAL)) continue;
			 //System.out.println("!!");
			 df.remove(feature);
		 }
	}

}
