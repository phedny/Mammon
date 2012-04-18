package org.mammon.math;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ZRandomElementTest {
	private final int q;
	private FiniteField<Z> field;

	public ZRandomElementTest(int q) {
		this.q = q;
	}

	@Before
	public void createFiniteField() {
		field = new Z(q);
	}

	@Test
	public void shouldBeCreated() {
		assertNotNull(field);
	}

	@Test
	public void shouldReturnUniformDitributedRandomElement() {
		ElementCounter<Z> counter = new ElementCounter<Z>();
		for (int index = 0; index < q*q; index++) {
			counter.increaseCountFor(field.getRandomElement());
		}

		assertTrue(counter.isUniform(Double.valueOf(q).doubleValue()));
	}

	@Parameters
	public static Collection<Object[]> data() {
		List<Object[]> data = new ArrayList<Object[]>();
		data.add(new Object[]{11});
		data.add(new Object[]{17});
		data.add(new Object[]{23});
		data.add(new Object[]{37});
		return data;
	}
}

class ElementCounter<F extends FiniteField<F>> {
	private final Map<FiniteField.Element<F>, AtomicInteger> elementCount = new HashMap<FiniteField.Element<F>, AtomicInteger>();
	private final AtomicInteger callCount = new AtomicInteger();

	public ElementCounter() {
	}

	public boolean isUniform(double epsilon) {
		double expectation = callCount.doubleValue() / Double.valueOf(elementCount.keySet().size()).doubleValue();
		for (Map.Entry<FiniteField.Element<F>, AtomicInteger> entry: elementCount.entrySet()) {
			double frequency = entry.getValue().doubleValue() / callCount.doubleValue();
			if (Math.abs(frequency - expectation) > epsilon) {
				return false;
			}
		}
		return true;
	}

	public void increaseCountFor(FiniteField.Element<F> element) {
		callCount.incrementAndGet();
		if (!elementCount.containsKey(element)) {
			elementCount.put(element, new AtomicInteger());
		}
		elementCount.get(element).incrementAndGet();
	}
}
