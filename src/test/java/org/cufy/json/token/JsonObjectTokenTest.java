package org.cufy.json.token;

import org.cufy.http.json.JsonNumber;
import org.cufy.http.json.JsonString;
import org.cufy.http.json.token.JsonObjectToken;
import org.cufy.http.json.token.JsonTokenSource;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonObjectTokenTest {
	@Test
	public void parseObjectSimple() throws IOException {
		String source = "{  \"x\": {  \"y\": 90} }";

		assertEquals(
				Map.of(
						new JsonString("x"), Map.of(
								new JsonString("y"), new JsonNumber(new BigDecimal(90))
						)
				),
				new JsonObjectToken(new JsonTokenSource(new StringReader(source))).nextElement(),
				"Unexpected parse value"
		);
	}
}
