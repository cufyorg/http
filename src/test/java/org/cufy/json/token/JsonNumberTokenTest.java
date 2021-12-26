package org.cufy.json.token;

import org.cufy.http.json.token.JsonNumberToken;
import org.cufy.http.json.token.JsonTokenSource;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonNumberTokenTest {
	@Test
	public void parseNumber() throws IOException {
		@Language("json")
		String source = "-0.3e+10";

		assertEquals(
				new BigDecimal("-0.3e+10"),
				new JsonNumberToken(new JsonTokenSource(new StringReader(source))).nextNumber(),
				"Expected Value Mismatch"
		);
	}
}
