/*
 * Author: tdanford
 * Date: Mar 17, 2009
 */
package edu.mit.csail.cgs.cgstools.singlevarcalculus;

import edu.mit.csail.cgs.utils.models.Model;

public class RealInterval extends Model {

	public Double lower, upper;
	
	public RealInterval(Double l, Double u) { 
		lower = l; 
		upper = u;
	}
	
	public double measure() { return upper-lower; }
	
	public RealInterval intersection(RealInterval intv) { 
		if(!intersects(intv)) { throw new IllegalArgumentException(); }
		return new RealInterval(Math.max(lower, intv.lower), 
				Math.min(upper, intv.upper));
	}
	
	public RealInterval union(RealInterval intv) { 
		if(!intersects(intv)) { throw new IllegalArgumentException(); }
		return new RealInterval(Math.min(lower, intv.lower), 
				Math.max(upper, intv.upper));
	}
	
	public boolean intersects(RealInterval intv) {
		return (lower < intv.lower && upper > intv.lower) ||
			(intv.lower < lower && intv.upper > lower);
	}
}
