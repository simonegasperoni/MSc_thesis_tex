package com.sciamlab.it.cata.classifier;

import java.util.HashSet;
import java.util.Set;

public class PredictionEntry {


	public final String id;
	public final String title;
	public final String description;
	public final Set<String> tags;
	public final String publisher;
	
	public PredictionEntry(Builder builder) {
		super();
		this.publisher=builder.publisher;
		this.id = builder.id;
		this.title = builder.title;
		this.description = builder.description;
		this.tags = builder.tags;
	}

	@Override
	public String toString() {
		return "id=" + id + ", title=" + title + ", description=" + description + ", tags=" + tags;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PredictionEntry other = (PredictionEntry) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	
	public static class Builder{
		private String publisher;
		private String id;
		private String title;
		private final String description;
		private final Set<String> tags = new HashSet<String>();
		
		public Builder publisher(String publisher){
			this.publisher = publisher;
			return this;
		}
		
		public Builder(String description){
			this.description = description;
		}
		
		public Builder title(String title){
			this.title = title;
			return this;
		}
		
		public Builder id(String id){
			this.id = id;
			return this;
		}
		
		public Builder tag(String tag){
			this.tags.add(tag);
			return this;
		}
		
		public Builder tag(Set<String> tags){
			this.tags.addAll(tags);
			return this;
		}
		
		public PredictionEntry build(){
			return new PredictionEntry(this);
		}
	}
	
}
