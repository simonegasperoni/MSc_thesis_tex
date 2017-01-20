package com.sciamlab.it.cata.classifier;
import com.sciamlab.it.cata.selector.ChiSquareSelector;
import com.sciamlab.it.cata.selector.GenericFeatureSelector;
import com.sciamlab.it.cata.training.TrainingSet;

public class BayesMultinomialWO extends BayesMultinomial{

	public BayesMultinomialWO(TrainingSet trainingSet){
		GenericFeatureSelector gfs=new ChiSquareSelector();
		gfs.filter(trainingSet, 3000);
		
		this.featureToCategoryCountMap=trainingSet.getDf();
		System.out.println("df map size: "+featureToCategoryCountMap.size());
		trainingSet.createDoccounter();
		this.docCounter=trainingSet.getDoccounter();
		trainingSet.createSumDF();
		this.categoryWholeCountMap=trainingSet.getSumDF();
	}
}
