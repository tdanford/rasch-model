/*
 * Author: tdanford
 * Date: Mar 9, 2009
 */
package edu.mit.csail.cgs.cgstools.rasch;

public class Logistic {
	
	public static Double eval(Double x) { 
		double numer = Math.exp(x);
		double denom = 1.0 + Math.exp(x);
		return numer / denom;
	}
}
