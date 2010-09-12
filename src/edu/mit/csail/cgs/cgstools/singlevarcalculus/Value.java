/*
 * Author: tdanford
 * Date: Mar 18, 2009
 */
package edu.mit.csail.cgs.cgstools.singlevarcalculus;

import edu.mit.csail.cgs.utils.models.Accessor;
import edu.mit.csail.cgs.utils.models.ArrayFieldAccessor;
import edu.mit.csail.cgs.utils.models.FieldAccessor;
import edu.mit.csail.cgs.utils.models.Model;

public class Value<T extends Model> extends ConstantFunction {
	
	public T model;
	public Accessor<T> field;
	public Double defaultValue; 
	
	public Value(T m, String fname) throws NoSuchFieldException { 
		this(m, new FieldAccessor(m.getClass(), fname));
	}

	public Value(T m, String aname, int idx) throws NoSuchFieldException { 
		this(m, new ArrayFieldAccessor(m.getClass(), aname, idx));
	}

	public Value(T m, Accessor<T> f) { 
		super(0.0);
		model = m; 
		field = f;
		defaultValue = 0.0;
		
		if(!Model.isSubclass(field.getType(), Double.class)) { 
			throw new IllegalArgumentException();
		}
	}
	
	public String toString() { return field.toString(); }
	
	public Double eval(Double input) {
		Double val = (Double)field.get(model);
		if(val == null) { val = defaultValue; }
		return val;
	}
}
