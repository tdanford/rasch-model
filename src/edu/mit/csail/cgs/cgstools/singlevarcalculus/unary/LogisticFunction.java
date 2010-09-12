/*
 * Author: tdanford
 * Date: Dec 1, 2008
 */
package edu.mit.csail.cgs.cgstools.singlevarcalculus.unary;

import edu.mit.csail.cgs.cgstools.rasch.Logistic;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.ConstantFunction;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.FunctionModel;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.FunctionModelPaintable;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.VariableFunction;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.binary.ProductFunction;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.binary.RationalFunction;
import edu.mit.csail.cgs.viz.paintable.PaintableFrame;
import edu.mit.csail.cgs.viz.paintable.PaintableScale;

public class LogisticFunction extends FunctionModel {
	
	public static void main(String[] args) { 
		FunctionModelPaintable p = new FunctionModelPaintable(new LogisticFunction());
		p.setProperty(FunctionModelPaintable.xScaleKey, new PaintableScale(-10.0, 10.0));
		p.setProperty(FunctionModelPaintable.yScaleKey, new PaintableScale(-0.1, 1.1));
		PaintableFrame pf = new PaintableFrame("Logistic Function", p);
	}
	
	public FunctionModel f;
	public Double[] domain;
	
	public LogisticFunction() { 
		this(new VariableFunction());
	}
	
	public LogisticFunction(FunctionModel ff) { 
		f = ff;
		domain = f.domain();
	}
	
	public Double eval(Double input) {
		Double loginput = f.eval(input);
		return Logistic.eval(loginput);
	}
}
