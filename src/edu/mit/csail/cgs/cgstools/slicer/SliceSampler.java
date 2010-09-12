/*
 * Author: tdanford
 * Date: Mar 3, 2009
 */
package edu.mit.csail.cgs.cgstools.slicer;

import java.util.*;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.*;

public class SliceSampler implements Sampler {

	private Random rand;
	private FunctionModel func;
	private double x, w; 
	
	public SliceSampler(FunctionModel f, double w, double _x) {
		rand = new Random();
		func = f;
		x = _x;
		this.w = w;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.csail.cgs.cgstools.slicer.Sampler#samples(int)
	 */
	public Collection<Sample> samples(int k) { 
		ArrayList<Sample> s = new ArrayList<Sample>();
		for(int i = 0; i < k; i++) { 
			s.add(new Sample(nextX()));
		}
		return s;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.csail.cgs.cgstools.slicer.Sampler#nextX()
	 */
	public double nextX() {
		double fx = func.eval(x);
		double l = rand.nextDouble();
		double y = l * fx;
		double m = rand.nextDouble();
		
		double lw = x - m*w;
		double uw = lw + w;
		
		//System.out.println(String.format("\t\tx: %.3f", x));
		//System.out.println(String.format("\t\tf(x): %.3f", fx));
		//System.out.println(String.format("\t\tlmbda: %.3f", l));
		//System.out.println(String.format("\t\ty: %.3f", y));
		
		while(func.eval(lw) > y) { 
			lw -= w; 
		}
		while(func.eval(uw) > y) { 
			uw += w; 
		}

		//System.out.println(String.format("\t\tU,L: %.3f,%.3f", lw, uw));

		double nx = lw + (rand.nextDouble() * (uw-lw));
		while(func.eval(nx) < y) { 
			nx = lw + (rand.nextDouble() * (uw-lw));			
		}
		
		//System.out.println(String.format("\t\tx': %.3f", nx));
		
		x = nx;
		return x;
	}

	public double nextLogX() {
		double logfx = func.eval(x);
		double logl = Math.log(rand.nextDouble());
		double y = logl + logfx;
		double m = rand.nextDouble();
		
		double lw = x - m*w;
		double uw = lw + w;
		
		//System.out.println(String.format("\t\tx: %.3f", x));
		//System.out.println(String.format("\t\tf(x): %.3f", fx));
		//System.out.println(String.format("\t\tlmbda: %.3f", l));
		//System.out.println(String.format("\t\ty: %.3f", y));
		
		while(func.eval(lw) > y) { 
			lw -= w; 
		}
		while(func.eval(uw) > y) { 
			uw += w; 
		}

		//System.out.println(String.format("\t\tU,L: %.3f,%.3f", lw, uw));

		double nx = lw + (rand.nextDouble() * (uw-lw));
		while(func.eval(nx) < y) { 
			nx = lw + (rand.nextDouble() * (uw-lw));			
		}
		
		//System.out.println(String.format("\t\tx': %.3f", nx));
		
		x = nx;
		return x;
	}
}
