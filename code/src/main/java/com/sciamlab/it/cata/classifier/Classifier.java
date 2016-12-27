package com.sciamlab.it.cata.classifier;
import java.util.List;

import com.sciamlab.it.cata.feature.FeatureExtractor;
import com.sciamlab.it.cata.training.TrainingSet;

public interface Classifier {
	public ClassifiedEntry predict(PredictionEntry entry, double threshold, FeatureExtractor fe) throws Exception;
	public ClassifiedEntry predict(List<String> featuresToPredict, double threshold) throws Exception;
	public static class Factory{
		public static  Classifier build(
				Class<? extends Classifier> clazz, TrainingSet trainingSet) throws Exception{
			return (Classifier) clazz.getConstructor(TrainingSet.class).newInstance(trainingSet);
		}
	}
}
