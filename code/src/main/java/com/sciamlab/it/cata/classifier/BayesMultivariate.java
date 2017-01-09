package com.sciamlab.it.cata.classifier;
import com.sciamlab.it.cata.selector.GenericFeatureSelector;
import com.sciamlab.it.cata.selector.MutualInformationSelector;
import com.sciamlab.it.cata.training.TrainingSet;

public class BayesMultivariate extends Bayes{

	public BayesMultivariate(TrainingSet trainingSet){
		GenericFeatureSelector gfs=new MutualInformationSelector();
		gfs.filter(trainingSet, 3000);
		
		this.featureToCategoryCountMap=trainingSet.getDf();
		System.out.println("df map size: "+featureToCategoryCountMap.size());
		trainingSet.createDoccounter();
		this.docCounter=trainingSet.getDoccounter();
		trainingSet.createSumDF();
		this.categoryWholeCountMap=trainingSet.getSumDF();
	}
}
