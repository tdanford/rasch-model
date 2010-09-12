/*
 * Author: tdanford
 * Date: Dec 1, 2008
 */
package edu.mit.csail.cgs.cgstools.singlevarcalculus.unary;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.FunctionModel;

public class SineFunction extends FunctionModel {
	
	public FunctionModel x;
	
	public SineFunction(FunctionModel f) { 
		x = f;
	}
	
	public Double eval(Double input) { 
		return Math.sin(x.eval(input));
	}
	
	public String toString() {
		return String.format("sin(%s)", x.toString());
	}
}
