/*
 * Author: tdanford
 * Date: Dec 1, 2008
 */
package edu.mit.csail.cgs.cgstools.singlevarcalculus.binary;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.ConstantFunction;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.FunctionModel;

public class SumFunction extends FunctionModel {
	
	public static FunctionModel add(FunctionModel... fs) {
		if(fs.length == 0) { return new ConstantFunction(0.0); }
		if(fs.length == 1) { return fs[0]; }
		FunctionModel f = fs[0];
		for(int i = 1; i < fs.length; i++) { 
			f = new SumFunction(f, fs[i]);
		}
		return f;
	}
	
	public FunctionModel f1, f2;
	
	public SumFunction(FunctionModel first, FunctionModel second) { 
		f1 = first; f2 = second;
	}
	
	public SumFunction(FunctionModel f1, Double c) { 
		this(f1, new ConstantFunction(c));
	}
	
	public Double eval(Double input) { 
		return f1.eval(input) + f2.eval(input);
	}
	
	public String toString() { return String.format("(%s) + (%s)", f1, f2); }
}
