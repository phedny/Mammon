package org.mammon.sandbox;

import java.util.HashMap;
import java.util.Map;

import org.mammon.math.FiniteField;
import org.mammon.sandbox.objects.example.ExampleFiniteField;

@SuppressWarnings("unchecked")
public class OracleHashFunction {

	protected final ExampleFiniteField f;
	private Map map = new HashMap();
	private int d;

	public OracleHashFunction(ExampleFiniteField f, int depth) {
		this.f = f;
		d = depth - 1;
	}

	protected FiniteField.Element<ExampleFiniteField> oracle(Object... val) {
		Map curr = map;
		for (int i = 0; i < d; i++) {
			Map next = (Map) curr.get(Integer.valueOf(val[i].hashCode()));
			if (next == null) {
				next = new HashMap();
				curr.put(Integer.valueOf(val[i].hashCode()), next);
			}
			curr = next;
		}
		if (curr.containsKey(Integer.valueOf(val[d].hashCode()))) {
			return (FiniteField.Element<ExampleFiniteField>) curr.get(Integer.valueOf(val[d].hashCode()));
		} else {
			FiniteField.Element<ExampleFiniteField> element = f.getRandomElement();
			curr.put(Integer.valueOf(val[d].hashCode()), element);
			return element;
		}
	}

}