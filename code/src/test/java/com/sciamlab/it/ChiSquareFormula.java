package com.sciamlab.it;
import org.junit.Test;
public class ChiSquareFormula {
	@Test
	public void test() {
		double n00=774106.0;
		double n01=27652.0;
		double n10=141.0;
		double n11=49.0;
		double score=((n11+n10+n01+n00)*(Math.pow((n11*n00)-(n10*n01),2)))/(((n11+n01)*(n11+n10)*(n10+n00)*(n01+n00)));
		System.out.println(score);
	}
}
