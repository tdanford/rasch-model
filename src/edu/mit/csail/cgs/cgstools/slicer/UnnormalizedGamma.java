/*
 * Author: tdanford
 * Date: Mar 18, 2009
 */
package edu.mit.csail.cgs.cgstools.slicer;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.*;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.binary.*;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.unary.*;

public class UnnormalizedGamma extends ProductFunction {
	
	public FunctionModel inner;
	public Integer k;
	public Double theta;
	
	public UnnormalizedGamma(Integer k, Double theta) { 
		this(new VariableFunction(), k, theta);
	}

	public UnnormalizedGamma(FunctionModel f, Integer k, Double theta) { 
		super(new MonomialFunction(f, k-1),
				new ExponentFunction(new NegativeFunction(new RationalFunction(
						f, new ConstantFunction(theta)))));
		this.k = k;
		this.theta = theta;
	}
	
	public Double eval(Double input) { 
		Double x = inner.eval(input);
		Double leftp = 1.0;
		for(int i = 0; i < k-1; i++) { 
			leftp *= x;
		}
		double rightp = Math.exp(-x / theta); 
		return leftp * rightp;
	}
}
