package org.mammon.brands.example;

import java.util.HashMap;
import java.util.Map;

import org.mammon.brands.Group.Element;

@SuppressWarnings("unchecked")
public class OracleHashFunction {

	protected final ExampleGroup g;
	private Map map = new HashMap();
	private int d;

	public OracleHashFunction(ExampleGroup g, int depth) {
		this.g = g;
		d = depth - 1;
	}

	protected Element<ExampleGroup> oracle(Object... val) {
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
			return (Element<ExampleGroup>) curr.get(Integer.valueOf(val[d].hashCode()));
		} else {
			Element<ExampleGroup> element = g.getRandomElement(null);
			curr.put(Integer.valueOf(val[d].hashCode()), element);
			return element;
		}
	}

}