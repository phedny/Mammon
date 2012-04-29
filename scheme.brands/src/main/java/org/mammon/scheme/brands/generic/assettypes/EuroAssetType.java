package org.mammon.scheme.brands.generic.assettypes;

import org.mammon.AssetType;
import org.mammon.messaging.AvailableAtRuntime;
import org.mammon.messaging.Identifiable;

@AvailableAtRuntime(AssetType.class)
public final class EuroAssetType implements Identifiable, AssetType {

	@Override
	public String getCallSign() {
		return "EUR";
	}

	@Override
	public String getIdentity() {
		return "assettype-EUR";
	}

}