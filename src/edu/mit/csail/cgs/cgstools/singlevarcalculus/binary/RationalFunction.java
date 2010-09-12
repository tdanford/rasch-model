/*
 * Author: tdanford
 * Date: Dec 1, 2008
 */
package edu.mit.csail.cgs.cgstools.singlevarcalculus.binary;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.ConstantFunction;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.FunctionModel;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.unary.MonomialFunction;

public class RationalFunction extends ProductFunction {

	public RationalFunction(FunctionModel fnum, FunctionModel fdenom) { 
		super(fnum, new MonomialFunction(fdenom, -1));
	}
	
	public RationalFunction(FunctionModel fdenom) { 
		this(new ConstantFunction(1.0), fdenom);
	}
	
	public String toString() { 
		return String.format("(%s) / (%s)", f1.toString(), f2.toString());
	}
}
