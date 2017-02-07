package com.sciamlab.it.cata.threshold;
import java.util.Map;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;

public abstract class Thresholder {
	public abstract Map<Theme, Double> getScore(Map<Theme, Double> res);
	public Theme getFirst(Map<Theme, Double> results){
		Theme first=Theme.ECON;
		double max=results.get(Theme.ECON);
		for(Theme t:Theme.values()){
			double current=results.get(t);
			if(current>max){
				max=current;
				first=t;
			}
		}
		return first;
	}
}

