package com.sciamlab.it.cata.evaluation;
import java.io.FileWriter;
import java.io.IOException;

import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.it.cata.classifier.ClassifiedEntry;

public class Printlog{
	private FileWriter fw;
	public Printlog(String file) throws IOException{
		fw= new FileWriter(file, true);
	}
	
	public void close() throws IOException{
		fw.close();
	}
	
	public void printDataset(OdhEntry pe) throws IOException{
		fw.write(pe.getTitle());
		fw.write("\n");
		fw.write(pe.getDescription());
		fw.write("\n");
		fw.write(pe.getTags().toString());
		fw.write("\n");
	}
	
	public void printClassifiedEntry(ClassifiedEntry pe) throws IOException{
		fw.write("feature:");
		fw.write(pe.getFeatureSet().toString());
		fw.write("\n");
		fw.write("-----------------------------------");
		fw.write("\n");
	}
	public void printTheme(Theme t) throws IOException{
		fw.write("classified as: ");
		fw.write(t.toString());
		fw.write("\n");
	}
	
	
	public static void main(String[] args) throws IOException {
		
		Printlog pl=new Printlog("C:/Users/simone/Desktop/file");

		pl.close();
		
	}
}
