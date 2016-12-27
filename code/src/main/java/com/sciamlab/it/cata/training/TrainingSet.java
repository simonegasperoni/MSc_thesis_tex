package com.sciamlab.it.cata.training;
import java.util.Map;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.it.cata.classifier.ClassifiedEntry;

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
	
	//getters
	public Map<String, ClassifiedEntry> getDocMap();
	public Map<String, Map<Theme, Integer>> getDf();
	public Map<String, Map<Theme, Integer>> getTf();
	public Map<Theme, Integer> getDoccounter();
	public Map<Theme, Integer> getSumDF();
	public Map<Theme, Integer> getSumTF();
	public TrainingSet clone();
	

}
