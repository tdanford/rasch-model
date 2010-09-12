/*
 * Author: tdanford
 * Date: May 15, 2009
 */
package edu.mit.csail.cgs.cgstools.slicer;

import java.util.*;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.FunctionModel;
import edu.mit.csail.cgs.utils.probability.NormalDistribution;

public class MHKernelSampler implements Sampler {

	private FunctionModel pdist;
	private Double jumpingVariance;
	private Double value;
	private Random rand;
	
	public MHKernelSampler(FunctionModel p, double jmp, double x) { 
		pdist = p;
		value = x;
		jumpingVariance = jmp;
		rand = new Random();
	}
	
	public Double propose() { 
		double diff = rand.nextGaussian() * jumpingVariance;
		return value + diff;
	}
	
	public Double Q(Double newValue, Double oldValue) { 
		double diff = newValue - oldValue;
		double c = 1.0 / Math.sqrt(2.0 * Math.PI * jumpingVariance);
		double expt = -(diff*diff) / (2.0 * jumpingVariance);
		return c * Math.exp(expt);
	}

	public Collection<Sample> samples(int k) {
		ArrayList<Sample> samples = new ArrayList<Sample>();
		return samples;
	}
	
	public Sample sample() {
		Double newX = propose();
		double Qratio = Q(value, newX) / Q(newX, value);
		double Aratio = pdist.eval(newX) / pdist.eval(value);
		double a = Aratio * Qratio;

		if(a >= 1.0) { 
			value = newX;
		} else { 
			double p = rand.nextDouble();
			value = p <= a ? newX : value;
		}
		
		return new Sample(value);
	}
}
