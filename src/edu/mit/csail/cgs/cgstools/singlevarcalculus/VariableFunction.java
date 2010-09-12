/*
 * Author: tdanford
 * Date: Dec 21, 2008
 */
package edu.mit.csail.cgs.cgstools.singlevarcalculus;

public class VariableFunction extends FunctionModel {
	
	public Double eval(Double input) {
		return input;
	}
	
	public String toString() { return "x"; }
}
