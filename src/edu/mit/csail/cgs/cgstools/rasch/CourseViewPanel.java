/*
 * Author: tdanford
 * Date: Mar 11, 2009
 */
package edu.mit.csail.cgs.cgstools.rasch;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class CourseViewPanel extends JPanel {
	
	private Thread resamplingThread;
	private Runnable sampler;
	private Course[] courses;
	
	public CourseViewPanel(RandomDataGenerator generator, RaschEvaluator eval) { 
		courses = eval.courses();
		
		Arrays.sort(courses, new Comparator<Course>() { 
			public int compare(Course c1, Course c2) { 
				return c1.teacher.name.compareTo(c2.teacher.name);
			}
		});
		
		sampler = eval.sampler(100, generator);
		resamplingThread = null;
	}
	
	public Action sampleAction() { 
		return new AbstractAction("Re-sample") { 
			public void actionPerformed(ActionEvent e) {
				startSampler();
			}
		};
	}

	public synchronized void startSampler() { 
		if(resamplingThread == null) { 
			resamplingThread = new Thread(new Runnable() { 
				public void run() { 
					sampler.run();
					repaint();
					resamplingThread = null;
				}
			});
			resamplingThread.start();
		}		
	}
	
	protected void paintComponent(Graphics g) { 
		super.paintComponent(g);
		
		int w = getWidth(), h = getHeight();
		int cwidth = (int)Math.floor((double)w / (double)(courses.length+2));
		
		Map<Student,Integer> sinds = new HashMap<Student,Integer>();
		int idx = 0;
		for(int i = 0; i < courses.length; i++) { 
			for(Student s : courses[i].students) { 
				if(!sinds.containsKey(s)) { 
					sinds.put(s, idx++);
				}
			}
		}
		
		int gheight = (int)Math.floor((double)h / (double)(idx+2));
		
		int minDim = Math.min(gheight, cwidth);
		cwidth = gheight = minDim;
		int x = 0;
		int y = 0;
		
		for(int i = 0; i < courses.length; i++) {
			
			x = i * cwidth;
			
			for(int j = 0; j < courses[i].students.length; j++) {
				
				boolean passed = courses[i].grades[j] == 1;
				int sidx = sinds.get(courses[i].students[j]);
				y = sidx * gheight;

				if(passed) { 
					g.setColor(Color.black);
				} else { 
					g.setColor(Color.white);
				}
				
				g.fillOval(x+1, y+1, cwidth-2, gheight-2);
				g.setColor(Color.black);
				g.drawOval(x+1, y+1, cwidth-2, gheight-2);
			}
			
			y = gheight * (sinds.size()+1); 
			g.setColor(createScaleColor(Logistic.eval(courses[i].teacher.skill)));
			g.fillOval(x+1, y+1, cwidth-2, gheight-2);
			g.setColor(Color.black);
			g.drawOval(x+1, y+1, cwidth-2, gheight-2);
		}
		
		for(Student s : sinds.keySet()) { 
			int j = sinds.get(s);
			x = (courses.length+1) * cwidth;
			y = j * gheight;
			Color c = createScaleColor(Logistic.eval(s.skill));
			
			g.setColor(c);
			g.fillOval(x+1, y+1, cwidth-2, gheight-2);
			g.setColor(Color.black);
			g.drawOval(x+1, y+1, cwidth-2, gheight-2);
		}
	}
	
	public Color createScaleColor(double sc) { 
		if(sc < 0.0 || sc > 1.0) { throw new IllegalArgumentException(); }
		int intensity = 255-(int)Math.floor(255.0 * sc);
		return new Color(intensity, intensity, intensity);
	}
}

class CourseViewFrame extends JFrame {
	
	private CourseViewPanel panel;
	private JButton sampleButton;
	
	public CourseViewFrame(CourseViewPanel cvp) { 
		super("Courses");
		panel = cvp;
		
		Container c = (Container)getContentPane();
		c.setLayout(new BorderLayout());
		c.add(panel, BorderLayout.CENTER);
		panel.setPreferredSize(new Dimension(400, 200));
		
		JPanel buttons = new JPanel(); buttons.setLayout(new FlowLayout());
		buttons.add(sampleButton = new JButton(panel.sampleAction()));
		
		c.add(buttons, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		showFrame();
	}
	
	public void showFrame() { 
		SwingUtilities.invokeLater(new Runnable() { 
			public void run() { 
				setVisible(true);
				setLocation(100, 100);
				pack();
			}
		});
	}
}
