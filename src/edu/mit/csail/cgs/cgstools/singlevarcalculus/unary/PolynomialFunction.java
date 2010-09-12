/*
 * Author: tdanford
 * Date: Dec 1, 2008
 */
package edu.mit.csail.cgs.cgstools.singlevarcalculus.unary;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.ConstantFunction;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.FunctionModel;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.binary.ProductFunction;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.binary.SumFunction;
import edu.mit.csail.cgs.utils.ArrayUtils;

public class PolynomialFunction extends SumFunction {

	public FunctionModel f;
	public Double[] coefs;
	
	/**
	 * coefficients are given in order, from highest to lowest.  [n, n-1, ... 1] 
	 * 
	 * @param inner
	 * @param c
	 */
	public PolynomialFunction(FunctionModel inner, Double... c) {
		super(restOfPolynomial(inner, c), new ConstantFunction(0.0));
		f = inner;
		coefs = c.clone();
	}
	
	private static FunctionModel restOfPolynomial(FunctionModel inner, Double... c) { 
		if(c.length == 0) { 
			return new ConstantFunction(0.0);
		} else if (c.length == 1) { 
			return new ConstantFunction(c[0]);
		} else { 
			return new SumFunction(
					new ProductFunction(new ConstantFunction(c[0]), 
							new MonomialFunction(inner, c.length+1)),
					restOfPolynomial(inner, ArrayUtils.tail(c)));
		}
	}
	
	public Double eval(Double in) { 
		Double input = f.eval(in);
		
		Double sum = 0.0;
		
		Double x = 1.0;
		if(coefs.length > 0) { 
			sum += coefs[coefs.length-1];
		}
		
		for(int i = coefs.length-2; i >= 0; i--) {  
			x *= input;
			sum += (x * coefs[i]);
		}
		
		return sum;
	}
}
