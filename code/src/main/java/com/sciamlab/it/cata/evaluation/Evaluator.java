package com.sciamlab.it.cata.evaluation;

import com.sciamlab.it.cata.classifier.Classifier;

public interface Evaluator {
	public void evaluate(Class<? extends Classifier> clazz) throws Exception;
	public double getPrecision();
	public double getRecall();
	public double getAccuracy();
	public double getFmeasure();
}
