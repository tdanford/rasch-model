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

public class FunctionAnalysis {
	
	public static void main(String[] args) { 
		FunctionModel original = 
			new ProductFunction(0.23, new CosineFunction(
					new ProductFunction(5.0, new VariableFunction())));
		original = new SumFunction(original, 
				new LinearFunction(new VariableFunction(), 2.0, 1.15));
		
		//FunctionModel original = new LinearFunction(0.0, 3.0);
		//original = new ProductFunction(original, new SineFunction(5.0, 0.23));
		
		FunctionAnalysis analysis = 
			new FunctionAnalysis(original, 
					new FourierSeries(), 
					new Double[] { -Math.PI, Math.PI });

		Double integral = original.integrate(analysis.range, analysis.accuracy);
		System.out.println("Integral: " + integral);

		analysis.visualizeApproximation(21, -50.0, 50.0);
	}
	
	public static Double acc = 0.001;
	
	private Map<Integer,Double> coefs;
	private FunctionSeries series;
	private FunctionModel f;
	private Double accuracy;
	private Double dc;
	
	private Double[] range;
	
	public FunctionAnalysis(FunctionModel fm, FunctionSeries s, Double[] r) { 
		f = fm;
		coefs = new HashMap<Integer,Double>();
		accuracy = acc;
		series = s;
		range = r;
		dc = f.integrate(range, accuracy) / (range[1] - range[0]);
	}
	
	public double width() { return range[1] - range[0]; }

	public void visualizeApproximation(int n) { 
		visualizeApproximation(n, range[0], range[1]);
	}
	
	public void visualizeApproximation(int n, double miny, double maxy) { 
		FunctionModelPaintable original = new FunctionModelPaintable(f);
		original.setProperty(FunctionModelPaintable.colorKey, Color.blue);
		original.setProperty(FunctionModelPaintable.xScaleKey, new PaintableScale(range[0], range[1]));
		original.setProperty(FunctionModelPaintable.yScaleKey, new PaintableScale(miny, maxy));
		
		FunctionModelPaintable approx = new FunctionModelPaintable(approximate(n));
		original.synchronizeProperty(FunctionModelPaintable.xScaleKey, approx);
		original.synchronizeProperty(FunctionModelPaintable.yScaleKey, approx);

		OverlayModelPaintable overlay = new OverlayModelPaintable(original, approx);
		new PaintableFrame("Function Analysis", overlay);
	}
	
	public FunctionModel approximate(int n) { 
		FunctionModel approx = new ConstantFunction(dc);
		
		for(int i = 1; i <= n; i++) { 
			double c = findCoef(i);
			FunctionModel cfunc = series.function(i);
			approx = new SumFunction(approx,
					new ProductFunction(
							new ConstantFunction(c), cfunc));
			System.out.println(i + ": " + cfunc.toString());
		}
		
		// I apparently need this constant factor... but I don't understand why!
		// approx = new ProductFunction(new ConstantFunction(0.5), approx);

		return approx;
	}
	
	public Double findCoef(int n) { 
		if(coefs.containsKey(n)) { 
			return coefs.get(n);
		} else { 
			FunctionModel basis = series.function(n);
			//double norm = basis.integrate(range, accuracy);
			FunctionModel dcf = new SumFunction(f, -dc);
			
			double norm = (range[1] - range[0]) / 2.0;
			Double aN = 
				new ProductFunction(dcf,basis)
					.integrate(range, accuracy) / norm;
			coefs.put(n, aN);
			return aN;
		}
	}
}
