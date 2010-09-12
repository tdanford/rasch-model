/*
 * Author: tdanford
 * Date: Nov 30, 2008
 */
package edu.mit.csail.cgs.cgstools.singlevarcalculus;

import java.util.*;
import java.lang.reflect.*;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.binary.*;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.differentiator.SymbolicDiff;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.unary.*;
import edu.mit.csail.cgs.utils.models.*;

public abstract class FunctionModel extends Model {
	
	public static Double eps = 1.0e-6; 
	
	private ModelFieldAnalysis<? extends FunctionModel> analysis;
	
	public FunctionModel() { 
		super();
		analysis = new ModelFieldAnalysis(this.getClass());
	}
	
	public abstract Double eval(Double input);
	
	public boolean inDomain(Double v) { 
		Double[] r = domain();
		return r[0] < v && r[1] > v;
	}
	
	public boolean inRange(Double v) { 
		Double[] r = range();
		return r[0] < v && r[1] > v;
	}
	
	public Double newtonIterate(Double start, Double tol, int maxIter) { 
		FunctionModel df = derivative();
		if(df == null) { throw new IllegalArgumentException(); }
		
		Double lastValue = 0.0, value = start;
		Double diff = 0.0;
		int itr = 0;
		
		do { 
			lastValue = value;
			
			double m = df.eval(value);
			double b = -m*value;
			value = -b/m;
			
			diff = Math.abs(value - lastValue);
			itr += 1;
			
		} while(itr <= maxIter && diff > tol && !Double.isInfinite(value) && !Double.isNaN(value));
		
		if(Double.isInfinite(value)) {
			System.err.println(String.format("newtonIterate(%f) became infinite.", start));
			return null;
		} else if (Double.isNaN(value)) { 
			System.err.println(String.format("newtonIterate(%f) became NaN.", start));
			return null;
		}
		
		return itr <= maxIter ? value : null;
	}
	
	public Double fixpIterate(Double start, Double tol, int maxIter) { 
		Double lastValue = 0.0, value = start;
		Double diff = 0.0;
		int itr = 0;
		
		do { 
			lastValue = value;
			value = eval(value);
			diff = Math.abs(value - lastValue);
			itr += 1;
			
		} while(itr <= maxIter && diff > tol && !Double.isInfinite(value) && !Double.isNaN(value));

		if(Double.isInfinite(value)) {
			System.err.println(String.format("fixpIterate(%f) became infinite.", start));
			return null;
		} else if (Double.isNaN(value)) { 
			System.err.println(String.format("fixpIterate(%f) became NaN.", start));
			return null;
		}
		
		return itr <= maxIter ? value : null;
	}
	
	public Double[] range() { 
		Field rangeField = analysis.findField("range");
		if(rangeField != null) { 
			Class type = rangeField.getType();
			if(Model.isSubclass(type, Double[].class)) { 
				try {
					Double[] array = (Double[]) rangeField.get(this);
					if(array != null) { 
						return array;
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return new Double[] { -Double.MAX_VALUE, Double.MAX_VALUE };
	}
	
	public Double[] domain() { 
		Field rangeField = analysis.findField("domain");
		if(rangeField != null) { 
			Class type = rangeField.getType();
			if(Model.isSubclass(type, Double[].class)) { 
				try {
					Double[] array = (Double[]) rangeField.get(this);
					if(array != null) { 
						return array;
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return new Double[] { -Double.MAX_VALUE, Double.MAX_VALUE };
	}

	/**
	 * This should be replaced with a symbolic integrator...
	 * 
	 * @return
	 */
	public FunctionModel antiderivative() { 
		Field rangeField = analysis.findField("antiderivative");
		if(rangeField != null) { 
			Class type = rangeField.getType();
			if(Model.isSubclass(type, FunctionModel.class)) { 
				try {
					FunctionModel anti = (FunctionModel)rangeField.get(this);
					return anti;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}		
		return null;
	}
	
	/**
	 * Calculates the derivative, symbolically.  
	 * 
	 * @return
	 */
	public FunctionModel derivative() { 
		return SymbolicDiff.differentiate(this);
	}
	
	/**
	 * Can be overloaded by sub-classes, to allow for adaptive integration steps.
	 */
	public Double integrationStep(Double input, Double accuracy) { 
		return accuracy;
	}
	
	/**
	 * Simple numeric integration routine.  
	 * 
	 * @param intRange
	 * @param accuracy
	 * @return
	 */
	public Double integrate(Double[] intRange, Double accuracy) {
		FunctionModel antiderivative = antiderivative();
		
		if(antiderivative != null) { 
			return antiderivative.eval(intRange[1]) - antiderivative.eval(intRange[0]);
		}
		
		ArrayList<Double> points = new ArrayList<Double>();
		Double spacing = accuracy;
		
		for(Double value = intRange[0]; value <= intRange[1]; value += spacing) { 
			points.add(value);
			spacing = integrationStep(value, accuracy);
		}
		
		Double sum = 0.0;
		Double v1 = null;
		
		for(int i = 0; i < points.size()-1; i++) { 
			Double p1 = points.get(i), p2 = points.get(i+1);
			if(v1 == null) { v1 = eval(p1); }
			Double v2 = eval(p2);
			
			Double vmean = (v1+v2) / 2.0;
			Double w = (p2-p1); 

			sum += (w * vmean);

			v1 = v2;
		}
		
		return sum;
	}
}

