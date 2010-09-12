/*
 * Author: tdanford
 * Date: Dec 3, 2008
 */
package edu.mit.csail.cgs.cgstools.singlevarcalculus.unary;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.ConstantFunction;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.FunctionModel;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.binary.ProductFunction;

public class NegativeFunction extends ProductFunction {
	
	public NegativeFunction(FunctionModel ff) { 
		super(-1.0, ff);
	}
	
	public String toString() { return String.format("-(%s)", f2.toString()); }
}
