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
 * Compiled patterns of the class {@link HTTPRegExp}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.21
 */
public final class HTTPPattern {
	/**
	 * A compiled pattern of the regex {@link HTTPRegExp#FIELD_NAME}.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	public static final Pattern FIELD_NAME = Pattern.compile(HTTPRegExp.FIELD_NAME);
	/**
	 * A compiled pattern of the regex {@link HTTPRegExp#FIELD_VALUE}.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	public static final Pattern FIELD_VALUE = Pattern.compile(HTTPRegExp.FIELD_VALUE);
	/**
	 * A compiled pattern of the regex {@link HTTPRegExp#HEADER}.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	public static final Pattern HEADER = Pattern.compile(HTTPRegExp.HEADER);
	/**
	 * A compiled pattern of the regex {@link HTTPRegExp#HEADERS}.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	public static final Pattern HEADERS = Pattern.compile(HTTPRegExp.HEADERS);
	/**
	 * A compiled pattern of the regex {@link HTTPRegExp#HTTP_VERSION}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern HTTP_VERSION = Pattern.compile(HTTPRegExp.HTTP_VERSION);
	/**
	 * A compiled pattern of the regex {@link HTTPRegExp#METHOD}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern METHOD = Pattern.compile(HTTPRegExp.METHOD);
	/**
	 * A compiled pattern of the regex {@link HTTPRegExp#REASON_PHRASE}.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Pattern REASON_PHRASE = Pattern.compile(HTTPRegExp.REASON_PHRASE);
	/**
	 * A compiled pattern of the regex {@link HTTPRegExp#REQUEST}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern REQUEST = Pattern.compile(HTTPRegExp.REQUEST);
	/**
	 * A compiled pattern of the regex {@link HTTPRegExp#REQUEST_LINE}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern REQUEST_LINE = Pattern.compile(HTTPRegExp.REQUEST_LINE);
	/**
	 * A compiled pattern of the regex {@link HTTPRegExp#RESPONSE}.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Pattern RESPONSE = Pattern.compile(HTTPRegExp.RESPONSE);
	/**
	 * A compiled pattern of the regex {@link HTTPRegExp#STATUS_CODE}.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Pattern STATUS_CODE = Pattern.compile(HTTPRegExp.STATUS_CODE);
	/**
	 * A compiled pattern of the regex {@link HTTPRegExp#STATUS_LINE}.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Pattern STATUS_LINE = Pattern.compile(HTTPRegExp.STATUS_LINE);

	/**
	 * Utility classes shall have no instances.
	 *
	 * @throws AssertionError when called.
	 * @since 0.0.1 ~2021.03.21
	 */
	private HTTPPattern() {
		throw new AssertionError("No instance for you!");
	}
}
