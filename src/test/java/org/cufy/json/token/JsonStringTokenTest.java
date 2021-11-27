package org.cufy.json.token;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonStringTokenTest {
	@Test
	public void parseString() throws IOException {
		@Language("json")
		String source = "\"\\\"Hello \\\\ \\u0057\\u006f\\u0072\\u006c\\u0064\\\"\"";

		assertEquals(
				"\"Hello \\ World\"",
				new JsonStringToken(new JsonTokenSource(new StringReader(source))).nextString(),
				"Expected Value Mismatch"
		);
		assertEquals(
				"\"\\\"Hello \\\\ World\\\"\"",
				new JsonStringToken(new JsonTokenSource(new StringReader(source))).nextElement().json(),
				"Expected Json Mismatch"
		);
	}
}
