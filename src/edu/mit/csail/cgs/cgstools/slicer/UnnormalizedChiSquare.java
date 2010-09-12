/*
 * Author: tdanford
 * Date: Mar 18, 2009
 */
package edu.mit.csail.cgs.cgstools.slicer;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.*;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.binary.*;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.unary.*;

public class UnnormalizedChiSquare extends ProductFunction {

	public UnnormalizedChiSquare(FunctionModel f, Integer k) { 
		super(new MonomialFunction(f, (k/2)-1),
			  new ExponentFunction(new ProductFunction(-0.5, f)));
	}
	
	public UnnormalizedChiSquare(Integer k) { 
		this(new VariableFunction(), k);
	}
}
