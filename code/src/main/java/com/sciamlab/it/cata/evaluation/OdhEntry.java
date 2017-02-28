package com.sciamlab.it.cata.evaluation;
import java.util.Set;

import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
public class OdhEntry{
	
	private String title;
	private String description;
	private Set<String> tags;
	private Set<Theme> categories;
	private String id;
	private String publisher;
	
	public OdhEntry(String id, String title, String description, Set<String> tags, Set<Theme> categories, String publisher){
		this.id=id;
		this.description=description;
		this.title=title;
		this.tags=tags;
		this.categories=categories;
		this.publisher=publisher;
	}

	public String getPublisher() {
		return publisher;
	}
	
	public String getId() {
		return id;
	}
	
	public Set<Theme> getCategories() {
		return categories;
	}
	public String getDescription() {
		return description;
	}
	public Set<String> getTags() {
		return tags;
	}
	public String getTitle() {
		return title;
	}
	
	@Override
	public String toString() {
		return super.toString()+"\n"+
				this.id+"\n"+
				this.title+"\n"+
				this.description+"\n"+
				"tags: "+this.tags+"\n"+
				"categories: "+this.categories+"\n"+
				"publisher: "+this.publisher+"\n";
	}
	
}