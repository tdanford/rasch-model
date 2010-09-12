/*
 * Author: tdanford
 * Date: Mar 9, 2009
 */
package edu.mit.csail.cgs.cgstools.slicer;

import java.util.*;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.FunctionModel;
import edu.mit.csail.cgs.utils.models.Model;
import edu.mit.csail.cgs.viz.eye.ModelHistogram;
import edu.mit.csail.cgs.viz.paintable.PaintableFrame;
import edu.mit.csail.cgs.viz.paintable.PaintableScale;

public class SliceTest {
	
	public static void main(String[] args) { 
		double x = 1.0;
		double w = 1.0;
		int samples = 1000;
		
		FunctionModel p = new UnnormalizedGaussian(0.0, 2.0);
		SliceSampler s = new SliceSampler(p, w, x);
		
		ModelHistogram hist = new ModelHistogram("x");
		hist.setProperty(ModelHistogram.binsKey, 50);
		hist.setProperty(ModelHistogram.xScaleKey, 
				new PaintableScale(-10.0, 10.0));
		
		double m = 0.0;
		LinkedList<Double> slist = new LinkedList<Double>();
		
		for(int i = 0; i < samples; i++) { 
			x = s.nextX();
			hist.addModel(new HistSample(x));
			m += x;
			slist.add(x);
		}
		
		m /= (double)samples;
		double v = 0.0;
		for(Double sm : slist) { 
			double d = sm-m;
			d *= d;
			v += d;
		}
		
		double var = v / (double)samples;
		
		System.out.println(String.format("Mean: %.3f", m));
		System.out.println(String.format("Var: %.3f", var));
		
		PaintableFrame pf = new PaintableFrame("Histogram", hist);
	}

	public static class HistSample extends Model { 
		public Double x; 
		public HistSample(double _x) { x = _x; }
	}

}
