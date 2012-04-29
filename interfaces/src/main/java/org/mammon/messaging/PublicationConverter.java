package org.mammon.messaging;

public interface PublicationConverter<T extends Identifiable> {
	
	Identifiable convert(T object, String newIdentity);

}
