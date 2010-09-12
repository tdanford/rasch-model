/*
 * Author: tdanford
 * Date: Dec 1, 2008
 */
package edu.mit.csail.cgs.cgstools.singlevarcalculus.analysis;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.*;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.binary.*;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.unary.*;
import edu.mit.csail.cgs.viz.paintable.PaintableFrame;

public class Main {

	public static void main(String[] args) { 
		FunctionModel line = new LinearFunction(new VariableFunction(), 0.0, 1.0);
		FunctionModel rat = new RationalFunction(new ConstantFunction(1.0), line);
		
		FunctionModel pieces = new PiecewiseFunction(rat, 0.25, line);
		//FunctionModel gauss = FunctionModel.createGaussian(0.0, 1.0);
		
		//FunctionModel fp = new AbsFunction(rat);
		//FunctionModel fp = new SumFunction(line, 2.0);
		FunctionModel fp = new PolynomialFunction(new VariableFunction(), 
				new Double[] { 1.0, -2.0, 1.0, 1.0 });
		
		//FourierAnalysis analysis = new FourierAnalysis(fp);
		FunctionAnalysis analysis = new FunctionAnalysis(
				fp, new FourierSeries(), new Double[] { -Math.PI, Math.PI });
		analysis.visualizeApproximation(20, -50.0, 50.0);
	}
}
