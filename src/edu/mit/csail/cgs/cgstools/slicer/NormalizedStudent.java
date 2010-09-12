/*
 * Author: tdanford
 * Date: Mar 21, 2009
 */
package edu.mit.csail.cgs.cgstools.slicer;

import java.awt.Color;

import cern.jet.random.StudentT;
import cern.jet.random.engine.DRand;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.FunctionModel;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.VariableFunction;
import edu.mit.csail.cgs.viz.eye.ModelHistogram;
import edu.mit.csail.cgs.viz.eye.OverlayModelPaintable;
import edu.mit.csail.cgs.viz.paintable.PaintableFrame;
import edu.mit.csail.cgs.viz.paintable.PaintableScale;

public class NormalizedStudent extends FunctionModel {
	
	public static void main(String[] args) { 
		int samples = 10000;
		Double[] degs = new Double[] { 2.0, 3.0, 5.0, 10.0 };
		ModelHistogram[] hists = new ModelHistogram[degs.length];

		for(int i = 0; i < hists.length; i++) { 
			hists[i] = new ModelHistogram("x");
			hists[i].setProperty(ModelHistogram.binsKey, 100);
			
			double f = (double)(i + 1) / (double)hists.length;
			int intensity = (int)Math.round(f * 255.0);
			Color c = new Color(0, 0, 255, 50);
			
			hists[i].setProperty(ModelHistogram.colorKey, c);
			
			if(i > 0) { 
				//hists[0].synchronizeProperty(ModelHistogram.xScaleKey, hists[i]);
				hists[0].synchronizeProperty(ModelHistogram.yScaleKey, hists[i]);
			}
		
			SliceSampler sampler = 
				new SliceSampler(new NormalizedStudent(degs[i]), 1.0, 0.0);

			for(int j = 0; j < samples; j++) { 
				double x = sampler.nextX();
				//System.out.println(String.format("%d: %.3f", i, x));
				hists[i].addModel(new PointSample(x));
			}
			
			hists[i].setProperty(ModelHistogram.xScaleKey, new PaintableScale(-10.0, 10.0));
		}
		
		PaintableFrame pf = new PaintableFrame("Student", new OverlayModelPaintable(hists));
	}
	
	public FunctionModel f;
	private Double degrees;
	private StudentT dist;
	
	public NormalizedStudent(double deg) { 
		this(new VariableFunction(), deg);
	}
	
	public NormalizedStudent(FunctionModel fm, double deg) {
		f = fm;
		degrees = deg;
		dist = new StudentT(degrees, new DRand());
	}

	public Double eval(Double input) { 
		return dist.pdf(f.eval(input));
	}
}
