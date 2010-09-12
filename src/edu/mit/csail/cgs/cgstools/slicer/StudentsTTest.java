/*
 * Author: tdanford
 * Date: Mar 19, 2009
 */
package edu.mit.csail.cgs.cgstools.slicer;

import java.util.*;

import cern.jet.stat.Probability;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.VariableFunction;

public class StudentsTTest {

	public static void main(String[] args) {
		int n = 10;
		Double[] s1 = new Double[n], s2 = new Double[n];
		Random rand = new Random();
		
		for(int i = 0; i < n; i++) { 
			s1[i] = rand.nextGaussian();
			s2[i] = rand.nextGaussian() + 1.5;
		}
		
		StudentsTTest ttest = new StudentsTTest(2*n-2);
		double t = ttest.calculateT(s1, s2);
		int samples = 1000;
		
		System.out.println(String.format("T: %.3f", t));
		
		for(int i = 0; i < 5; i++) { 
			System.out.println(String.format("p: %.5f (%.5f)", 
					ttest.samplePValue(t, samples), ttest.pvalue(t)));
		}
		
		t = 1.475884;
		ttest = new StudentsTTest(5);
		double p1 = ttest.pvalue(t);
		//double p2 = ttest.pvalue(-t);
		double p2 = p1;
		System.out.println(String.format("%f + %f = %f", p1, p2, (p1+p2)));
	}
	
	public Integer degrees;
	public UnnormalizedStudent studentDist;
	
	public StudentsTTest(int k) { 
		degrees = k;
		studentDist = new UnnormalizedStudent(new VariableFunction(), degrees);
	}
	
	public static Double calculateT(Double[] v, Double mu) { 
		Double m = 0.0;
		for(Double val : v) { m += val; }
		m /= (double)v.length;
		
		Double s = 0.0;
		for(Double val : v) { 
			double diff = val - m;
			diff *= diff;
			s += diff;
		}
		
		s /= (double)v.length;
		s = Math.sqrt(s);
		
		double t = (m - mu) / (s / Math.sqrt((double)v.length));
		return t;
	}
	
	public static Double calculateT(Double[] v1, Double[] v2) { 
		Double m1 = 0.0, m2 = 0.0;
		
		for(int i = 0; i < v1.length; i++) { 
			m1 += v1[i];
		}
		
		for(int i = 0; i < v2.length; i++) { 
			m2 += v2[i];
		}
		
		m1 /= (double)v1.length; 
		m2 /= (double)v2.length;
		
		Double s1 = 0.0, s2 = 0.0;
		
		for(int i = 0; i < v1.length; i++) { 
			double d = v1[i] - m1;
			d *= d;
			s1 += d;
		}
		
		for(int i = 0; i < v2.length; i++) { 
			double d = v2[i] - m2;
			d *= d;
			s2 += d;
		}
			
		s1 /= (double)v1.length; 
		s2 /= (double)v2.length; 
		
		double tnumer = m1-m2;
		double s12 = Math.sqrt((s1 + s2) / 2);
		double tdenom = s12 * Math.sqrt(2.0 / (double)v1.length);
		if(v1.length != v2.length) { throw new IllegalArgumentException(); }
		
		return tnumer / tdenom;
	}
	
	public Double pvalue(Double t) {
		double k = (double)degrees;
		double lowert = Probability.studentT(k, Math.min(t, -t));
		return 2.0 * lowert;
	}
	
	public Double samplePValue(Double t, int samples) { 
		double cutoff = Math.min(t, -t);
		
		int exceeded = 0;
		SliceSampler sampler = new SliceSampler(studentDist, 0.5, t);
		
		for(int i = 0; i < samples; i++) {
			for(int k = 0; k < 3; k++) { 
				sampler.nextX();
			}
			
			if(sampler.nextX() <= cutoff) {
				exceeded += 1;
			}
		}
		
		return (double)exceeded / (double)samples;
	}
	
}
