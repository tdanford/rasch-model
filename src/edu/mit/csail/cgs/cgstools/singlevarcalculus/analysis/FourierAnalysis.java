/*
 * Author: tdanford
 * Date: Nov 30, 2008
 */
package edu.mit.csail.cgs.cgstools.singlevarcalculus.analysis;

import java.util.*;
import java.awt.Color;
import java.lang.reflect.*;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.*;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.binary.*;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.unary.*;
import edu.mit.csail.cgs.utils.models.*;
import edu.mit.csail.cgs.viz.eye.OverlayModelPaintable;
import edu.mit.csail.cgs.viz.paintable.PaintableFrame;
import edu.mit.csail.cgs.viz.paintable.PaintableScale;

public class FourierAnalysis {
	
	public static void main(String[] args) { 
		FunctionModel original = 
			new ProductFunction(0.23, new CosineFunction(
					new ProductFunction(5.0, new VariableFunction())));
		original = new SumFunction(original, 
				new LinearFunction(new VariableFunction(), 2.0, 1.15));
		
		//FunctionModel original = new LinearFunction(0.0, 3.0);
		//original = new ProductFunction(original, new SineFunction(5.0, 0.23));
		
		FourierAnalysis analysis = new FourierAnalysis(original);

		Double integral = original.integrate(analysis.range, analysis.accuracy);
		System.out.println("Integral: " + integral);

		analysis.visualizeApproximation(20);
	}
	
	public static Double acc = 0.001;

	private Double a0;
	private Map<Integer,Double> cosineA, sineB;
	private FunctionModel f;
	private Double accuracy;
	
	private Double[] range;
	
	public FourierAnalysis(FunctionModel fm) { 
		f = fm;
		cosineA = new HashMap<Integer,Double>();
		sineB = new HashMap<Integer,Double>();
		accuracy = acc;
		range = new Double[] { -Math.PI, Math.PI };
		a0 = f.integrate(range, accuracy) / Math.PI; 
	}
	
	public void visualizeApproximation(int n) { 
		FunctionModelPaintable original = new FunctionModelPaintable(f);
		original.setProperty(FunctionModelPaintable.colorKey, Color.blue);
		original.setProperty(FunctionModelPaintable.xScaleKey, new PaintableScale(-Math.PI, Math.PI));
		original.setProperty(FunctionModelPaintable.yScaleKey, new PaintableScale(-5.0, 5.0));
		
		FunctionModelPaintable approx = new FunctionModelPaintable(approximate(n));
		original.synchronizeProperty(FunctionModelPaintable.xScaleKey, approx);
		original.synchronizeProperty(FunctionModelPaintable.yScaleKey, approx);

		OverlayModelPaintable overlay = new OverlayModelPaintable(original, approx);
		new PaintableFrame("Fourier Analysis", overlay);
	}
	
	public FunctionModel approximate(int n) { 
		FunctionModel approx = new ConstantFunction(a0 / 2.0);
		
		for(int i = 1; i <= n; i++) { 
			double a = findA(i), b = findB(i);
			approx = new SumFunction(approx, 
					new ProductFunction(a, new CosineFunction(
							new ProductFunction((double)i, new VariableFunction()))));
			approx = new SumFunction(approx, 
					new ProductFunction(b, new SineFunction(
							new ProductFunction((double)i, new VariableFunction()))));
		}
		
		// I apparently need this constant factor... but I don't understand why!
		approx = new ProductFunction(new ConstantFunction(0.5), approx);
		
		System.out.println("approx: \n" + approx.toString());
		
		return approx;
	}
	
	public Double findA(int n) { 
		if(cosineA.containsKey(n)) { 
			return cosineA.get(n);
		} else { 
			Double aN = 
				new ProductFunction(f,
						new CosineFunction(
								new ProductFunction((double)n, 
										new VariableFunction())))
					.integrate(range, accuracy) / Math.PI;
			cosineA.put(n, aN);
			return aN;
		}
	}

	public Double findB(int n) { 
		if(sineB.containsKey(n)) { 
			return sineB.get(n);
		} else { 
			Double bN =  
				new ProductFunction(f,
						new SineFunction(
								new ProductFunction((double)n, 
										new VariableFunction())))
					.integrate(range, accuracy) / Math.PI;
			sineB.put(n, bN);
			return bN;
		}
	}
}
