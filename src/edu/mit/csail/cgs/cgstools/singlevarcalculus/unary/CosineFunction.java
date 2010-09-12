/*
 * Author: tdanford
 * Date: Dec 1, 2008
 */
package edu.mit.csail.cgs.cgstools.singlevarcalculus.unary;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.FunctionModel;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.VariableFunction;

public class CosineFunction extends FunctionModel {
	
	public FunctionModel x;
	
	public CosineFunction(FunctionModel f) { 
		x = f;
	}
	
	public Double eval(Double input) { 
		return Math.cos(x.eval(input));
	}
	
	public String toString() {
		return String.format("cos(%s)", x.toString());
	}
}

