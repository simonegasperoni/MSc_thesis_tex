package com.sciamlab.it.cata.classifier;
import java.io.IOException;

import com.sciamlab.it.cata.training.TrainingSet;

public class BayesMultinomialWO extends BayesMultinomial{

	public BayesMultinomialWO(TrainingSet trainingSet) throws IOException{
		super();
		this.featureToCategoryCountMap=trainingSet.getDf();
		System.out.println("df map size: "+featureToCategoryCountMap.size());
		trainingSet.createDoccounter();
		this.docCounter=trainingSet.getDoccounter();
		trainingSet.createSumDF();
		this.categoryWholeCountMap=trainingSet.getSumDF();
	}
}
