/*
 * Author: tdanford
 * Date: May 11, 2009
 */
package edu.mit.csail.cgs.cgstools.slicer;

import java.util.*;
import cern.jet.stat.*;

public class FTest {
	
	public static double Fcdf(double x, int d1, int d2) {
		double dd1 = (double)d1, dd2 = (double)d2;
		double ix = (dd1 * x) / (dd1 * x + dd2);
		double a = dd1 / 2.0, b = dd2 / 2.0;
		return regularizedIncompleteBeta(ix, a, b);
	}
	
	public static double expt(double base, int ex) { 
		if (ex > 0) {
			double value = 1.0;
			for(int i = 0; i < ex; i++) { 
				value *= base;
			}
			return value;
		} else if (ex < 0) { 
			double value = 1.0;
			int nex = -ex;
			for(int i = 0; i < nex; i++) { 
				value /= base;
			}
			return value;
		} else { 
			return 1.0;
		}
	}

	public static double regularizedIncompleteBeta(double x, double a, double b) { 
		return Gamma.incompleteBeta(x, a, b) / Gamma.beta(a, b);
	}
}
