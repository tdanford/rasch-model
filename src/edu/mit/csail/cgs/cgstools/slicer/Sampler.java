/*
 * Author: tdanford
 * Date: Mar 9, 2009
 */
package edu.mit.csail.cgs.cgstools.slicer;

import java.util.Collection;

public interface Sampler {
	public Collection<Sample> samples(int k);
}