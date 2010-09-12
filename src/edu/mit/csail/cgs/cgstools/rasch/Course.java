/*
 * Author: tdanford
 * Date: Mar 9, 2009
 */
package edu.mit.csail.cgs.cgstools.rasch;

import java.util.*;

import edu.mit.csail.cgs.utils.models.Model;

public class Course extends Model {

	public Teacher teacher;
	public Student[] students;
	public Integer[] grades;
	
	public Course() {}
	
	public Course(Teacher t, Map<Student,Integer> gs) { 
		teacher = t;
		students = new Student[gs.size()];
		grades = new Integer[students.length];
		
		int i = 0;
		for(Student s : gs.keySet()) { 
			students[i] = s;
			grades[i] = gs.get(s);
			i++;
		}
	}
	
	public int findStudentIndex(Student s) { 
		for(int i = 0; i < students.length; i++) { 
			if(students[i].equals(s)) { 
				return i;
			}
		}
		return -1;
	}
	
	public int size() { return students.length; }
	
	public int passed() { 
		int p = 0; 
		for(int i = 0; i < grades.length; i++) { 
			p += grades[i] == 1 ? 1 : 0;
		}
		return p;
	}
}
