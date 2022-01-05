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
package org.cufy.http.internal.syntax;

import org.jetbrains.annotations.ApiStatus;

import java.util.regex.Pattern;

/**
 * Compiled patterns to break down the components of a request-line.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.21
 */
@ApiStatus.Internal
public final class HttpParse {
	/**
	 * A pattern that groups the components of a request. (no validation)
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Pattern REQUEST = Pattern.compile(
			"^(?<RequestLine>[^\r\n]+)" + AbnfRegExp.CRLF +
			"(?<Headers>(?:[^\r\n]+" + AbnfRegExp.CRLF + ")*)" +
			"(?:" + AbnfRegExp.CRLF + "(?<Body>.*))?$"
	);
	/**
	 * A pattern that groups the components of a request-line. (no validation)
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern REQUEST_LINE = Pattern.compile(
			"^(?<Method>[A-Z]+) (?<Uri>[^ ]*) (?<HttpVersion>.*)" + AbnfRegExp.CRLF + "?$"
	);
	/**
	 * A pattern that groups the components of a response. (no validation)
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Pattern RESPONSE = Pattern.compile(
			"^(?<StatusLine>[^\r\n]+)" + AbnfRegExp.CRLF +
			"(?<Headers>(?:[^\r\n]+" + AbnfRegExp.CRLF + ")*)" +
			"(?:" + AbnfRegExp.CRLF + "(?<Body>.*))?$"
	);
	/**
	 * A pattern that groups the components of a response status-line. (no validation)
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Pattern STATUS_LINE = Pattern.compile(
			"^(?<HttpVersion>.*) (?<StatusCode>.*) (?<ReasonPhrase>.*)" +
			AbnfRegExp.CRLF + "?$"
	);

	/**
	 * Utility classes shall not have instances.
	 *
	 * @throws AssertionError when called.
	 * @since 0.0.1 ~2021.03.21
	 */
	private HttpParse() {
		throw new AssertionError("No instance for you!");
	}
}
