package org.mammon.sandbox.objects.example;

import org.mammon.math.Group;
import org.mammon.math.Group.Element;
import org.mammon.messaging.AvailableAtRuntime;
import org.mammon.messaging.Identifiable;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.util.HashCodeUtil;

@AvailableAtRuntime(BrandsSchemeSetup.class)
public class ExampleSetup
		implements
		Identifiable,
		BrandsSchemeSetup<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	private final ExampleRandomGenerator randomGenerator = new ExampleRandomGenerator();

	private final ExampleGroup group = new ExampleGroup(randomGenerator);

	private final ExampleFiniteField field = new ExampleFiniteField(randomGenerator);

	private final ExampleSignatureHashFunction signatureHashFunction = new ExampleSignatureHashFunction(group, field);

	private final ExamplePaymentHashFunction paymentHashFunction = new ExamplePaymentHashFunction(group, field);

	private final Group.Element<ExampleGroup> g;

	private final Group.Element<ExampleGroup> g1;

	private final Group.Element<ExampleGroup> g2;

	public ExampleSetup() {
		g = group.getRandomElement();
		g1 = group.getRandomElement();
		g2 = group.getRandomElement();
	}

	@Override
	public Element<ExampleGroup> getGenerator(int i) {
		switch (i) {
		case 0:
			return g;
		case 1:
			return g1;
		case 2:
			return g2;
		default:
			throw new ArrayIndexOutOfBoundsException();
		}
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
				&& paymentHashFunction.equals(other.paymentHashFunction) && g.equals(other.g) && g1.equals(other.g1)
				&& g2.equals(other.g2);
	}

	@Override
	public int hashCode() {
		int hashCode = HashCodeUtil.SEED;
		hashCode = HashCodeUtil.hash(hashCode, group);
		hashCode = HashCodeUtil.hash(hashCode, signatureHashFunction);
		hashCode = HashCodeUtil.hash(hashCode, paymentHashFunction);
		hashCode = HashCodeUtil.hash(hashCode, g);
		hashCode = HashCodeUtil.hash(hashCode, g2);
		hashCode = HashCodeUtil.hash(hashCode, g2);
		return hashCode;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("ExampleSetup[");
		sb.append(group.toString()).append(",(");
		sb.append(g.toString()).append(",");
		sb.append(g1.toString()).append(",");
		sb.append(g2.toString()).append("),");
		sb.append(signatureHashFunction.toString()).append(",");
		sb.append(paymentHashFunction.toString()).append("]");
		return sb.toString();
	}

	@Override
	public String getIdentity() {
		return "ExampleSetup";
	}

}
