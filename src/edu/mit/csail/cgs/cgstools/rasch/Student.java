/*
 * Author: tdanford
 * Date: Mar 9, 2009
 */
package edu.mit.csail.cgs.cgstools.rasch;

import edu.mit.csail.cgs.utils.models.Model;

public class Student extends Model implements Comparable<Student> {

	public String name;
	public Double skill;
	
	public Student() {}
	public Student(String n, Double s) { 
		name = n; skill = s;
	}
	
	public boolean equals(Object o) { 
		if(!(o instanceof Student)) { return false; }
		Student t = (Student)o;
		return t.name.equals(name);
	}
	
	public int hashCode() { return name.hashCode(); }
	
	public String toString() { return name; }
	
	public int compareTo(Student s) { 
		return name.compareTo(s.name);
	}
}
