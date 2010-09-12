/*
 * Author: tdanford
 * Date: Mar 17, 2009
 */
package edu.mit.csail.cgs.cgstools.singlevarcalculus.differentiator;

import java.lang.reflect.*;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.*;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.binary.*;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.unary.*;
import edu.mit.csail.cgs.cgstools.slicer.UnnormalizedGaussian;
import edu.mit.csail.cgs.utils.models.ModelFieldAnalysis;

public class SymbolicDiff {
	
	public static void main(String[] args) { 
		FunctionModel g = new LinearFunction(1.0, 3.0);
		FunctionModel fm = differentiate(g);
		
		System.out.println(g.toString());
		System.out.println();
		System.out.println(fm.toString());

		double x = 2.0; 
		double y = g.eval(x);
		double yprime = fm.eval(x);
	
		System.out.println(String.format("x: %f", x));
		System.out.println(String.format("y: %f", y));
		System.out.println(String.format("yprime: %f", yprime));
	}

	public static FunctionModel differentiate(FunctionModel m) {
		
		if(m instanceof VariableFunction) { 
			return new ConstantFunction(1.0);
			
		} else if (m instanceof ConstantFunction) { 
			return new ConstantFunction(0.0);
			
		} else if (m instanceof SumFunction) { 
			SumFunction f = (SumFunction)m;
			return new SumFunction(differentiate(f.f1), differentiate(f.f2));
			
		} else if (m instanceof ProductFunction) { 
			ProductFunction f = (ProductFunction)m;
			FunctionModel df1 = differentiate(f.f1);
			FunctionModel df2 = differentiate(f.f2);
			return new SumFunction(
					new ProductFunction(f.f1, df2), 
					new ProductFunction(df1, f.f2));
			
		} else if (m instanceof ExponentFunction) { 
			ExponentFunction f = (ExponentFunction)m;
			return new ProductFunction(f, differentiate(f.exponent));

		} else if (m instanceof SineFunction) { 
			SineFunction f = (SineFunction)m;
			return new ProductFunction(new CosineFunction(f.x), differentiate(f.x));

		} else if (m instanceof CosineFunction) { 
			CosineFunction f = (CosineFunction)m;
			return new ProductFunction(
					new NegativeFunction(new SineFunction(f.x)), 
					differentiate(f.x));

		} else if (m instanceof LogFunction) { 
			LogFunction f = (LogFunction)m;
			return new ProductFunction(
					new MonomialFunction(f.f, -1),
					differentiate(f.f));
			
		} else if (m instanceof MonomialFunction) { 
			MonomialFunction f = (MonomialFunction)m;
			return new ProductFunction(new MonomialFunction(f.f, f.exponent-1),
					differentiate(f.f));
		}
		
		throw new IllegalArgumentException(m.getClass().getSimpleName());
	}
}
