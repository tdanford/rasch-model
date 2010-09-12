/*
 * Author: tdanford
 * Date: Mar 9, 2009
 */
package edu.mit.csail.cgs.cgstools.rasch;

import java.util.*;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.FunctionModel;
import edu.mit.csail.cgs.cgstools.slicer.Sampler;
import edu.mit.csail.cgs.cgstools.slicer.SliceSampler;
import edu.mit.csail.cgs.cgstools.slicer.UnnormalizedGaussian;

public class RandomDataGenerator {
	
	private Random rand;
	private FunctionModel tPrior, sPrior;
	
	private Map<String,Teacher> teachers;
	private Map<String,Student> students;
	private Map<String,Double> teacherSkills, studentSkills;
	
	public RandomDataGenerator() { 
		rand = new Random();
		
		tPrior = new UnnormalizedGaussian(0.0, 1.0);
		sPrior = new UnnormalizedGaussian(0.0, 1.0);
		
		teachers = new TreeMap<String,Teacher>();
		students = new TreeMap<String,Student>();
		teacherSkills = new HashMap<String,Double>();
		studentSkills = new HashMap<String,Double>();
	}
	
	public FunctionModel getTeacherPrior() { return tPrior; }
	public FunctionModel getStudentPrior() { return sPrior; }
	
	public Double getTeacherSkill(Teacher t) { 
		return teacherSkills.get(t.name);
	}
	
	public Double getStudentSkill(Student s) { 
		return studentSkills.get(s.name);
	}
	
	public Teacher generateTeacher(String n) { 
		double w = 1.0;
		double x = 0.0;
		SliceSampler sampler = new SliceSampler(tPrior, w, x);
		for(int i = 0; i < 5; i++) { x = sampler.nextX(); }
		return createTeacher(n, x);
	}
	
	public Teacher createTeacher(String n, double sk) { 
		Teacher t = new Teacher(n, sk);
		if(!teachers.containsKey(n)) { 
			teachers.put(n, t);
			teacherSkills.put(t.name, t.skill);
			return t;
		} else { 
			throw new IllegalArgumentException(n);
		}
	}
	
	public Student[] generateStudents(int k) { 
		int base = students.size();
		Student[] array = new Student[k];
		for(int i = 0; i < k; i++) { 
			String n = String.format("student#%d", (base + i));
			array[i] = generateStudent(n);
		}
		return array;
	}

	public Student generateStudent(String n) { 
		double w = 1.0;
		double x = 0.0;
		SliceSampler sampler = new SliceSampler(sPrior, w, x);
		for(int i = 0; i < 5; i++) { x = sampler.nextX(); }
		
		return createStudent(n, x);
	}
	
	public Student createStudent(String n, double x) { 
		Student s = new Student(n, x);
		if(!students.containsKey(n)) { 
			students.put(n, s);
			studentSkills.put(s.name, s.skill);
			return s;
		} else { 
			throw new IllegalArgumentException(n);
		}		
	}
	
	public Student getStudent(String n) { return students.get(n); }
	public Teacher getTeacher(String n) { return teachers.get(n); }
	
	public Course generateCourse(Teacher t, Student... s) { 
		Map<Student,Integer> grades = new HashMap<Student,Integer>();
		double tSkill = t.skill;
		
		for(int i = 0; i < s.length; i++) { 
			double sSkill = s[i].skill;
			double linear = sSkill + tSkill;
			double p = Logistic.eval(linear);
			double randp = rand.nextDouble();
			int grade = -1;
			
			if(randp <= p) { 
				grade = 1;
			} else { 
				grade = 0;
			}
			
			grades.put(s[i], grade);
		}
		
		return new Course(t, grades);
	}
}
