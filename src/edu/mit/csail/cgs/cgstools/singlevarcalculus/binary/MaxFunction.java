/*
 * Author: tdanford
 * Date: Dec 21, 2008
 */
package edu.mit.csail.cgs.cgstools.singlevarcalculus.binary;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.FunctionModel;

public class MaxFunction extends FunctionModel {
	
	public FunctionModel x1, x2;
	
	public MaxFunction(FunctionModel m1, FunctionModel m2) { 
		x1 = m1; 
		x2 = m2;
	}

	public Double eval(Double input) {
		Double y1 = x1.eval(input), y2 = x2.eval(input);
		return Math.max(y1, y2);
	}
}
