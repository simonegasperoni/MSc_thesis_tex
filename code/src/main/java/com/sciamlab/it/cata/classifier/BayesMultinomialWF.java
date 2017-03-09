package com.sciamlab.it.cata.classifier;
import java.io.IOException;

import com.sciamlab.it.cata.training.TrainingSet;

public class BayesMultinomialWF extends BayesMultinomial{

	public BayesMultinomialWF(TrainingSet trainingSet) throws IOException{
		super();
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
