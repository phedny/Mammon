package org.mammon.math.candidate;

import java.math.BigInteger;

public class SequentialCandidateStrategy implements CandidateStrategy {
	private BigInteger candidate = BigInteger.valueOf(1);

	@Override
	public BigInteger next() {
		candidate = candidate.add(BigInteger.ONE);
		return candidate;
	}

}
