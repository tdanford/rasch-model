/*
 * Author: tdanford
 * Date: Mar 18, 2009
 */
package edu.mit.csail.cgs.cgstools.slicer;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.*;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.binary.*;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.unary.*;

public class UnnormalizedInverseChiSquare extends UnnormalizedChiSquare {

	public UnnormalizedInverseChiSquare(FunctionModel f, Integer k) { 
		super(f, k);
	}
	
	public UnnormalizedInverseChiSquare(Integer k) { 
		this(new VariableFunction(), k);
	}
	
	public Double eval(Double input) { 
		return 1.0 / super.eval(input);
	}
}
