/*
 * Author: tdanford
 * Date: Nov 30, 2008
 */
package edu.mit.csail.cgs.cgstools.singlevarcalculus;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Iterator;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.binary.SumFunction;
import edu.mit.csail.cgs.utils.models.Model;
import edu.mit.csail.cgs.viz.eye.*;
import edu.mit.csail.cgs.viz.paintable.PaintableScale;

public class FunctionModelPaintable extends AbstractModelPaintable {
	
	public static String xScaleKey = "x-scale";
	public static String yScaleKey = "y-scale";
	public static String colorKey = "color";
	
	private FunctionModel function;
	
	public FunctionModelPaintable() { 
		this(new ConstantFunction(0.0));
	}
	
	public FunctionModelPaintable(FunctionModel f) {
		super();
		function = f;

		initProperty(new PropertyValueWrapper<PaintableScale>(xScaleKey, new PaintableScale(-1.0, 1.0)));
		initProperty(new PropertyValueWrapper<PaintableScale>(yScaleKey, new PaintableScale(-1.0, 1.0)));
		initProperty(new PropertyValueWrapper<Color>(colorKey, Color.red));
	}
	
	public void addModel(Model m) {
		if(Model.isSubclass(m.getClass(), FunctionModel.class)) { 
			if(function == null) {
				function = (FunctionModel)m;
			} else { 
				function = new SumFunction(function, (FunctionModel)m);
			}
			
			dispatchChangedEvent();
		}
	}

	public void addModels(Iterator<? extends Model> itr) {
		while(itr.hasNext()) { 
			addModel(itr.next());
		}
	}

	public void clearModels() {
		function = new ConstantFunction(0.0);
		dispatchChangedEvent();
	}

	public void paintItem(Graphics g, int x1, int y1, int x2, int y2) {
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		PaintableScale xScale = getPropertyValue(xScaleKey);
		PaintableScale yScale = getPropertyValue(yScaleKey);
		Color color = getPropertyValue(colorKey);
		
		Double min = xScale.getMin(), max = xScale.getMax();
		Double range = max-min;
		int w = x2-x1+1;
		int h = y2-y1+1;
		
		int px = -1, py = -1;
		
		g.setColor(Color.black);
		if(xScale.getMin() < 0.0 && xScale.getMax() > 0.0) { 
			double zf  =xScale.fractionalOffset(0.0);
			int x = x1 + (int)Math.round(zf * (double)w);
			g.drawLine(x, y1, x, y2);
		}
		if(yScale.getMin() < 0.0 && yScale.getMax() > 0.0) { 
			double zf  = yScale.fractionalOffset(0.0);
			int y = y2 - (int)Math.round(zf * (double)h);
			g.drawLine(x1, y, x2, y);
		}
		
		g.setColor(color);
		
		for(int x = x1; x <= x2; x++) { 
			Double xInput = min + ((double)(x-x1) / (double)w) * range;
			Double yOutput = function.eval(xInput);
			
			if(yOutput != null) { 
				double yf = yScale.fractionalOffset(yOutput);
				int y = y2 - (int)Math.round(yf * (double)h);

				if(px != -1 && py != -1) { 
					g.drawLine(px, py, x ,y);
				}

				px = x; py = y;
			} else { 
				px = py = -1;
			}
		}
	}
}
