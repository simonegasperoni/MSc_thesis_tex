package com.sciamlab.it.cata.selector;
import java.util.Set;

import com.sciamlab.it.cata.training.TrainingSet;

public interface FeatureSelector {
	public Set<String> getFilteredFeatures(TrainingSet ts);
}
