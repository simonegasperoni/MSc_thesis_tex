package com.sciamlab.it.cata.selector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.it.cata.training.TrainingSet;

//GenericFeatureSelection have to create the data structures
public abstract class GenericFeatureSelector implements FeatureSelector{
	private Map<String, Map<Theme, Integer>> df;
	private Map<Theme, Integer> doccounter;
	private Map<String, Integer> countTerms;
	protected int sizeTS;
	protected int num;
	
	public void featuresPerCategory(int num) {
		this.num = num;
	}
	
	public int getFeaturePerCategory() {
		return num;
	}
	
//MAP: 
//<feature1, 
//				Theme 1:<'N11':int, 'N01':int, 'N00':int, 'N10':int>
//				...
//				Theme n:<'N11':int, 'N01':int, 'N00':int, 'N10':int>>
//<feature2, 
//				Theme 1:<'N11':int, 'N01':int, 'N00':int, 'N10':int>
//				...
//				Theme n:<'N11':int, 'N01':int, 'N00':int, 'N10':int>>
	
	protected Map<String, Map<Theme, Map<String, Integer>>> data;
	
	public void buildData(TrainingSet ts){
		System.out.println(this.getClass().toString()+": building data struct");
		ts.createDF();
		this.df=ts.getDf();
		
		ts.createDoccounter();
		this.doccounter=ts.getDoccounter();
		
		ts.createTermOccurences();
		this.countTerms=ts.getTermOccurences();
		
		//size of Training set
		this.sizeTS=ts.getDocMap().size();
		
		data=new HashMap<String, Map<Theme, Map<String, Integer>>>();	
		for(String f:df.keySet()){
			HashMap<Theme,Map<String,Integer>> aux=new HashMap<Theme,Map<String,Integer>>();
			int termsOcc=countTerms.get(f);
			for(Theme t:df.get(f).keySet()){
				Map<String,Integer> n=new HashMap<String,Integer>();
				int n11=df.get(f).get(t);
				n.put("n11", df.get(f).get(t)-1);
				int n01=doccounter.get(t)-(df.get(f).get(t)-1);
				n.put("n01", n01);
				int n10=termsOcc-(df.get(f).get(t)-1);
				n.put("n10", n10);
				n.put("n00", sizeTS-n11-n01-n10+1);
				aux.put(t, n);
			}
			data.put(f, aux);
		}
	}
	
	public Map<String, Map<Theme, Map<String, Integer>>> getData() {
		return data;
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
	
	public void filter(TrainingSet ts) {
		System.out.println(this.getClass().toString()+": feature filtering");

		
		Map<String, Map<Theme, Double>> score=this.getScores(ts);
		Map<Theme, Double> thresholds=this.getThresholds(score, num);
		
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
	public  abstract Map<String, Map<Theme, Double>> getScores(TrainingSet ts);
}
