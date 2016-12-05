package com.sciamlab.it.corpus.features;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.italianStemmer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

public class FeaturesExtr {

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
	public FeaturesExtr(String tokenmodel, String posmodel) throws InvalidFormatException, IOException{
		
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
	
	public FeaturesExtr() throws InvalidFormatException, IOException{
		
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
	}

	//some stemmed stopwords
	public void setStopwords(){
		
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

	public static final String ONLY_NAMES_VERBS_ADJECTIVES="ONLY_NAMES_VERBS_ADJECTIVES";
	public static final String ONLY_NAMES_VERBS="ONLY_NAMES_VERBS";
	public static final String ONLY_NAMES="ONLY_NAMES";

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

	public HashSet<String> getIgnoreSet(){
		return this.ignoreset;
	}

	public void ignoreSet(String set){

		if(set.equals(FeaturesExtr.ONLY_NAMES_VERBS_ADJECTIVES)){
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
		else if(set.equals(FeaturesExtr.ONLY_NAMES_VERBS)){
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
		else if(set.equals(FeaturesExtr.ONLY_NAMES)){
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

	public String stems(String s){
		stemmer.setCurrent(s);
		stemmer.stem();
		return stemmer.getCurrent();
	}

	public String[] tokenizes(String text) throws InvalidFormatException, IOException {
		return tokenizer.tokenize(text);
	}

	public String[] tags(String[] sentence){
		return tagger.tag(sentence);
	}

	//main method for feature extraction
	public ArrayList<String> execute(String text) throws InvalidFormatException, IOException{

		ArrayList<String> result=new ArrayList<String>();		
		
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

	/*
	//test
	public static void main(String[] args) throws InvalidFormatException, IOException {
		FeaturesExtr fe=new FeaturesExtr();
		fe.ignoreSet(FeaturesExtr.ONLY_NAMES_VERBS_ADJECTIVES);
		String text="I Repubblicaines hanno scelto come il candidato delle presidenziali. Al primo turno grande exploit di Francois Fillon con il 44,2%, secondo Alain JuppÃ© con il 28,5%, terzo l'ex presidente del CEE con il 20,6. I due ex premier vanno al ballottaggio del 27 novembre. L'ex presidente lascia la politica";
		ArrayList<String> l=fe.execute(text);
		for(String li:l) System.out.println(li);
	}
	*/
}





























