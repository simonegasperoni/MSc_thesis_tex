package com.sciamlab.it;
import org.junit.Test;

public class MutualInformationFormula {
	public static double log2(double n){
		return Math.log10(n)/Math.log10(2);
	}
	@Test
	public void test() {
		double sizeTS=801948.0;
		double n00=774106.0;
		double n01=27652.0;
		double n10=141.0;
		double n11=49.0;
		double score=(n11/sizeTS)*log2(sizeTS*n11/((n11+n01)*(n11+n10)))+
				(n10/sizeTS)*log2(sizeTS*n10/((n10+n00)*(n11+n10)))+
				(n01/sizeTS)*log2(sizeTS*n01/((n11+n01)*(n01+n00)))+
				(n00/sizeTS)*log2(sizeTS*n00/((n10+n00)*(n01+n00)));
		System.out.println(score);
	}
}
