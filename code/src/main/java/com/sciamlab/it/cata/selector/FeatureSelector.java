package com.sciamlab.it.cata.selector;
import com.sciamlab.it.cata.training.TrainingSet;

public interface FeatureSelector {
	public void filter(TrainingSet ts);
}
