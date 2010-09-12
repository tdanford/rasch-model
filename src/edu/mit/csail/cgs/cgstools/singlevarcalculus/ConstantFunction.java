/*
 * Author: tdanford
 * Date: Dec 1, 2008
 */
package edu.mit.csail.cgs.cgstools.singlevarcalculus;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.unary.LinearFunction;

public class ConstantFunction extends FunctionModel {
	
	public static ConstantFunction PI = new ConstantFunction(Math.PI);
	public static ConstantFunction E = new ConstantFunction(Math.E);
	public static ConstantFunction ONE = new ConstantFunction(1.0);
	public static ConstantFunction ZERO = new ConstantFunction(0.0);

	public Double value;
	
	public ConstantFunction(Double v) { 
		value = v;
	}
	
	public Double eval(Double input) { 
		return value;
	}
	
	public String toString() { return String.valueOf(value); }
}

