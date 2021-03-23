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
package org.cufy.http.syntax;

import java.util.regex.Pattern;

/**
 * Compiled patterns of the regex-s in the class {@link ABNFRegExp}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.22
 */
public final class ABNFPattern {
	/**
	 * A compiled pattern of the regex {@link ABNFRegExp#CRLF}.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	public static final Pattern CRLF = Pattern.compile(ABNFRegExp.CRLF);

	/**
	 * Utility classes shall have no instances.
	 *
	 * @throws AssertionError when called.
	 * @since 0.0.1 ~2021.03.21
	 */
	private ABNFPattern() {
		throw new AssertionError("No instance for you!");
	}
}
