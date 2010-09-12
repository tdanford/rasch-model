/*
 * Author: tdanford
 * Date: Dec 1, 2008
 */
package edu.mit.csail.cgs.cgstools.singlevarcalculus.unary;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.ConstantFunction;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.FunctionModel;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.VariableFunction;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.binary.ProductFunction;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.binary.SumFunction;

public class ExponentFunction extends FunctionModel {

	public FunctionModel exponent;
	
	public ExponentFunction() { 
		this(new VariableFunction());
	}
	
	public ExponentFunction(FunctionModel f) { 
		exponent = f;
	}
	
	public Double eval(Double input) { 
		Double exp = exponent.eval(input);
		return Math.pow(Math.E, exp);
	}
	
	public String toString() { 
		return String.format("exp(%s)", exponent.toString());
	}
}
