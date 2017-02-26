package com.sciamlab.it.cata.feature;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.italianStemmer;

public class StemFeatureExtractor extends BasicFeatureExtractor {

	private SnowballStemmer stemmer;

	public StemFeatureExtractor() throws IOException{
		this.stemmer = (SnowballStemmer) new italianStemmer();
		this.stopwords=new HashSet<String>();
		this.setStopwords();
	}

	private String stems(String s){
		stemmer.setCurrent(s);
		stemmer.stem();
		return stemmer.getCurrent();
	}

	public List<String> execute(String text){
		String resultString = text.replaceAll("\\P{L}+", " ").toLowerCase();
		List<String> result=new ArrayList<String>();
		StringTokenizer tk=new StringTokenizer(resultString);
		while(tk.hasMoreTokens()){
			String s=tk.nextToken();
			if(!stopwords.contains(s)){
				result.add(this.stems(s));
			}
		}
		return result;
	}
}
