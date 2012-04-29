package org.mammon.messaging.impl;

public interface IdentityMapper {

	String serializeIdentity(String identity);
	
	String deserializeIdentity(String identity);
	
}
