/*
 * Author: tdanford
 * Date: Mar 9, 2009
 */
package edu.mit.csail.cgs.cgstools.slicer;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.*;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.unary.*;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.binary.*;

public class UnnormalizedGaussian extends ExponentFunction {
	
	public ConstantFunction mean, var;
	public FunctionModel inner;
	
	public UnnormalizedGaussian() { 
		this(0.0, 1.0);
	}
	
	public UnnormalizedGaussian(Double m, Double v) { 
		this(new VariableFunction(), new ConstantFunction(m), new ConstantFunction(v));
	}
	
	public UnnormalizedGaussian(FunctionModel f, ConstantFunction m, ConstantFunction v) {
		super(new RationalFunction(
				new NegativeFunction(
						new MonomialFunction(
								new DifferenceFunction(f, m), 2)), 
				new ProductFunction(2.0, v)));
		inner = f;
		mean = m; var = v;
	}

	public Double eval(Double input) {
		Double x = inner.eval(input);
		double diff = x - mean.eval(input);
		double expt = -(diff * diff) / (2.0 * var.eval(input));
		return Math.exp(expt);
	} 
	
	public Double getMean() { return mean.eval(0.0); }
	public Double getVar() { return var.eval(0.0); }
	
	public void setMean(Double m) { mean = new ConstantFunction(m); }
	public void setVar(Double v) { var = new ConstantFunction(v); }
	
	public String summary() {  
		return String.format("%.3f (+/- %.3f)", getVar(), 2.0*Math.sqrt(getVar()));
	}
	
}
