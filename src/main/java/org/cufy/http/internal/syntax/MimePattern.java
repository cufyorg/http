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

import java.util.regex.Pattern;

/**
 * Compiled patterns from the class {@link MimeRegExp}.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2022.12.26
 */
public final class MimePattern {
	/**
	 * A compiled pattern of the regex {@link MimeRegExp#MEDIA_TYPE}.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final Pattern MEDIA_TYPE = Pattern.compile(MimeRegExp.MEDIA_TYPE);
	/**
	 * A compiled pattern of the regex {@link MimeRegExp#PARAMETER}.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final Pattern PARAMETER = Pattern.compile(MimeRegExp.PARAMETER);
	/**
	 * A compiled pattern of the regex {@link MimeRegExp#PARAMETERS}.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final Pattern PARAMETERS = Pattern.compile(MimeRegExp.PARAMETERS);
	/**
	 * A compiled pattern of the regex {@link MimeRegExp#PARAMETER_NAME}.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final Pattern PARAMETER_NAME = Pattern.compile(MimeRegExp.PARAMETER_NAME);
	/**
	 * A compiled pattern of the regex {@link MimeRegExp#PARAMETER_VALUE}.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final Pattern PARAMETER_VALUE = Pattern.compile(MimeRegExp.PARAMETER_VALUE);
	/**
	 * A compiled pattern of the regex {@link MimeRegExp#TYPE}.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final Pattern TYPE = Pattern.compile(MimeRegExp.TYPE);

	/**
	 * Utility classes shall not have instances.
	 *
	 * @throws AssertionError when called.
	 * @since 0.0.1 ~2021.03.21
	 */
	private MimePattern() {
		throw new AssertionError("No instance for you!");
	}
}
