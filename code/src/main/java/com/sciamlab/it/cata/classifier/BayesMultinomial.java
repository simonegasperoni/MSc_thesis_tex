package com.sciamlab.it.cata.classifier;
import com.sciamlab.it.cata.training.TrainingSet;

public class BayesMultinomial extends Bayes{

	public BayesMultinomial(TrainingSet trainingSet){
		trainingSet.createTF();
		this.featureToCategoryCountMap=trainingSet.getTf();
		trainingSet.createDoccounter();
		this.docCounter=trainingSet.getDoccounter();
		trainingSet.createSumTF();
		this.categoryWholeCountMap=trainingSet.getSumTF();
	}
}
