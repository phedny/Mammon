package org.mammon.sandbox.objects.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.messaging.AvailableAtRuntime;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.PersistAs;
import org.mammon.messaging.ReturnsEnclosing;
import org.mammon.util.HashCodeUtil;

@AvailableAtRuntime(FiniteField.class)
public class ExampleFiniteField implements Identifiable<String>, FiniteField<ExampleFiniteField> {

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
		@ReturnsEnclosing
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

		@Override
		public <G extends Group<G>> Group.Element<G> raise(Group.Element<G> groupElement) {
			return groupElement; // TODO Correctly implement the raise method
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof ExampleElement)) {
				return false;
			}
			return simplify().myEquals(((ExampleElement) obj).simplify());
		}

		@Override
		public int hashCode() {
			return simplify().myHashCode();
		}

		public abstract ExampleElement simplify();

		public abstract int myHashCode();

		public abstract boolean myEquals(ExampleElement obj);

	}

	public class StaticElement extends ExampleElement {

		private final String value;

		@FromPersistent(FiniteField.Element.class)
		public StaticElement(@PersistAs("value") String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		@Override
		public boolean myEquals(ExampleElement obj) {
			if (obj == null || !(obj instanceof StaticElement)) {
				return false;
			}
			return value.equals(((StaticElement) obj).value);
		}

		@Override
		public int myHashCode() {
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

		@FromPersistent(FiniteField.Element.class)
		public AdditionElement(@PersistAs("operands") ExampleElement... operands) {
			this.operands = operands;
		}

		public AdditionElement(ExampleElement firstOperand, ExampleElement secondOperand) {
			operands = new ExampleElement[] { firstOperand, secondOperand };
		}

		public ExampleElement[] getOperands() {
			return operands.clone();
		}

		@Override
		public boolean myEquals(ExampleElement obj) {
			if (obj == null || !(obj instanceof AdditionElement)) {
				return false;
			}
			AdditionElement other = (AdditionElement) obj;
			return Arrays.deepEquals(operands, other.operands);
		}

		@Override
		public int myHashCode() {
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

		@FromPersistent(FiniteField.Element.class)
		public MultiplicationElement(@PersistAs("operands") ExampleElement... operands) {
			this.operands = operands;
		}

		public MultiplicationElement(ExampleElement firstOperand, ExampleElement secondOperand) {
			operands = new ExampleElement[] { firstOperand, secondOperand };
		}

		public ExampleElement[] getOperands() {
			return operands.clone();
		}

		@Override
		public boolean myEquals(ExampleElement obj) {
			if (obj == null || !(obj instanceof MultiplicationElement)) {
				return false;
			}
			MultiplicationElement other = (MultiplicationElement) obj;
			return Arrays.deepEquals(operands, other.operands);
		}

		@Override
		public int myHashCode() {
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

			Set<ExampleElement> toRemove = new HashSet<ExampleElement>();
			for (ExampleElement operand : operands) {
				if (operand instanceof ExponentiationElement) {
					ExponentiationElement eElt = (ExponentiationElement) operand;
					if (eElt.exponent.equals(last) && operands.contains(eElt.base)) {
						toRemove.add(eElt);
						toRemove.add(eElt.base);
					}
				}
			}
			for (ExampleElement operand : toRemove) {
				operands.remove(operand);
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

			List<MultiplicationElement> addOperands = new ArrayList<MultiplicationElement>();
			for (ExampleElement element : operands) {
				if (element instanceof AdditionElement) {
					if (addOperands.isEmpty()) {
						for (ExampleElement element2 : ((AdditionElement) element).operands) {
							addOperands.add(new MultiplicationElement(element2));
						}
					} else {
						List<MultiplicationElement> newAddOperands = new ArrayList<MultiplicationElement>();
						for (MultiplicationElement addOperand : addOperands) {
							for (ExampleElement element2 : ((AdditionElement) element).operands) {
								List<ExampleElement> elements = new ArrayList<ExampleElement>();
								elements.addAll(Arrays.asList(addOperand.operands));
								elements.add(element2);
								newAddOperands.add(new MultiplicationElement(elements
										.toArray(new ExampleElement[elements.size()])));
							}
						}
						addOperands = newAddOperands;
					}
				} else if (addOperands.isEmpty()) {
					addOperands.add(new MultiplicationElement(element));
				} else {
					for (int i = 0; i < addOperands.size(); i++) {
						List<ExampleElement> elements = new ArrayList<ExampleElement>();
						elements.addAll(Arrays.asList(addOperands.get(i).operands));
						elements.add(element);
						addOperands.set(i, new MultiplicationElement(elements.toArray(new ExampleElement[elements
								.size()])));
					}
				}
			}

			if (addOperands.size() < 2) {
				return new MultiplicationElement(operands.toArray(new ExampleElement[operands.size()]));
			} else {
				return new AdditionElement(addOperands.toArray(new ExampleElement[addOperands.size()]));
			}
		}
	}

	public class ExponentiationElement extends ExampleElement {

		private final ExampleElement base;

		private final ExampleElement exponent;

		@FromPersistent(FiniteField.Element.class)
		public ExponentiationElement(@PersistAs("base") ExampleElement base,
				@PersistAs("exponent") ExampleElement exponent) {
			this.base = base;
			this.exponent = exponent;
		}

		public ExampleElement getBase() {
			return base;
		}

		public ExampleElement getExponent() {
			return exponent;
		}

		@Override
		public boolean myEquals(ExampleElement obj) {
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
		public int myHashCode() {
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

	@Override
	public String getIdentity() {
		return "ExampleFiniteField";
	}

}
