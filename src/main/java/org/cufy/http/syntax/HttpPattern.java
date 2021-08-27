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
 * Compiled patterns of the class {@link HttpRegExp}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.21
 */
public final class HttpPattern {
	/**
	 * A compiled pattern of the regex {@link HttpRegExp#FIELD_NAME}.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	public static final Pattern FIELD_NAME = Pattern.compile(HttpRegExp.FIELD_NAME);
	/**
	 * A compiled pattern of the regex {@link HttpRegExp#FIELD_VALUE}.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	public static final Pattern FIELD_VALUE = Pattern.compile(HttpRegExp.FIELD_VALUE);
	/**
	 * A compiled pattern of the regex {@link HttpRegExp#HEADER}.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	public static final Pattern HEADER = Pattern.compile(HttpRegExp.HEADER);
	/**
	 * A compiled pattern of the regex {@link HttpRegExp#HEADERS}.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	public static final Pattern HEADERS = Pattern.compile(HttpRegExp.HEADERS);
	/**
	 * A compiled pattern of the regex {@link HttpRegExp#HTTP_VERSION}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern HTTP_VERSION = Pattern.compile(HttpRegExp.HTTP_VERSION);
	/**
	 * A compiled pattern of the regex {@link HttpRegExp#METHOD}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern METHOD = Pattern.compile(HttpRegExp.METHOD);
	/**
	 * A compiled pattern of the regex {@link HttpRegExp#REASON_PHRASE}.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Pattern REASON_PHRASE = Pattern.compile(HttpRegExp.REASON_PHRASE);
	/**
	 * A compiled pattern of the regex {@link HttpRegExp#REQUEST}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern REQUEST = Pattern.compile(HttpRegExp.REQUEST);
	/**
	 * A compiled pattern of the regex {@link HttpRegExp#REQUEST_LINE}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern REQUEST_LINE = Pattern.compile(HttpRegExp.REQUEST_LINE);
	/**
	 * A compiled pattern of the regex {@link HttpRegExp#RESPONSE}.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Pattern RESPONSE = Pattern.compile(HttpRegExp.RESPONSE);
	/**
	 * A compiled pattern of the regex {@link HttpRegExp#STATUS_CODE}.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Pattern STATUS_CODE = Pattern.compile(HttpRegExp.STATUS_CODE);
	/**
	 * A compiled pattern of the regex {@link HttpRegExp#STATUS_LINE}.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Pattern STATUS_LINE = Pattern.compile(HttpRegExp.STATUS_LINE);

	/**
	 * Utility classes shall have no instances.
	 *
	 * @throws AssertionError when called.
	 * @since 0.0.1 ~2021.03.21
	 */
	private HttpPattern() {
		throw new AssertionError("No instance for you!");
	}
}
