package com.sciamlab.it.cata.classifier;
import com.sciamlab.it.cata.feature.FeatureExtractor;
import com.sciamlab.it.cata.training.TrainingSet;

public interface Classifier {
	public ClassifiedEntry predict(PredictionEntry entry, double score) throws Exception;

	public static class Factory{
		public static  Classifier build(
				Class<? extends Classifier> clazz, TrainingSet trainingSet, FeatureExtractor featureExtractor) throws Exception{
			return (Classifier) clazz.getConstructor(TrainingSet.class, FeatureExtractor.class).newInstance(trainingSet, featureExtractor);
		}
	}
}
