/*
 * Author: tdanford
 * Date: Mar 17, 2009
 */
package edu.mit.csail.cgs.cgstools.singlevarcalculus.unary;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.FunctionModel;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.VariableFunction;

public class MonomialFunction extends FunctionModel {
	
	public FunctionModel f;
	public Integer exponent, radical;
	
	public MonomialFunction(Integer exp) { 
		this(new VariableFunction(), exp);
	}
	
	public MonomialFunction(FunctionModel ff, Integer exp) { 
		this(ff, exp, 1);
	}
	
	public MonomialFunction(FunctionModel ff, Integer exp, Integer rad) { 
		f = ff;
		exponent = exp;
		radical = rad;
		
		if(radical > 2 || radical < 1) { 
			throw new IllegalArgumentException();
		}
	}

	public Double eval(Double input) {
		double prod = 1.0;
		double fx = f.eval(input);
		
		int absExp = Math.abs(exponent);
		if(radical == 2 && absExp % 2 == 0) { 
			absExp /= 2;
		}
		
		for(int i = 0; i < absExp; i++) { 
			prod *= fx;
		}
		
		if(radical == 2 && absExp % 2 == 1) { 
			prod = Math.sqrt(prod);
		}

		if(exponent < 0) { 
			prod = 1.0 / prod;
		}
	
		return prod;
	}
	
	public String toString() { 
		return radical == 1 ? 
				String.format("(%s)^%d", f.toString(), exponent) : 
				String.format("(%s)^(%d/%d)", f.toString(), exponent, radical);
	}
}
