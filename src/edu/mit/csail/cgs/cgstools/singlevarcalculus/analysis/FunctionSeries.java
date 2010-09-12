/*
 * Author: tdanford
 * Date: Dec 2, 2008
 */
package edu.mit.csail.cgs.cgstools.singlevarcalculus.analysis;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.*;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.unary.*;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.binary.*;

public interface FunctionSeries {
	public FunctionModel function(Integer n);
}

class CosineSeries implements FunctionSeries {
	public FunctionModel function(Integer n) {
		return new CosineFunction(new ProductFunction(new ConstantFunction((double)n), 
				new VariableFunction()));
	} 
}

class SineSeries implements FunctionSeries {
	public FunctionModel function(Integer n) {
		return new SineFunction(new ProductFunction(new ConstantFunction((double)n), 
				new VariableFunction()));
	} 
}

class InterleavedFunctionSeries implements FunctionSeries {
	
	private FunctionSeries a, b;
	
	public InterleavedFunctionSeries(FunctionSeries _a, FunctionSeries _b) { 
		a = _a; b = _b;
	}

	public FunctionModel function(Integer nn) {
		boolean isa = nn % 2 == 0;
		int n = nn / 2;
		return isa ? a.function(n) : b.function(n);
	} 
}

class FourierSeries extends InterleavedFunctionSeries { 
	public FourierSeries() { 
		super(new CosineSeries(), new SineSeries());
	}
}
