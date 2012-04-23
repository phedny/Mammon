package org.mammon.sandbox;

public interface IdentityMapper {

	String serializeIdentity(String identity);
	
	String deserializeIdentity(String identity);
	
}
