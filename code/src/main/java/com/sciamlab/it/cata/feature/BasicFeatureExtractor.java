package com.sciamlab.it.cata.feature;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.tartarus.snowball.SnowballStemmer;
import com.sciamlab.it.cata.classifier.PredictionEntry;
import opennlp.tools.util.InvalidFormatException;

public class BasicFeatureExtractor implements FeatureExtractor {

	private HashSet<String> stopwords;
	private SnowballStemmer stemmer;

	public BasicFeatureExtractor() throws InvalidFormatException, IOException{
		this.stopwords=new HashSet<String>();
		this.setStopwords();
	}

	//some stemmed stopwords
	private void setStopwords(){
		
		stopwords.add(this.stems("gennaio"));
		stopwords.add(this.stems("febbraio"));
		stopwords.add(this.stems("marzo"));
		stopwords.add(this.stems("aprile"));
		stopwords.add(this.stems("maggio"));
		stopwords.add(this.stems("giugno"));
		stopwords.add(this.stems("luglio"));
		stopwords.add(this.stems("agosto"));
		stopwords.add(this.stems("settembre"));
		stopwords.add(this.stems("ottobre"));
		stopwords.add(this.stems("novembre"));
		stopwords.add(this.stems("dicembre"));
		stopwords.add(this.stems("lunedì"));
		stopwords.add(this.stems("martedì"));
		stopwords.add(this.stems("mercoledì"));
		stopwords.add(this.stems("giovedì"));
		stopwords.add(this.stems("venerdì"));
		stopwords.add(this.stems("sabato"));
		stopwords.add(this.stems("domenica"));
		stopwords.add(this.stems("pagina"));
		stopwords.add(this.stems("pag"));
		stopwords.add(this.stems("allegato"));
		stopwords.add(this.stems("allegare"));
	}

	
	private String stems(String s){
		stemmer.setCurrent(s);
		stemmer.stem();
		return stemmer.getCurrent();
	}


	//main method for feature extraction
	public List<String> execute(String text) throws InvalidFormatException, IOException{
		List<String> result=new ArrayList<String>();		
		
		return result;
	}
	
	public List<String> extract(PredictionEntry entry) {
		List<String> res= new ArrayList<String>();
		
		return res;
	}

	//test
	public static void main(String[] args) throws InvalidFormatException, IOException {

	}

}
