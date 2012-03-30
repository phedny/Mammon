package org.mammon.math;


/**
 * This interface can be used for generic typing, to bind classes of an actual
 * implementation compile-time.
 */
public interface FiniteField<F extends FiniteField<F>> {

	/**
	 * @return the zero element of this finite field.
	 */
	Element<F> getZero();

	/**
	 * @return the unit (one) element of this finite field.
	 */
	Element<F> getOne();

	/**
	 * @return a random element from this finite field.
	 */
	Element<F> getRandomElement();

	/**
	 * This interface represents an element from a finite field.
	 */
	public interface Element<F extends FiniteField<F>> {

		/**
		 * @return the finite field this element belongs to.
		 */
		F getFiniteField();

		/**
		 * @param other
		 *            an element from the same finite field to add together.
		 * @return the sum of this element and the other element.
		 */
		Element<F> add(Element<F> other);

		/**
		 * @return the opposite of this element.
		 */
		Element<F> getOpposite();

		/**
		 * @param other
		 *            an element from the same finite field to multiply
		 *            together.
		 * @return the product of this element and the other element.
		 */
		Element<F> multiply(Element<F> other);

		/**
		 * @return the inverse of this element.
		 */
		Element<F> getInverse();

		/**
		 * @param exponent
		 *            an element from the same finite field to use as exponent.
		 * @return the exponentiation of this element by the exponent.
		 */
		FiniteField.Element<F> exponentiate(FiniteField.Element<F> other);
	}

}
