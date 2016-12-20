//recursive crawling from relative path
//constructor(root path)
//.crawl() returns list of relative path to files

package com.sciamlab.it.acquis.xquery;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FScrawlingRename {
	
	private String directory;
	private ArrayList<String> files;
	
	public FScrawlingRename(String directory) {
		files=new ArrayList<String>();
		this.directory=directory;
	}
	
	public ArrayList<String> getFiles() {
		return files;
	}
	
	public ArrayList<String> crawl() throws IOException {
		return crawl(new File(directory), "");
	}

	private ArrayList<String> crawl(File f, String current) throws IOException {
		if (!f.isDirectory()){
			String dir=current+f.getName();
			Path source = Paths.get("C:/Users/simone/Desktop/"+dir);
			Files.move(source, source.resolveSibling(f.getName().replaceAll("#", "-c-")));
			
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
	
	public static void main(String[] args) throws IOException { 
		FScrawlingRename fsc=new FScrawlingRename("C:/Users/simone/Desktop/it-acquis3");
		fsc.crawl();
	}
	
}
