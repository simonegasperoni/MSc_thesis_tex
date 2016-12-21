package com.sciamlab.it.cata.feature;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.italianStemmer;
import com.sciamlab.it.cata.classifier.PredictionEntry;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

public class OpenNlpExtractor implements FeatureExtractor {

	private SnowballStemmer stemmer;
	private InputStream is;
	private TokenizerModel tokmodel;
	private Tokenizer tokenizer;
	private InputStream modelIn;
	private POSTaggerME tagger;
	private HashSet<String> ignoreset;
	private HashSet<String> stopwords;

	
	//model for opennlp
	//
	//    models/it-token.bin
	//    models/it-pos-maxent.bin
	
	//Italian feature extraction
	public OpenNlpExtractor(String tokenmodel, String posmodel) throws InvalidFormatException, IOException{
		
		this.ignoreset = new HashSet<String>();
		this.stemmer = (SnowballStemmer) new italianStemmer();
		this.is = new FileInputStream(tokenmodel);
		this.tokmodel = new TokenizerModel(is);
		this.tokenizer = new TokenizerME(tokmodel);
		this.modelIn = new FileInputStream(posmodel);
		POSModel model = new POSModel(modelIn);
		this.tagger = new POSTaggerME(model);
		this.is.close();
		this.modelIn.close();
		this.stopwords=new HashSet<String>();
		this.setStopwords();
	}
	
	public OpenNlpExtractor() throws InvalidFormatException, IOException{
		
		this.ignoreset = new HashSet<String>();
		this.stemmer = (SnowballStemmer) new italianStemmer();
		this.is = new FileInputStream("models/it-token.bin");
		this.tokmodel = new TokenizerModel(is);
		this.tokenizer = new TokenizerME(tokmodel);
		this.modelIn = new FileInputStream("models/it-pos-maxent.bin");
		POSModel model = new POSModel(modelIn);
		this.tagger = new POSTaggerME(model);
		this.is.close();
		this.modelIn.close();
		this.stopwords=new HashSet<String>();
		this.setStopwords();
		this.ignoreSet(OpenNlpExtractor.ONLY_NAMES_VERBS_ADJECTIVES);
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
		stopwords.add(this.stems("luned�"));
		stopwords.add(this.stems("marted�"));
		stopwords.add(this.stems("mercoled�"));
		stopwords.add(this.stems("gioved�"));
		stopwords.add(this.stems("venerd�"));
		stopwords.add(this.stems("sabato"));
		stopwords.add(this.stems("domenica"));
		stopwords.add(this.stems("pagina"));
		stopwords.add(this.stems("pag"));
		stopwords.add(this.stems("allegato"));
		stopwords.add(this.stems("allegare"));
	}

	private static final String ONLY_NAMES_VERBS_ADJECTIVES="ONLY_NAMES_VERBS_ADJECTIVES";
	private static final String ONLY_NAMES_VERBS="ONLY_NAMES_VERBS";
	private static final String ONLY_NAMES="ONLY_NAMES";

	/*

	 * Tanl POS Tagset
	 * The Tanl tagset is based on the ILC/PAROLE tagset and is conformant to the EAGLES international standard
	 * 
	 * 
	 * http://medialab.di.unipi.it/wiki/Tanl_POS_Tagset

	Coarse-grained tags:

	//A 	adjective
	//B 	adverb
	//C 	conjunction
	//D 	determiner
	//E 	preposition
	//F 	punctuation
	//I 	interjection
	//N 	numeral
	//P 	pronoun
	//R 	article
	//S 	noun
	//T 	predeterminer
	//V 	verb
	//X 	residual class

	 */

	private void ignoreSet(String set){

		if(set.equals(OpenNlpExtractor.ONLY_NAMES_VERBS_ADJECTIVES)){
			ignoreset.add("B");
			ignoreset.add("C");
			ignoreset.add("D");
			ignoreset.add("E");
			ignoreset.add("F");
			ignoreset.add("I");
			ignoreset.add("N");
			ignoreset.add("P");
			ignoreset.add("R");
			ignoreset.add("T");
			ignoreset.add("X");
		}
		else if(set.equals(OpenNlpExtractor.ONLY_NAMES_VERBS)){
			ignoreset.add("A");
			ignoreset.add("B");
			ignoreset.add("C");
			ignoreset.add("D");
			ignoreset.add("E");
			ignoreset.add("F");
			ignoreset.add("I");
			ignoreset.add("N");
			ignoreset.add("P");
			ignoreset.add("R");
			ignoreset.add("T");
			ignoreset.add("X");
		}
		else if(set.equals(OpenNlpExtractor.ONLY_NAMES)){
			ignoreset.add("A");
			ignoreset.add("B");
			ignoreset.add("C");
			ignoreset.add("D");
			ignoreset.add("E");
			ignoreset.add("F");
			ignoreset.add("I");
			ignoreset.add("N");
			ignoreset.add("P");
			ignoreset.add("R");
			ignoreset.add("T");
			ignoreset.add("X");
			ignoreset.add("V");
		}
	}

	private String stems(String s){
		stemmer.setCurrent(s);
		stemmer.stem();
		return stemmer.getCurrent();
	}

	private String[] tokenizes(String text) throws InvalidFormatException, IOException {
		return tokenizer.tokenize(text);
	}

	private String[] tags(String[] sentence){
		return tagger.tag(sentence);
	}

	//main method for feature extraction
	public List<String> execute(String text) throws Exception{

		List<String> result=new ArrayList<String>();		
		
		//Open-NLP
		String[] tokens=tokenizes(text);
		String[] tags=tags(tokens);

		//filtering/cleaning feature
		int i=0;
		while(i<tokens.length){
			if(!ignoreset.contains(""+tags[i].charAt(0))&&(!tags[i].substring(0, 2).equals("VA"))){
				String stemmed=stems(tokens[i].replaceAll("[^a-zA-Z ]", "").toLowerCase());
				if((!stopwords.contains(stemmed))&&(stemmed.length()>2)) result.add(stemmed);
			}
			i++;
		}
		return result;
	}
	
	public List<String> extract(PredictionEntry entry) throws Exception {
		List<String> res= new ArrayList<String>();
		
		if(entry.getTitle()!=null){
			res.addAll(this.execute(entry.getTitle()));
		}
		if(entry.getDescription()!=null){
			res.addAll(this.execute(entry.getDescription()));
		}
		if(entry.getTags()!=null){
			for(String s:entry.getTags()) res.add(this.stems(s));
		}
		
		return res;
	}

	//test
	public static void main(String[] args) throws Exception {
		
		FeatureExtractor fe=new OpenNlpExtractor();
		Set<String> tags=new HashSet<String>();
		tags.add("tag1");
		tags.add("medicina");
		tags.add("urbanistica");
		PredictionEntry pe=new PredictionEntry("questo è un dataset di prova", "questo è il corpo di un dataset di prova", tags);
		List<String> l=fe.extract(pe);
		for(String li:l) System.out.println(li);
	}

}
