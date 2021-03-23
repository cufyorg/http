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
 * Compiled patterns of the RegExp-s in {@link URIRegExp}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.21
 */
public final class URIPattern {
	/**
	 * A compiled pattern of the regex {@link URIRegExp#ATTR_NAME}.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	public static final Pattern ATTR_NAME = Pattern.compile(URIRegExp.ATTR_NAME);
	/**
	 * A compiled pattern of the regex {@link URIRegExp#ATTR_VALUE}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern ATTR_VALUE = Pattern.compile(URIRegExp.ATTR_VALUE);
	/**
	 * A compiled pattern of the regex {@link URIRegExp#AUTHORITY}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern AUTHORITY = Pattern.compile(URIRegExp.AUTHORITY);
	/**
	 * A compiled pattern of the regex {@link URIRegExp#FRAGMENT}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern FRAGMENT = Pattern.compile(URIRegExp.FRAGMENT);
	/**
	 * A compiled pattern of the regex {@link URIRegExp#HOST}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern HOST = Pattern.compile(URIRegExp.HOST);
	/**
	 * A compiled pattern of the regex {@link URIRegExp#PATH}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern PATH = Pattern.compile(URIRegExp.PATH);
	/**
	 * A compiled pattern of the regex {@link URIRegExp#PORT}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern PORT = Pattern.compile(URIRegExp.PORT);
	/**
	 * A compiled pattern of the regex {@link URIRegExp#QUERY}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern QUERY = Pattern.compile(URIRegExp.QUERY);
	/**
	 * A compiled pattern of the regex {@link URIRegExp#SCHEME}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern SCHEME = Pattern.compile(URIRegExp.SCHEME);
	/**
	 * A compiled pattern of the regex {@link URIRegExp#URI}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern URI = Pattern.compile(URIRegExp.URI);
	/**
	 * A compiled pattern of the regex {@link URIRegExp#URI_REFERENCE}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern URI_REFERENCE = Pattern.compile(URIRegExp.URI_REFERENCE);
	/**
	 * A compiled pattern of the regex {@link URIRegExp#USERINFO}.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Pattern USERINFO = Pattern.compile(URIRegExp.USERINFO);
	/**
	 * A compiled pattern of the regex {@link URIRegExp#USERINFO_NC}.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	public static final Pattern USERINFO_NC = Pattern.compile(URIRegExp.USERINFO_NC);

	/**
	 * Utility classes shall not have instances.
	 *
	 * @throws AssertionError when called.
	 * @since 0.0.1 ~2021.03.21
	 */
	private URIPattern() {
		throw new AssertionError("No instance for you!");
	}
}
