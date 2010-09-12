/*
 * Author: tdanford
 * Date: Dec 1, 2008
 */
package edu.mit.csail.cgs.cgstools.singlevarcalculus.unary;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.FunctionModel;

public class AbsFunction extends FunctionModel {
	
	public FunctionModel f;
	
	public AbsFunction(FunctionModel ff) { 
		f = ff;
	}
	
	public Double eval(Double input) {
		return Math.abs(f.eval(input));
	}
}
