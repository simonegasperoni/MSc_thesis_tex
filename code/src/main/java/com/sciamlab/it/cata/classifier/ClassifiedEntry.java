package com.sciamlab.it.cata.classifier;

import java.util.List;
import java.util.Map;

import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;

public class ClassifiedEntry {

	public final List<String> featureSet;
	public final Map<Theme, Double> categories;
	
	public List<String> getFeatureSet() {
		return featureSet;
	}
	
	public Map<Theme, Double> getCategories() {
		return categories;
	}
	
	public ClassifiedEntry(List<String> featureSet, Map<Theme, Double> categories) {
		super();
		this.featureSet = featureSet;
		this.categories = categories;
	}
	public String toString(){
		String clas="classified entry: \n";
		clas=clas+this.featureSet.toString()+" \n";
		for(Theme t:categories.keySet())
			clas=clas+t.toString()+" score: "+categories.get(t)+" \n";
		return clas;
	}
	
}
