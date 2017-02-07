package com.sciamlab.it.cata.classifier;

import java.util.Set;

public class PredictionEntry {

	public final String title;
	public final String description;
	public final Set<String> tags;
	
	public PredictionEntry(String title, String description, Set<String> tags) {
		super();
		this.title = title;
		this.description = description;
		this.tags = tags;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Set<String> getTags() {
		return tags;
	}

	@Override
	public String toString() {
		return "title=" + title + ", description=" + description + ", tags=" + tags;
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
	
	
}
