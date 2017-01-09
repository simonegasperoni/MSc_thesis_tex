package com.sciamlab.it.cata.evaluation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.it.cata.classifier.ClassifiedEntry;
import com.sciamlab.it.cata.classifier.Classifier;
import com.sciamlab.it.cata.training.TrainingSet;
import com.sciamlab.it.cata.training.TrainingSetImpl;

public class KfoldEvaluator implements Evaluator {
	private TrainingSet trainset; 
	private int n;
	
	public KfoldEvaluator(TrainingSet trainset, int n) throws Exception{
		this.trainset=trainset;
		this.n=n;
	}
	
	
	//splits trainingset in n parts
	private List<TrainingSet> splitTrainingSet(){
		Map<String,ClassifiedEntry> docmap=trainset.getDocMap();
		Map<String,ClassifiedEntry> aux=new HashMap<String,ClassifiedEntry>();
		List<TrainingSet> sets=new ArrayList<TrainingSet>();
		int i=0;
		int lengthParts=(int)docmap.size()/n;
		for(String k:docmap.keySet()){
			aux.put(k,docmap.get(k));
			i++;
			if(i%lengthParts==0){
				sets.add(new TrainingSetImpl(aux));
				aux=new HashMap<String,ClassifiedEntry>();
			}
		}
		sets.get(0).getDocMap().putAll(aux);
		return sets;
	}
	
	//creates n trainingset for k fold cross validation
	private List<TrainingSet> createNtrainingset(List<TrainingSet> list){
		List<TrainingSet> newts=new ArrayList<TrainingSet>();
		for(int c=0; c<n; c++) newts.add(trainset.clone());
		for(int c=0; c<n; c++){
			TrainingSet t=list.get(c);
			TrainingSet nt=newts.get(c);
			for(String s:t.getDocMap().keySet()){
				nt.getDocMap().remove(s);
			}
		}
		return newts;
	}

	public void evaluate(Class<? extends Classifier> clazz) throws Exception{
		List<TrainingSet> testsetlist=this.splitTrainingSet();
		List<TrainingSet> trainsetlist=this.createNtrainingset(testsetlist);
		
		
		
		for(int c=0; c<n; c++){
			System.out.println("kfold-cross validation: #"+c);
			Classifier classifier=Classifier.Factory.build(clazz, trainsetlist.get(c));
			double tp=0; //true positive
			double tpfp=0; //true positive + false positive
			double tpfn=0; //true positive + false negative
			

			TrainingSet test=testsetlist.get(c);
			Map<String,ClassifiedEntry> docmap=test.getDocMap();
			for(String id:docmap.keySet()){
				ClassifiedEntry ce=docmap.get(id);
				List<String> list=ce.getFeatureSet();
				//System.out.println(list);
				ClassifiedEntry classified=classifier.predict(list, 1.0);
				//System.out.println(classified.getCategories().size());;

				for(Theme t:classified.getCategories().keySet()){
					if(ce.getCategories().containsKey(t)) tp++;
				}	
				tpfp=tpfp+classified.getCategories().size();
				tpfn=tpfn+ce.getCategories().size();
				
			} 
			double precision=new Double(tp/tpfp);
			double recall=new Double(tp/tpfn);
			System.out.println("-----------------------------------------");
			System.out.println("current evaluation");
			System.out.println("-----------------------------------------");
			System.out.println("precision   ---->  "+new Double(tp/tpfp));
			System.out.println("recall      ---->  "+new Double(tp/tpfn));
			System.out.println("f1-measure  ---->  "+new Double((2*precision*recall)/(precision+recall)));
			System.out.println("-----------------------------------------");
			
		}

		
	}
	
	@Override
	public double getPrecision() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getRecall() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getAccuracy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getFmeasure() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static void main(String args[]){
		
	}

}
