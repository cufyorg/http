/*
 *	Copyright 2021-2022 Cufy and ProgSpaceSA
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
package org.cufy.http.internal.syntax;

import org.jetbrains.annotations.ApiStatus;

import java.util.regex.Pattern;

/**
 * Compiled patterns to break down the components of a mime.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.03.21
 */
@ApiStatus.Internal
public final class MimeParse {
	/**
	 * A pattern that groups the components of a mime. (no validation)
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final Pattern MEDIA_TYPE = Pattern.compile(
			"^(?<Type>" + MimeRegExp.TYPE + ")" +
			"(?:/(?<Subtype>" + MimeRegExp.SUB_TYPE + "))?" +
			"(?:;(?<Parameters>" + MimeRegExp.PARAMETER + "*))?"
	);

	/**
	 * Utility classes shall not have instances.
	 *
	 * @throws AssertionError when called.
	 * @since 0.0.1 ~2021.03.21
	 */
	private MimeParse() {
		throw new AssertionError("No instance for you!");
	}
}
