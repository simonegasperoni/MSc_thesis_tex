package com.sciamlab.it.cata.threshold;
import java.util.Map;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;

public interface Thresholder {
	public Map<Theme, Double> getScore(Map<Theme, Double> res);
}
