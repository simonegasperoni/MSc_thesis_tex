package com.sciamlab.it.cata.classifier;
import com.sciamlab.it.cata.training.TrainingSet;

public class BayesMultinomialWF extends BayesMultinomial{

	public BayesMultinomialWF(TrainingSet trainingSet){
		this.featureSelector.filter(trainingSet);
		
		this.featureToCategoryCountMap=trainingSet.getDf();
		System.out.println("df map size: "+featureToCategoryCountMap.size());
		trainingSet.createTF();
		this.featureToCategoryCountMap=trainingSet.getTf();
		System.out.println("tf map size: "+featureToCategoryCountMap.size());
		
		trainingSet.createDoccounter();
		this.docCounter=trainingSet.getDoccounter();
		trainingSet.createSumTF();
		this.categoryWholeCountMap=trainingSet.getSumTF();
	}
}
