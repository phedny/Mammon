package org.mammon.brands.example;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.mammon.brands.SignatureHashFunction;
import org.mammon.brands.Group.Element;

public class ExampleSignatureHashFunction implements SignatureHashFunction<ExampleGroup> {

	private final ExampleGroup g;

	private Map map = new HashMap();

	public ExampleSignatureHashFunction(ExampleGroup g) {
		this.g = g;
	}

	private Element<ExampleGroup> realHash(Element<ExampleGroup>... val) {
		Map curr = map;
		for (int i = 0; i < 4; i++) {
			Map next = (Map) curr.get(val[i].hashCode());
			if (next == null) {
				next = new HashMap();
				curr.put(val[i].hashCode(), next);
			}
			curr = next;
		}
		if (curr.containsKey(Integer.valueOf(val[4].hashCode()))) {
			return (Element<ExampleGroup>) curr.get(Integer.valueOf(val[4].hashCode()));
		} else {
			Element<ExampleGroup> element = g.getRandomElement(null);
			curr.put(Integer.valueOf(val[4].hashCode()), element);
			return element;
		}
	}

	@Override
	public Element<ExampleGroup> hash(Element<ExampleGroup> blindedIdentity, Element<ExampleGroup> commitment,
			Element<ExampleGroup>... secretValues) {
		Element<ExampleGroup>[] s = (Element<ExampleGroup>[]) Array.newInstance(Element.class, 5);
		s[0] = blindedIdentity;
		s[1] = commitment;
		s[2] = secretValues[0];
		s[3] = secretValues[1];
		s[4] = secretValues[2];
		return realHash(s);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ExampleSignatureHashFunction)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return ExampleSignatureHashFunction.class.getName().hashCode();
	}

	@Override
	public String toString() {
		return ExampleSignatureHashFunction.class.getSimpleName();
	}

}
