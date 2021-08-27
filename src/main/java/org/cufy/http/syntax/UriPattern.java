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
 * Compiled patterns of the RegExp-s in {@link UriRegExp}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.21
 */
public final class UriPattern {
	/**
	 * A compiled pattern of the regex {@link UriRegExp#ATTR_NAME}.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	public static final Pattern ATTR_NAME = Pattern.compile(UriRegExp.ATTR_NAME);
	/**
	 * A compiled pattern of the regex {@link UriRegExp#ATTR_VALUE}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern ATTR_VALUE = Pattern.compile(UriRegExp.ATTR_VALUE);
	/**
	 * A compiled pattern of the regex {@link UriRegExp#AUTHORITY}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern AUTHORITY = Pattern.compile(UriRegExp.AUTHORITY);
	/**
	 * A compiled pattern of the regex {@link UriRegExp#FRAGMENT}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern FRAGMENT = Pattern.compile(UriRegExp.FRAGMENT);
	/**
	 * A compiled pattern of the regex {@link UriRegExp#HOST}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern HOST = Pattern.compile(UriRegExp.HOST);
	/**
	 * A compiled pattern of the regex {@link UriRegExp#PATH}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern PATH = Pattern.compile(UriRegExp.PATH);
	/**
	 * A compiled pattern of the regex {@link UriRegExp#PORT}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern PORT = Pattern.compile(UriRegExp.PORT);
	/**
	 * A compiled pattern of the regex {@link UriRegExp#QUERY}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern QUERY = Pattern.compile(UriRegExp.QUERY);
	/**
	 * A compiled pattern of the regex {@link UriRegExp#SCHEME}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern SCHEME = Pattern.compile(UriRegExp.SCHEME);
	/**
	 * A compiled pattern of the regex {@link UriRegExp#URI}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern URI = Pattern.compile(UriRegExp.URI);
	/**
	 * A compiled pattern of the regex {@link UriRegExp#URI_REFERENCE}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern URI_REFERENCE = Pattern.compile(UriRegExp.URI_REFERENCE);
	/**
	 * A compiled pattern of the regex {@link UriRegExp#USERINFO}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern USERINFO = Pattern.compile(UriRegExp.USERINFO);
	/**
	 * A compiled pattern of the regex {@link UriRegExp#USERINFO_NC}.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	public static final Pattern USERINFO_NC = Pattern.compile(UriRegExp.USERINFO_NC);

	/**
	 * Utility classes shall not have instances.
	 *
	 * @throws AssertionError when called.
	 * @since 0.0.1 ~2021.03.21
	 */
	private UriPattern() {
		throw new AssertionError("No instance for you!");
	}
}
