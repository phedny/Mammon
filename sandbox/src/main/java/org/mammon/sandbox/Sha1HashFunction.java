package org.mammon.sandbox;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.mammon.math.FiniteField;
import org.mammon.math.Z;

public class Sha1HashFunction {

	protected final Z f;

	public Sha1HashFunction(Z f, int depth) {
		this.f = f;
	}

	protected FiniteField.Element<Z> oracle(Object... val) {
		StringBuilder sb = new StringBuilder("hashOf:");
		for (int i = 0; i < val.length; i++) {
			sb.append(val[i].toString()).append(",");
		}
		sb.append(".");
		
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			byte[] bytes = sb.toString().getBytes("UTF-8");
			byte[] digest = sha1.digest(bytes);
			return f.element(new BigInteger(digest));
		} catch (NoSuchAlgorithmException e) {
			throw new AssertionError(e);
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
	}

}