package com.sciamlab.it.cata.feature;

import java.util.List;

import com.sciamlab.it.cata.classifier.PredictionEntry;

public interface FeatureExtractor {

	public List<String> extract(PredictionEntry entry) throws Exception;
	public List<String> execute(String corpus) throws Exception;
	
}
