/*
 * Author: tdanford
 * Date: Mar 18, 2009
 */
package edu.mit.csail.cgs.cgstools.slicer;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.FunctionModel;

public class UnnormalizedInverseGamma extends UnnormalizedGamma {

	public UnnormalizedInverseGamma(FunctionModel f, Integer alpha, Double beta) { 
		super(f, alpha, 1.0/beta);
	}
}
