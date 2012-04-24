package org.mammon.sandbox.real.bank;

import org.mammon.math.Gq;
import org.mammon.messaging.PublicationConverter;
import org.mammon.sandbox.real.example.ExampleBank;
import org.mammon.sandbox.real.example.ExampleSetup;

public class ConvertToRemoteBank implements PublicationConverter<ExampleBank> {

	@Override
	public RemoteBank convert(ExampleBank bank, String newIdentity) {
		return new RemoteBank((ExampleSetup) bank.getSetup(), (Gq.GqElement) bank.getPublicKey(), newIdentity);
	}

}
