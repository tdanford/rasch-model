/*
 * Author: tdanford
 * Date: Mar 9, 2009
 */
package edu.mit.csail.cgs.cgstools.rasch;

import java.util.*;

import edu.mit.csail.cgs.cgstools.singlevarcalculus.FunctionModel;
import edu.mit.csail.cgs.cgstools.singlevarcalculus.binary.ProductFunction;
import edu.mit.csail.cgs.cgstools.slicer.Sample;
import edu.mit.csail.cgs.cgstools.slicer.Sampler;
import edu.mit.csail.cgs.cgstools.slicer.SliceSampler;
import edu.mit.csail.cgs.cgstools.slicer.UnnormalizedGaussian;

/**
 * p_ij is the probability of student j passing a course taught by teacher i
 * 
 * The 'Rasch model' corresponds to a model for p_ij, determined by two 
 * underlying "skill" scores and the inverse-logistic function. 
 * 
 * p_ij = inv-logistic( T_i + S_j )
 * 
 * where : 
 * 		   T_i is the "skill" of teacher i
 * 		   S_j is the "skill" of student j
 * 
 * @author tdanford
 *
 */
public class RaschEvaluator {
	
	// The total sets of students, courses, and teachers.
	private LinkedList<Course> courses;
	private Set<Teacher> teachers;
	private Set<Student> students;
	
	// Records the courses taken by each student.
	private Map<Student,ArrayList<Course>> studentCourses;
	
	// Records the courses taught by each teacher.
	private Map<Teacher,ArrayList<Course>> teacherCourses;

	private UnnormalizedGaussian tPrior, sPrior;
	
	/*
	 * T_i : teacher i's skill
	 * S_j : student j's skill
	 * g_j[c] : the grade of student j in course c
	 * C_i : set of courses taught by teacher i
	 * C_j : set of courses taken by student j
	 * 
	 * P(g_j[c] | T_i, S_j) = 
	 * 			g_j[c] 	   * inv-logistic( T_i, S_j ) + 
	 * 			(1-g_j[c]) * (1.0 - inv-logistic( T_i, S_j )) 
	 *
	 * T_i | S* \prop 
	 * 		prior(T_i) * \prod_j \prod_{c \in C_j \cup C_i} P(g_j[c] | T_i, S_j)
	 * 
	 * S_j | T* \prop 
	 * 	    prior(S_j) * \prod_{C_j} P(g_j[c] | T_i, S_j )
	 * 
	 *  (We only need to calculate the conditional probabilities of the T_i's and 
	 *   S_j's up to proportionality, since slice-sampling will still sample from 
	 *   the "real" normalized probability distribution.) 
	 */
	
	public RaschEvaluator(Collection<Course> cs) {
		
		tPrior = new UnnormalizedGaussian(0.0, 1.0);
		sPrior = new UnnormalizedGaussian(0.0, 1.0);
		
		courses = new LinkedList<Course>(cs);
		teachers = new TreeSet<Teacher>();
		students = new TreeSet<Student>();
		studentCourses = new TreeMap<Student,ArrayList<Course>>();
		teacherCourses = new TreeMap<Teacher,ArrayList<Course>>();
		
		for(Course c : courses) { 
			teachers.add(c.teacher);
			if(!teacherCourses.containsKey(c.teacher)) { 
				teacherCourses.put(c.teacher, new ArrayList<Course>());
			}
			teacherCourses.get(c.teacher).add(c);
			
			for(Student s : c.students) { 
				students.add(s);
				if(!studentCourses.containsKey(s)) { 
					studentCourses.put(s, new ArrayList<Course>());
				}
				studentCourses.get(s).add(c);
			}
		}
	}
	
	public int getNumStudentCourses(Student s) {
		if(!studentCourses.containsKey(s)) { 
			throw new IllegalArgumentException(s.toString());
		}
		return studentCourses.get(s).size();
	}

	/**
	 * Calculates the fraction of courses taken by the student <tt>s</tt> in which that
	 * student received a passing grade.
	 * 
	 * @param s
	 * @return
	 */
	public double studentPassingRate(Student s) { 
		int total = 0; 
		int passed = 0;
		
		if(!students.contains(s)) { throw new IllegalArgumentException(s.toString()); }
		
		for(Course c : studentCourses.get(s)) { 
			int idx = c.findStudentIndex(s);
			assert idx != -1;
			
			total += 1;
			passed += c.grades[idx] == 1 ? 1 : 0;
		}
		
		return (double)passed / (double)Math.max(1, total);
	}
	
	/**
	 * Calculates the fraction students taught by the teacher <tt>t</tt>, in all 
	 * that teacher's courses, that passed.  
	 *  
	 * @param t
	 * @return
	 */
	public double teacherPassingRate(Teacher t) { 
		int total = 0;
		int passed = 0;
		
		if(!teachers.contains(t)) { throw new IllegalArgumentException(t.toString()); }
		
		for(Course c : teacherCourses.get(t)) { 
			total += c.size();
			passed += c.passed();
		}
		
		return (double)passed / (double)Math.max(1, total);
	}

	/**
	 * Samples initial values, for all teacher and student skills, from the 
	 * prior distributions.
	 */
	public void init() {
		double w = 1.0;
		for(Teacher t : teachers) {
			double x = 0.0;
			SliceSampler sampler = new SliceSampler(tPrior, w, x);
			for(int i = 0; i < 5; i++) { 
				x = sampler.nextX();
			}
			t.skill = x;
		}
		for(Student s : students) { 
			double x = 0.0;
			SliceSampler sampler = new SliceSampler(sPrior, w, x);
			for(int i = 0; i < 5; i++) { 
				x = sampler.nextX();
			}
			s.skill = x;
		}
	}
	
	/**
	 * A helper class -- this is the likelihood function of a skill score, 
	 * for a particular student grade.  Since the likelihood of the student 
	 * 'skills' (given the teacher 'skills') is identical to the likelihood of 
	 * the teacher skills (given the student skills), we can use the same 
	 * class to represent *both* likelihood functions.  
	 */
	public static class GradeLikelihood extends FunctionModel {
		
		public Integer grade;
		public Double skill;
		
		public GradeLikelihood() {}
		
		public GradeLikelihood(int g, double sk) { 
			grade = g;
			skill = sk;
		}

		public Double eval(Double input) {
			double linear = input + skill;
			double p = Logistic.eval(linear);
			double g = (double)grade;
			return g*p + (1.0-g)*(1.0-p);
		}
	}
	
	public FunctionModel createConditionalLikelihood(Teacher t) { 
		FunctionModel p = tPrior;
		
		for(Course c : teacherCourses.get(t)) { 
			for(int i = 0; i < c.students.length; i++) { 
				int grade = c.grades[i];
				double sk = c.students[i].skill;
				p = new ProductFunction(p, new GradeLikelihood(grade, sk));
			}
		}
		
		return p;
	}
	
	public FunctionModel createConditionalLikelihood(Student s) { 
		FunctionModel p = sPrior;
		for(Course c : studentCourses.get(s)) {
			int i = c.findStudentIndex(s);
			int grade = c.grades[i];
			double sk = c.teacher.skill;
			p = new ProductFunction(p, new GradeLikelihood(grade, sk));
		}
		return p;
	}

	/**
	 * Performs a single "round" of Gibbs sampling -- that is, every teacher 
	 * and every student has their skill value resampled exactly once. 
	 */
	public void gibbsRound() { 
		double w = 1.0;
		
		for(Teacher t : teachers) {
			double osk = t.skill;
			FunctionModel p = createConditionalLikelihood(t);
			
			SliceSampler sampler = new SliceSampler(p, w, t.skill);
			t.skill = sampler.nextX();
			//System.out.println(String.format("\t%s : %.3f -> %.3f", 
			//		t.name, osk, t.skill));
		}
		
		for(Student s : students) { 
			double osk = s.skill;
			FunctionModel p = createConditionalLikelihood(s);
			
			SliceSampler sampler = new SliceSampler(p, w, s.skill);
			s.skill = sampler.nextX();			
			//System.out.println(String.format("\t%s : %.3f -> %.3f", 
			//		s.name, osk, s.skill));
		}
		
		tPrior.setMean(teacherSkillMean());
		tPrior.setVar(teacherSkillVar());
		
		sPrior.setMean(studentSkillMean());
		sPrior.setVar(studentSkillVar());
	}
	
	public Double teacherSkillMean() { 
		double m = 0.0;
		for(Teacher t : teachers) { m += t.skill; }
		m /= (double)Math.max(teachers.size(), 1);
		return m;
	}
	
	public static double minVar = 0.001;
	
	public Double teacherSkillVar() { 
		double m = teacherSkillMean();
		double v = 0.0;
		for(Teacher t : teachers) { 
			double d = t.skill - m;
			v += (d * d); 
		}
		v /= (double)Math.max(1, teachers.size()-1);
		return Math.max(minVar, v);
	}

	public Double studentSkillMean() { 
		double m = 0.0;
		// Constraining the students skills to have a mean=0, for purposes
		// of identification in the model...
		/*
		for(Student t : students) { m += t.skill; }
		m /= (double)Math.max(students.size(), 1);
		*/
		return m;
	}
	
	public Double studentSkillVar() { 
		double m = studentSkillMean();
		double v = 0.0;
		// Constraining the students skills to have a var=1.0, for purposes
		// of identification in the model...
		/*
		for(Student t : students) { 
			double d = t.skill - m;
			v += (d * d); 
		}
		v /= (double)Math.max(1, students.size()-1);
		return Math.max(v, minVar);
		*/
		return 1.0;
	}

	/**
	 * Performs <tt>rounds</tt> iterations of Gibbs sampling. 
	 * 
	 * @param rounds
	 */
	public void gibbsSample(int rounds) { 
		for(int i = 0; i < rounds; i++) {
			gibbsRound();
			//System.out.print(" " + i); System.out.flush();
		}
		//System.out.println();
	}
	
	public Collection<Sample> sample(int burn, int spacing, int N) { 
		ArrayList<Sample> samples = new ArrayList<Sample>();
		
		Teacher[] tarray = teachers();
		Student[] sarray = students();
		
		gibbsSample(burn);
		System.out.println(String.format("Sampling (%d) ", N)); System.out.flush();

		double[] values = new double[tarray.length + sarray.length]; 

		for(int i = 0; i < N; i++) { 
			//System.out.print(String.format("Sample %d : ", i));
			gibbsSample(spacing);
			for(int j = 0; j < tarray.length; j++) { 
				values[j] = tarray[j].skill;
			}
			for(int j = 0; j < sarray.length; j++) { 
				values[j+tarray.length] = sarray[j].skill;
			}
			
			Sample s = new Sample(values);
			samples.add(s);
			
			//System.out.println(String.format("\tTeachers: %s", tPrior.toString()));
			//System.out.println(String.format("\tStudents: %s", sPrior.toString()));
			
			if(i > 0 && i % 100 == 0) { System.out.print("."); System.out.flush(); }
			if(i > 0 && i % 1000 == 0) { System.out.print("(" + i + ")"); System.out.flush(); }
		}
		System.out.println();
		
		return samples;
	}
	
	public Teacher[] teachers() { return teachers.toArray(new Teacher[0]); }
	public Student[] students() { return students.toArray(new Student[0]); }
	public Course[] courses() { return courses.toArray(new Course[0]); }
	
	public Runnable sampler(int samples, RandomDataGenerator g) { 
		return new SamplingRunnable(g, samples);
	}
	
	private class SamplingRunnable implements Runnable {
		
		private int samples;
		private RandomDataGenerator gen;
		
		public SamplingRunnable(RandomDataGenerator g, int s) { gen = g; samples = s; }
		
		public void run() { 
			Collection<Sample> tsamples = sample(20, 5, samples);

			Teacher[] teachers = teachers();
			
			for(int i = 0; i < teachers.length; i++) {
				
				double sampled = Sample.mean(tsamples, i);
				double sampledvar = Sample.var(tsamples, i); 
				double realValue = gen.getTeacherSkill(teachers[i]);
				
				System.out.println(String.format("%s -> %.3f (+/- %.3f) = %.3f ",
						teachers[i].toString(), 
						sampled, 2.0 * sampledvar,
						realValue));
				
				teachers[i].skill = sampled;
			}

			Student[] students = students();
			for(int i = 0; i < students.length; i++) { 

				double sampled = Sample.mean(tsamples, i+teachers.length);
				double sampledvar = Sample.var(tsamples, i+teachers.length); 
				double realValue = gen.getStudentSkill(students[i]);
				
				System.out.println(String.format("%s -> %.3f (+/- %.3f) = %.3f",
						students[i].toString(), 
						sampled, 2.0 * sampledvar,
						realValue));
				
				students[i].skill = sampled;
			}

		}
	}
}
