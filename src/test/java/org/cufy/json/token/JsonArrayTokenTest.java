package org.cufy.json.token;

import org.cufy.http.json.JsonNumber;
import org.cufy.http.json.JsonString;
import org.cufy.http.json.token.JsonArrayToken;
import org.cufy.http.json.token.JsonTokenSource;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonArrayTokenTest {
	@Test
	public void parseArray() throws IOException {
		String source = "[{  \"x\": [{  \"y\": 90}, 0.1]}, -80]";

		assertEquals(
				List.of(
						Map.of(
								new JsonString("x"), List.of(
										Map.of(
												new JsonString("y"), new JsonNumber(new BigDecimal(90))
										),
										new JsonNumber(new BigDecimal("0.1"))
								)
						),
						new JsonNumber(new BigDecimal(-80))
				),
				new JsonArrayToken(new JsonTokenSource(new StringReader(source))).nextElement(),
				"Unexpected parse value"
		);
	}

	@Test
	public void reportUnclosedArray() {
		String source = "[\"element\"";

		assertThrows(
				IllegalArgumentException.class,
				() ->
						new JsonArrayToken(new JsonTokenSource(new StringReader(source)))
								.nextElement(),
				"Expected to report invalid array"
		);
	}

	@Test
	public void reportUnrepeatedArray() {
		String source = "[\"element\"\"element\"]";

		assertThrows(
				IllegalArgumentException.class,
				() ->
						new JsonArrayToken(new JsonTokenSource(new StringReader(source)))
								.nextElement(),
				"Expected to report invalid array"
		);
	}
}
