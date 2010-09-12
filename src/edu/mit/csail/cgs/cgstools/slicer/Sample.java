package edu.mit.csail.cgs.cgstools.slicer;

import java.util.*;
import edu.mit.csail.cgs.utils.models.Model;

public class Sample extends Model { 
	
	public Double[] sample;
	
	public Sample() {}
	
	public Sample(double x) { sample = new Double[] { x }; }
	
	public Sample(double[] s) { 
		sample = new Double[s.length];
		for(int i = 0; i < s.length; i++) { sample[i] = s[i]; }
	}
	
	public Sample(Sample s) { 
		sample = s.sample.clone();
	}
	
	public Sample(Sample s, int dropIdx) { 
		sample = new Double[s.sample.length-1];
		for(int i = 0; i < sample.length; i++) {  
			if(i < dropIdx) { 
				sample[i] = s.sample[i];
			} else { 
				sample[i] = s.sample[i+1];
			}
		}
	}
	
	public void scale(double v) { 
		for(int i = 0; i < sample.length; i++) { 
			sample[i] *= v;
		}
	}

	public static double mean(Collection<Sample> s) { 
		return mean(s, 0);
	}
	
	public static double mean(Collection<Sample> s, int i) { 
		double m = 0.0;
		int c = 0;
		for(Sample ss : s) { 
			m += ss.sample[i];
			c += 1;
		}
		return m / (double)Math.max(1, c);
	}
	
	public static double var(Collection<Sample> s) { 
		return var(s, 0);
	}
	
	public static double var(Collection<Sample> s, int i) { 
		double m = mean(s, i);
		double var = 0.0;
		int c = 0;
		for(Sample ss : s) { 
			double v = ss.sample[i];
			double d = v-m;
			d *= d;
			var += d;
			c += 1;
		}
		return var / (double)Math.max(1, c);		
	}
	
	public static Collection<Sample> marginalize(Collection<Sample> ss, Integer... inds) { 
		for(int i = 0; i < inds.length; i++) { 
			ss = marginalize(ss, inds[i]);
		}
		return ss;
	}

	public static Collection<Sample> marginalize(Collection<Sample> ss, Integer idx) { 
		ArrayList<Sample> ms = new ArrayList<Sample>();
		for(Sample s : ss) { 
			ms.add(new Sample(s, idx));
		}
		return ms;
	}
	
	public Sample expectation(Collection<Sample> ss) { 
		double[] d = null;
		
		for(Sample s : ss) { 
			if(d == null) { 
				d = new double[s.sample.length];
			} else if (d.length != s.sample.length) { 
				throw new IllegalArgumentException(s.asJSON().toString());
			}
			
			for(int i = 0; i < d.length; i++) { 
				d[i] += s.sample[i];
			}
		}
		
		for(int i = 0; i < d.length; i++) { 
			d[i] /= (double)(Math.max(1, ss.size()));
		}
		
		return new Sample(d);
	}
}
