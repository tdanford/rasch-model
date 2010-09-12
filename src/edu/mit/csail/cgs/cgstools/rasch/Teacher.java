/*
 * Author: tdanford
 * Date: Mar 9, 2009
 */
package edu.mit.csail.cgs.cgstools.rasch;

import edu.mit.csail.cgs.utils.models.Model;

public class Teacher extends Model implements Comparable<Teacher> {

	public String name;
	public Double skill;
	
	public Teacher() {}
	public Teacher(String n, Double s) { 
		name = n; skill = s;
	}
	
	public boolean equals(Object o) { 
		if(!(o instanceof Teacher)) { return false; }
		Teacher t = (Teacher)o;
		return t.name.equals(name);
	}
	
	public int hashCode() { return name.hashCode(); }
	
	public String toString() { return name; }
	
	public int compareTo(Teacher t) { 
		return name.compareTo(t.name);
	}
}
