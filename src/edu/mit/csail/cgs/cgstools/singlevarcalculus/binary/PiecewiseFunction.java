/*
 * Author: tdanford
 * Date: Dec 3, 2008
 */
package edu.mit.csail.cgs.cgstools.singlevarcalculus.binary;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.FunctionModel;

public class PiecewiseFunction extends FunctionModel {
	
	public FunctionModel f1, f2; 
	public Double split;
	
	public PiecewiseFunction(FunctionModel f1, Double split, FunctionModel f2) { 
		this.f1 = f1; this.f2 = f2;
		this.split = split;
	}

	public Double eval(Double input) {
		if(input <= split) { 
			return f1.eval(input);
		} else { 
			return f2.eval(input);
		}
	}
}
