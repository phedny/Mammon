package org.mammon.brands.example;

import java.lang.reflect.Array;
import java.security.SecureRandom;
import java.util.Arrays;

import org.mammon.brands.BrandsSchemeSetup;
import org.mammon.brands.Group.Element;

public class ExampleSetup implements
		BrandsSchemeSetup<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	private final ExampleGroup group = new ExampleGroup();

	private final ExampleSignatureHashFunction signatureHashFunction = new ExampleSignatureHashFunction(group);

	private final ExamplePaymentHashFunction paymentHashFunction = new ExamplePaymentHashFunction();

	private final Element<ExampleGroup>[] generators;

	@SuppressWarnings("unchecked")
	public ExampleSetup() {
		SecureRandom r = new SecureRandom();
		generators = (Element<ExampleGroup>[]) Array.newInstance(Element.class, 3);
		generators[0] = group.getRandomElement(r);
		generators[1] = group.getRandomElement(r);
		generators[2] = group.getRandomElement(r);
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
