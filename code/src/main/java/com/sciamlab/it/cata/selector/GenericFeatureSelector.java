package com.sciamlab.it.cata.selector;
import java.util.HashMap;
import java.util.Map;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.it.cata.training.TrainingSet;

//GenericFeatureSelection have to create the data structures
public abstract class GenericFeatureSelector implements FeatureSelector{
	private Map<String, Map<Theme, Integer>> df;
	private Map<Theme, Integer> doccounter;
	private Map<String, Integer> countTerms;
	protected int sizeTS;
	
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
	
	abstract public void filter(TrainingSet ts, int k);
}
