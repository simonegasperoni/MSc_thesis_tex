package com.sciamlab.it.cata.training;
import java.util.Map;
import java.util.Set;

import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.it.cata.classifier.ClassifiedEntry;
import com.sciamlab.it.cata.selector.GenericFeatureSelector;

public interface TrainingSet {

	//term frequency
	public Map<String, Map<Theme, Integer>> createTF();
	//document frequency
	public Map<String, Map<Theme, Integer>> createDF();
	//sums TFs/DFs per category
	public Map<Theme, Integer> createSumTF();
	public Map<Theme, Integer> createSumDF();
	//category-documents
	public Map<Theme, Integer> createDoccounter();
	//document frequency per term
	public void createTermOccurences();
	
	public TrainingSet clone();
	public TrainingSet merge(TrainingSet t);
	public TrainingSet remove(TrainingSet ts);
	public TrainingSet filter(GenericFeatureSelector fs);
	public TrainingSet remove(Set<String> stopfeatures);
	
	
	
	//getters
	public Map<String, ClassifiedEntry> getDocMap();
	public Map<String, Map<Theme, Integer>> getDf();
	public Map<String, Map<Theme, Integer>> getTf();
	public Map<Theme, Integer> getDoccounter();
	public Map<Theme, Integer> getSumDF();
	public Map<Theme, Integer> getSumTF();
	public Map<String, Integer> getTermOccurences();
	

}
