package org.mammon.sandbox;

import java.util.HashMap;
import java.util.Map;

import org.mammon.math.FiniteField;
import org.mammon.math.Z;

@SuppressWarnings("unchecked")
public class RealOracleHashFunction {

	protected final Z f;
	private Map map = new HashMap();
	private int d;

	public RealOracleHashFunction(Z f, int depth) {
		this.f = f;
		d = depth - 1;
	}

	protected FiniteField.Element<Z> oracle(Object... val) {
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
			return (FiniteField.Element<Z>) curr.get(Integer.valueOf(val[d].hashCode()));
		} else {
			FiniteField.Element<Z> element = f.getRandomElement();
			curr.put(Integer.valueOf(val[d].hashCode()), element);
			return element;
		}
	}

}