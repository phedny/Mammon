package org.mammon.sandbox.real.example;

import org.mammon.math.Gq;
import org.mammon.math.GqSetupFactory;
import org.mammon.math.Group;
import org.mammon.math.Z;
import org.mammon.math.Group.Element;
import org.mammon.messaging.AvailableAtRuntime;
import org.mammon.messaging.Identifiable;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.util.HashCodeUtil;

@AvailableAtRuntime(BrandsSchemeSetup.class)
public class ExampleSetup
		implements
		Identifiable,
		BrandsSchemeSetup<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	private final Gq group = new Gq(GqSetupFactory.factory(7919, 63353).generateSetup());

	private final Z field = new Z(7919);

	private final ExampleSignatureHashFunction signatureHashFunction = new ExampleSignatureHashFunction(group, field);

	private final ExamplePaymentHashFunction paymentHashFunction = new ExamplePaymentHashFunction(group, field);

	private final Group.Element<Gq> g;

	private final Group.Element<Gq> g1;

	private final Group.Element<Gq> g2;

	public ExampleSetup() {
		g = group.getRandomElement();
		g1 = group.getRandomElement();
		g2 = group.getRandomElement();
	}

	@Override
	public Element<Gq> getGenerator(int i) {
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
	public Gq getGroup() {
		return group;
	}

	@Override
	public Z getFiniteField() {
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
