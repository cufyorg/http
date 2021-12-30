/*
 *	Copyright 2021 Cufy
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	    http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package org.cufy.http.json.token;

import org.cufy.http.json.JsonNull;
import org.cufy.http.json.Json;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Json null parsing token.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.24
 */
@ApiStatus.Internal
public class JsonNullToken extends AbstractJsonToken {
	/**
	 * Construct a new null token for the given {@code source}.
	 *
	 * @param source the source for the constructed token to read from.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.3.0 ~2021.11.23
	 */
	public JsonNullToken(@NotNull JsonTokenSource source) {
		super(source);
	}

	@NotNull
	@Override
	public JsonNull nextElement() throws IOException {
		if (
				this.source.read() == 'n' &&
				this.source.read() == 'u' &&
				this.source.read() == 'l' &&
				this.source.read() == 'l'
		)
			return Json.NULL;

		throw new JsonTokenException("Invalid Null", this.source.nextIndex());
	}
}
