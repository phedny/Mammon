package org.mammon.messaging.impl;


public class NoOpIdentityMapper implements IdentityMapper {

	@Override
	public String deserializeIdentity(String identity) {
		return identity;
	}

	@Override
	public String serializeIdentity(String identity) {
		return identity;
	}

}
