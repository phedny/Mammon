package org.mammon.sandbox.objects.example;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.mammon.math.Group.Element;
import org.mammon.sandbox.HashCodeUtil;
import org.mammon.scheme.brands.BrandsSchemeSetup;

public class ExampleSetup
		implements
		BrandsSchemeSetup<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	private final ExampleRandomGenerator randomGenerator = new ExampleRandomGenerator();

	private final ExampleGroup group = new ExampleGroup(randomGenerator);

	private final ExampleFiniteField field = new ExampleFiniteField(randomGenerator);

	private final ExampleSignatureHashFunction signatureHashFunction = new ExampleSignatureHashFunction(group, field);

	private final ExamplePaymentHashFunction paymentHashFunction = new ExamplePaymentHashFunction(group, field);

	private final Element<ExampleGroup>[] generators;

	@SuppressWarnings("unchecked")
	public ExampleSetup() {
		generators = (Element<ExampleGroup>[]) Array.newInstance(Element.class, 3);
		generators[0] = group.getRandomElement();
		generators[1] = group.getRandomElement();
		generators[2] = group.getRandomElement();
	}

	@Override
	public Element<ExampleGroup>[] getGenerators() {
		return generators.clone();
	}

	@Override
	public ExampleGroup getGroup() {
		return group;
	}

	@Override
	public ExampleFiniteField getFiniteField() {
		return field;
	}

	@Override
	public ExamplePaymentHashFunction getPaymentHash() {
		return paymentHashFunction;
	}

	@Override
	public ExampleSignatureHashFunction getSignatureHash() {
		return signatureHashFunction;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ExampleSetup)) {
			return false;
		}
		ExampleSetup other = (ExampleSetup) obj;
		return group.equals(other.group) && signatureHashFunction.equals(other.signatureHashFunction)
				&& paymentHashFunction.equals(other.paymentHashFunction) && Arrays.equals(generators, other.generators);
	}

	@Override
	public int hashCode() {
		int hashCode = HashCodeUtil.SEED;
		hashCode = HashCodeUtil.hash(hashCode, group);
		hashCode = HashCodeUtil.hash(hashCode, signatureHashFunction);
		hashCode = HashCodeUtil.hash(hashCode, paymentHashFunction);
		hashCode = HashCodeUtil.hash(hashCode, generators);
		return hashCode;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("ExampleSetup[");
		sb.append(group.toString()).append(",(");
		sb.append(generators[0].toString()).append(",");
		sb.append(generators[1].toString()).append(",");
		sb.append(generators[2].toString()).append("),");
		sb.append(signatureHashFunction.toString()).append(",");
		sb.append(paymentHashFunction.toString()).append("]");
		return sb.toString();
	}

}
