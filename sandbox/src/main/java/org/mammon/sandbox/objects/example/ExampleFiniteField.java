package org.mammon.sandbox.objects.example;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

import org.mammon.math.FiniteField;
import org.mammon.sandbox.HashCodeUtil;

public class ExampleFiniteField implements FiniteField<ExampleFiniteField> {

	private final ExampleRandomGenerator randomGenerator;

	private final ExampleElement zero = new StaticElement("0");

	private final ExampleElement one = new StaticElement("1");

	private final ExampleElement last = new StaticElement("-1");

	public ExampleFiniteField(ExampleRandomGenerator randomGenerator) {
		this.randomGenerator = randomGenerator;
	}
	
	public static ExampleElement last() {
		return new ExampleFiniteField(null).last;
	}

	@Override
	public ExampleElement getOne() {
		return one;
	}

	@Override
	public ExampleElement getRandomElement() {
		return new StaticElement(randomGenerator.nextString());
	}

	@Override
	public ExampleElement getZero() {
		return zero;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ExampleFiniteField)) {
			return false;
		}
		return this == obj;
	}

	@Override
	public int hashCode() {
		return ExampleFiniteField.class.getName().hashCode();
	}

	@Override
	public String toString() {
		return ExampleFiniteField.class.getSimpleName();
	}

	public abstract class ExampleElement implements FiniteField.Element<ExampleFiniteField> {

		@Override
		public ExampleElement add(Element<ExampleFiniteField> other) {
			try {
				return new AdditionElement(this, (ExampleElement) other);
			} catch (ClassCastException e) {
				throw new IllegalArgumentException("Argument must be of type ExampleElement");
			}
		}

		@Override
		public ExampleFiniteField getFiniteField() {
			return ExampleFiniteField.this;
		}

		@Override
		public ExampleElement getInverse() {
			return new ExponentiationElement(this, last);
		}

		@Override
		public ExampleElement multiply(Element<ExampleFiniteField> other) {
			try {
				return new MultiplicationElement(this, (ExampleElement) other);
			} catch (ClassCastException e) {
				throw new IllegalArgumentException("Argument must be of type ExampleElement");
			}
		}

		@Override
		public Element<ExampleFiniteField> getOpposite() {
			return new MultiplicationElement(this, last);
		}

		@Override
		public org.mammon.math.FiniteField.Element<ExampleFiniteField> exponentiate(
				org.mammon.math.FiniteField.Element<ExampleFiniteField> other) {
			return new ExponentiationElement(this, (ExampleElement) other);
		}

		public abstract ExampleElement simplify();

	}

	public class StaticElement extends ExampleElement {

		private final String value;

		public StaticElement(String value) {
			this.value = value;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof StaticElement)) {
				return false;
			}
			return value.equals(((StaticElement) obj).value);
		}

		@Override
		public int hashCode() {
			return value.hashCode();
		}

		@Override
		public String toString() {
			return value;
		}

		@Override
		public ExampleElement simplify() {
			return this;
		}

	}

	public class AdditionElement extends ExampleElement {

		private final ExampleElement[] operands;

		private AdditionElement(ExampleElement... operands) {
			this.operands = operands;
		}

		private AdditionElement(ExampleElement firstOperand, ExampleElement secondOperand) {
			operands = new ExampleElement[] { firstOperand, secondOperand };
		}

		public ExampleElement[] getOperands() {
			return operands.clone();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof AdditionElement)) {
				return false;
			}
			AdditionElement other = (AdditionElement) obj;
			return Arrays.deepEquals(operands, other.operands);
		}

		@Override
		public int hashCode() {
			int hashCode = HashCodeUtil.SEED;
			hashCode = HashCodeUtil.hash(hashCode, operands);
			return hashCode;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("(");
			for (ExampleElement operand : operands) {
				sb.append(operand).append(" + ");
			}
			sb.setLength(sb.length() - 3);
			return sb.append(")").toString();
		}

		@Override
		public ExampleElement simplify() {
			TreeSet<ExampleElement> operands = new TreeSet<ExampleElement>(new HashCodeComparator<ExampleElement>());
			operands.addAll(Arrays.asList(this.operands));
			for (boolean execute = true; execute;) {
				execute = false;
				TreeSet<ExampleElement> newOperands = new TreeSet<ExampleElement>(
						new HashCodeComparator<ExampleElement>());
				for (ExampleElement element : operands) {
					if (element instanceof AdditionElement) {
						newOperands.addAll(Arrays.asList(((AdditionElement) element).operands));
						execute = true;
					} else {
						newOperands.add(element);
					}
				}
				operands = newOperands;
			}

			TreeSet<ExampleElement> newOperands = new TreeSet<ExampleElement>(new HashCodeComparator<ExampleElement>());
			for (ExampleElement element : operands) {
				newOperands.add(element.simplify());
			}

			return new AdditionElement(newOperands.toArray(new ExampleElement[newOperands.size()]));
		}

	}

	public class MultiplicationElement extends ExampleElement {

		private final ExampleElement[] operands;

		private MultiplicationElement(ExampleElement... operands) {
			this.operands = operands;
		}

		private MultiplicationElement(ExampleElement firstOperand, ExampleElement secondOperand) {
			operands = new ExampleElement[] { firstOperand, secondOperand };
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof MultiplicationElement)) {
				return false;
			}
			MultiplicationElement other = (MultiplicationElement) obj;
			return Arrays.deepEquals(operands, other.operands);
		}

		@Override
		public int hashCode() {
			int hashCode = HashCodeUtil.SEED;
			hashCode = HashCodeUtil.hash(hashCode, operands);
			return hashCode;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("(");
			for (ExampleElement operand : operands) {
				sb.append(operand).append(" * ");
			}
			sb.setLength(sb.length() - 3);
			return sb.append(")").toString();
		}

		@Override
		public ExampleElement simplify() {
			TreeSet<ExampleElement> operands = new TreeSet<ExampleElement>(new HashCodeComparator<ExampleElement>());
			for (ExampleElement element : this.operands) {
				operands.add(element.simplify());
			}

			for (boolean execute = true; execute;) {
				execute = false;
				TreeSet<ExampleElement> newOperands = new TreeSet<ExampleElement>(
						new HashCodeComparator<ExampleElement>());
				for (ExampleElement element : operands) {
					if (element instanceof MultiplicationElement) {
						newOperands.addAll(Arrays.asList(((MultiplicationElement) element).operands));
						execute = true;
					} else {
						newOperands.add(element);
					}
				}
				operands = newOperands;
			}

			return new MultiplicationElement(operands.toArray(new ExampleElement[operands.size()]));
		}
	}

	public class ExponentiationElement extends ExampleElement {

		private final ExampleElement base;

		private final ExampleElement exponent;

		private ExponentiationElement(ExampleElement base, ExampleElement exponent) {
			this.base = base;
			this.exponent = exponent;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof ExponentiationElement)) {
				return false;
			}
			ExponentiationElement other = (ExponentiationElement) obj;
			if (base.equals(other.base) && exponent.equals(other.exponent)) {
				return true;
			}
			if (base.equals(other.exponent) && exponent.equals(other.base)) {
				return true;
			}
			return false;
		}

		@Override
		public int hashCode() {
			int hashCode = HashCodeUtil.SEED;
			hashCode = HashCodeUtil.hash(hashCode, base);
			hashCode = HashCodeUtil.hash(hashCode, exponent);
			return hashCode;
		}

		@Override
		public String toString() {
			return "(" + base.toString() + " ^ " + exponent.toString() + ")";
		}

		@Override
		public ExampleElement simplify() {
			ExampleElement base = this.base.simplify();
			ExampleElement exponent = this.exponent.simplify();
			if (exponent instanceof AdditionElement) {
				AdditionElement e = (AdditionElement) exponent;
				ExampleElement[] operands = new ExampleElement[e.operands.length];
				for (int i = 0; i < e.operands.length; i++) {
					operands[i] = new ExponentiationElement(base, e.operands[i]);
				}
				return new MultiplicationElement(operands).simplify();
			}
			if (base instanceof ExponentiationElement) {
				ExponentiationElement e = (ExponentiationElement) base;
				return new ExponentiationElement(e.base, new MultiplicationElement(e.exponent, exponent).simplify());
			}
			if (base instanceof MultiplicationElement) {
				MultiplicationElement e = (MultiplicationElement) base;
				ExampleElement[] operands = new ExampleElement[e.operands.length];
				for (int i = 0; i < e.operands.length; i++) {
					operands[i] = new ExponentiationElement(e.operands[i], exponent);
				}
				return new MultiplicationElement(operands).simplify();
			}
			return new ExponentiationElement(base, exponent);
		}
	}

	private class HashCodeComparator<T> implements Comparator<T> {

		@Override
		public int compare(T o1, T o2) {
			return o1.hashCode() - o2.hashCode();
		}

	}

}
