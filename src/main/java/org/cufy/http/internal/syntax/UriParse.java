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
package org.cufy.http.internal.syntax;

import org.jetbrains.annotations.ApiStatus;

import java.util.regex.Pattern;

/**
 * Compiled patterns to break down the components of an uri.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.21
 */
@ApiStatus.Internal
public final class UriParse {
	/**
	 * A pattern that groups the components of the "Authority" component of a Uri. (no
	 * validation)
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern AUTHORITY = Pattern.compile(
			"^(?:(?<UserInfo>" + UriRegExp.USERINFO + ")@)?(?<Host>" + UriRegExp.HOST +
			")(?::(?<Port>" + UriRegExp.PORT + "))?$"
	);

	/**
	 * A pattern that groups the components of a Uri. (no validation)
	 *
	 * @since <a href="https://tools.ietf.org/html/rfc3986#appendix-B">
	 * 		RFC3986 Appendix-B
	 * 		</a>
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern URI = Pattern.compile(
			"^(?:(?<Scheme>[^:/?#]+):)?(?://(?<Authority>[^/?#]*))?(?<Path>[^?#]*)(?:\\?(?<Query>[^#]*))?(?:#(?<Fragment>.*))?$"
	);

	/**
	 * Utility classes shall not have instances.
	 *
	 * @throws AssertionError when called.
	 * @since 0.0.1 ~2021.03.21
	 */
	private UriParse() {
		throw new AssertionError("No instance for you!");
	}
}
