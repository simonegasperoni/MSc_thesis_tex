package com.sciamlab.it.cata.classifier;
import com.sciamlab.it.cata.training.TrainingSet;

public class BayesMultivariate extends Bayes{

	public BayesMultivariate(TrainingSet trainingSet){
		trainingSet.createDF();
		this.featureToCategoryCountMap=trainingSet.getDf();
		trainingSet.createDoccounter();
		this.docCounter=trainingSet.getDoccounter();
		trainingSet.createSumDF();
		this.categoryWholeCountMap=trainingSet.getSumDF();
	}
}
