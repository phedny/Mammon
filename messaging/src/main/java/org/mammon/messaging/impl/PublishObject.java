package org.mammon.messaging.impl;

import org.json.JSONException;
import org.json.JSONObject;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.Message;
import org.mammon.messaging.PersistAs;

public class PublishObject implements Message {

	private final JSONObject object;

	@FromPersistent(PublishObject.class)
	public PublishObject(@PersistAs("object") JSONObject object) {
		this.object = object;
	}

	public PublishObject(String json) throws JSONException {
		object = new JSONObject(json);
	}

	public JSONObject getObject() {
		return object;
	}

}
