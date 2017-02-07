package com.sciamlab.it.cata.feature;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

public class NoStemFeatureExtractor extends BasicFeatureExtractor {

	public NoStemFeatureExtractor() throws IOException{
		this.stopwords=new HashSet<String>();
		this.setStopwords();
	}

	public List<String> execute(String text){
		String resultString = text.replaceAll("\\P{L}+", " ").toLowerCase();
		List<String> result=new ArrayList<String>();
		StringTokenizer tk=new StringTokenizer(resultString);
		while(tk.hasMoreTokens()){
			String s=tk.nextToken();
			if(!stopwords.contains(s)){
				result.add(s);
			}
		}
		return result;
	}

}
