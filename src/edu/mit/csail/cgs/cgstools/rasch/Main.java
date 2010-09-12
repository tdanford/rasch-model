/*
 * Author: tdanford
 * Date: Mar 9, 2009
 */
package edu.mit.csail.cgs.cgstools.rasch;

import java.util.*;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.FunctionModel;
import edu.mit.csail.cgs.cgstools.slicer.Sample;
import edu.mit.csail.cgs.cgstools.slicer.SliceSampler;

public class Main {

	public static void main(String[] args) { 
		RandomDataGenerator gen = new RandomDataGenerator();
		
		Teacher t1 = gen.createTeacher("A", 0.0);
		Teacher t2 = gen.createTeacher("B", 0.0);
		Teacher t3 = gen.createTeacher("C", 0.0);
		
		boolean includeMom = true;

		double good = 1.5;
		double bad = -1.5;
		
		Student s0 = gen.createStudent("s0", bad);
		Student s1 = gen.createStudent("s1", bad);
		Student s2 = gen.createStudent("s2", bad);
		Student s3 = gen.createStudent("s3", bad);
		Student s4 = gen.createStudent("s4", bad);
		
		Student s5 = gen.createStudent("s5", good);
		Student s6 = gen.createStudent("s6", good);
		Student s7 = gen.createStudent("s7", good);
		Student s8 = gen.createStudent("s8", good);
		Student s9 = gen.createStudent("s9", good);
		
		LinkedList<Course> courses = new LinkedList<Course>();

		for(int i = 0; i < 10; i++) { 
			courses.add(gen.generateCourse(t1, s0, s1, s2, s3, s4));
			courses.add(gen.generateCourse(t2, s5, s6, s7, s8, s9));
			
			if(includeMom) { 
				courses.add(gen.generateCourse(t3, s0, s1, s2, s3, s4, s5, s6, s7, s8, s9));
			}
		}

		RaschEvaluator eval = new RaschEvaluator(courses);
		
		CourseViewFrame frame = new CourseViewFrame(new CourseViewPanel(gen, eval));

	}
}
