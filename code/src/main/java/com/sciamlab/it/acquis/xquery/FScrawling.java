//recursive crawling from relative path
//constructor(root path)
//.crawl() returns list of relative path to files

package com.sciamlab.it.acquis.xquery;
import java.io.*;
import java.util.ArrayList;

public class FScrawling {
	
	private String directory;
	private ArrayList<String> files;
	
	public FScrawling(String directory) {
		files=new ArrayList<String>();
		this.directory=directory;
	}
	
	public ArrayList<String> getFiles() {
		return files;
	}
	
	public ArrayList<String> crawl() {
		return crawl(new File(directory), "");
	}

	private ArrayList<String> crawl(File f, String current) {
		if (!f.isDirectory()){
			String dir=current+f.getName();
			//System.out.println("file: "+dir);
			files.add(dir);
		}
		else {
			File[] subFiles = f.listFiles();
			current += f.getName()+"/";
			for (int i = 0; i < subFiles.length; i++) {
				crawl(subFiles[i], current);
			}
		}
		return files;
	}
	
	public static void main(String[] args) { 
		FScrawling fsc=new FScrawling("C:/Users/simone/Desktop/it-acquis3");
		fsc.crawl();
		System.out.println(fsc.getFiles().size());
	}
	
}
