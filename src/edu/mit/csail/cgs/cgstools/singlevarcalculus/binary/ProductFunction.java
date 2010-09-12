/*
 * Author: tdanford
 * Date: Dec 1, 2008
 */
package edu.mit.csail.cgs.cgstools.singlevarcalculus.binary;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.ConstantFunction;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.FunctionModel;

public class ProductFunction extends FunctionModel {
	
	public static FunctionModel multiply(FunctionModel... fs) {
		if(fs.length == 0) { return new ConstantFunction(1.0); }
		if(fs.length == 1) { return fs[0]; }
		FunctionModel f = fs[0];
		for(int i = 1; i < fs.length; i++) { 
			f = new ProductFunction(f, fs[i]);
		}
		return f;
	}
	
	public FunctionModel f1, f2;
	
	public ProductFunction(FunctionModel first, FunctionModel second) { 
		f1 = first; f2 = second;
	}
	
	public ProductFunction(Double c, FunctionModel s) { 
		this(new ConstantFunction(c), s);
	}
	
	public Double eval(Double input) {
		Double v1 = f1.eval(input);
		Double v2 = f2.eval(input);
		if(v1 == null) { throw new IllegalArgumentException(); }
		if(v2 == null) { throw new IllegalArgumentException(); }
		return v1 * v2;
	}
	
	public String toString() { return String.format("(%s) * (%s)", f1, f2); }
}