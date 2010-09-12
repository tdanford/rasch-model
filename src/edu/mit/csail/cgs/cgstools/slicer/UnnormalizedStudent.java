/*
 * Author: tdanford
 * Date: Mar 18, 2009
 */
package edu.mit.csail.cgs.cgstools.slicer;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.*;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.unary.*;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.binary.*;
import edu.mit.csail.cgs.viz.eye.ModelHistogram;
import edu.mit.csail.cgs.viz.paintable.PaintableFrame;
import edu.mit.csail.cgs.viz.paintable.PaintableScale;

public class UnnormalizedStudent extends MonomialFunction {
	
	public static void main(String[] args) { 
		example(1);
		example(5);
		example(10);
		example(100);
	}
	
	public static void example(Integer nu) { 
		UnnormalizedStudent s = new UnnormalizedStudent(nu);
		
		ModelHistogram hist = new ModelHistogram("x");
		hist.setProperty(ModelHistogram.binsKey, 100);
		
		SliceSampler sampler = new SliceSampler(s, 0.1, 0.0);
		
		for(int i = 0; i < 10000; i++) { 
			PointSample ps = new PointSample(sampler.nextX());
			hist.addModel(ps);
		}
		
		PaintableScale scale = hist.getPropertyValue(ModelHistogram.xScaleKey);
		double abs =
			Math.max(10.0,
					Math.max(Math.abs(scale.getMin()), Math.abs(scale.getMax())));
		hist.setProperty(ModelHistogram.xScaleKey, new PaintableScale(-abs, abs));
		PaintableFrame pf = new PaintableFrame("Student " + nu, hist);
	}
	
	public Double nu;
	
	public UnnormalizedStudent(Integer k) { 
		this(new VariableFunction(), k);
	}
	
	public UnnormalizedStudent(FunctionModel f, Integer k) { 
		super(new SumFunction(
				new ProductFunction(1.0 / (double)k, 
									new MonomialFunction(f, 2)), 1.0), 
				-(k+1), 2);
		nu = (double)k;
	}
	
	public Double eval(Double input) { 
		Double x = f.eval(input);
		double x2 = x*x;
		double base = 1.0 + (x2/nu);
		double expt = -((nu + 1.0) / 2.0);
		return Math.pow(base, expt);
	}

}
 