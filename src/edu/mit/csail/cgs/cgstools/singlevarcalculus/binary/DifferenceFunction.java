/*
 * Author: tdanford
 * Date: Mar 18, 2009
 */
package edu.mit.csail.cgs.cgstools.singlevarcalculus.binary;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.FunctionModel;

public class DifferenceFunction extends SumFunction {

	public DifferenceFunction(FunctionModel f1, FunctionModel f2) { 
		super(f1, new ProductFunction(-1.0, f2));
	}
	
	public DifferenceFunction(FunctionModel f1, Double c2) { 
		super(f1, -c2);
	}
	
	public String toString() { 
		return String.format("%s - %s", f1.toString(), f2.toString());
	}
}
