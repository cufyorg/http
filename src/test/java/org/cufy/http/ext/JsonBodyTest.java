package org.cufy.http.ext;

import org.cufy.http.body.JsonBody;
import org.cufy.http.json.JsonArray;
import org.cufy.http.json.JsonObject;
import org.cufy.http.json.JsonString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonBodyTest {
	@Test
	public void jsonPath() {
		JsonBody body = new JsonBody();

		body.put("x", new JsonObject());
		body.put("x?.x", new JsonArray());
		body.put("x.x.2", new JsonString("HI"));
		body.put("x.x??.name", new JsonString("HI"));
		body.put("x.y?.h", new JsonString("HI"));

		Assertions.assertEquals(
				"{\"x\":{\"x\":[null,null,\"HI\"]}}",
				body.toString(),
				"Unexpected path access result"
		);
	}
}
