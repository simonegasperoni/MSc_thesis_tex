package com.sciamlab.it.cata.retraining;
import java.util.List;
import java.util.Set;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;

public class SOLrQuery{
	private String publisher;
	private List<String> tags;
	private Set<Theme> categories;
	
	public SOLrQuery(String publisher, List<String> tags, Set<Theme> categories){
		this.publisher=publisher;
		this.tags=tags;
		this.categories=categories;
	}
	
	public Set<Theme> getCategories() {
		return categories;
	}
	
	public String getPublisher() {
		return publisher;
	}
	
	public List<String> getTags() {
		return tags;
	}
	
	public String toString() {
		return this.publisher+" \n"+tags.toString()+" \n"+categories.toString();
	}
}