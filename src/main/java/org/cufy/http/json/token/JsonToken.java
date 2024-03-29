/*
 *	Copyright 2021-2022 Cufy
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

import org.cufy.http.json.JsonElement;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * A stateful one time read json token.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.23
 */
@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
@ApiStatus.Internal
public interface JsonToken {
	/**
	 * Return the sole element of this token.
	 *
	 * @return the element.
	 * @throws IOException        when any i/o exception occurs.
	 * @throws JsonTokenException if the token has been consumed or if the token is
	 *                            invalid.
	 * @since 0.3.0 ~2021.11.23
	 */
	@NotNull
	@Contract(mutates = "this")
	JsonElement nextElement() throws IOException;
}
