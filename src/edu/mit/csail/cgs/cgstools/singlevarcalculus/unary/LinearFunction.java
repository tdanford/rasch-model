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

public class LinearFunction extends SumFunction {
	
	public LinearFunction(Double b, Double m) { 
		this(new VariableFunction(), b, m);
	}
	
	public LinearFunction(FunctionModel f, Double b, Double m) { 
		this(f, new ConstantFunction(b), new ConstantFunction(m));
	}
	
	public LinearFunction(FunctionModel inner, ConstantFunction b, ConstantFunction m) {
		super(b, new ProductFunction(m, inner));
	}
}

