package com.sciamlab.it.cata.training;

public interface TrainingSource extends AutoCloseable{
	public void close() throws Exception;
	public void loadData() throws Exception; 
	public TrainingSet getTrainingSet();
	
}
