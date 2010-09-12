/*
 * Author: tdanford
 * Date: Dec 1, 2008
 */
package edu.mit.csail.cgs.cgstools.singlevarcalculus.unary;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.ConstantFunction;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.FunctionModel;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.binary.ProductFunction;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.binary.RationalFunction;

public class LogFunction extends FunctionModel {
	
	public FunctionModel f;
	
	public LogFunction(FunctionModel ff) { 
		f = ff;
	}
	
	public Double eval(Double input) {
		Double loginput = f.eval(input);
		if(loginput <= 0.0) { return null; }
		return Math.log(loginput);
	}
	
	public String toString() { return String.format("log(%s)", f.toString()); }
}
