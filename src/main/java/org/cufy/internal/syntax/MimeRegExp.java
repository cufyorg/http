/*
 *	Copyright 2021 Cufy and ProgSpaceSA
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
package org.cufy.internal.syntax;

import org.intellij.lang.annotations.RegExp;

/**
 * Regular expressions for mime components.
 *
 * @author LSafer
 * @version 0.3.0
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc7231">RFC7231</a>
 * @since 0.3.0 ~2022.12.26
 */
public final class MimeRegExp {
	/**
	 * Optional whitespace.
	 *
	 * @see <a href="https://datatracker.ietf.org/doc/html/rfc7230#section-3.2.3">RFC7230-3.2.3</a>
	 * @since 0.3.0 ~2022.12.26
	 */
	@RegExp
	//3.2.3 regex OWS = *( SP / HTAB )
	public static final String OWS = "(?:[" + AbnfRegExp.SP + AbnfRegExp.HTAB + "]*)";
	/**
	 * A regex matching the parameter in a media type.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	@RegExp
	//3.1.1.1 regex parameter = token "=" ( token / quoted-string )
	public static final String PARAMETER =
			"(?:" + HttpRegExp.TOKEN + "=" +
			"(?:" + HttpRegExp.TOKEN + "|" + HttpRegExp.QUOTED_STRING + ")" +
			")";
	/**
	 * A custom regex matching the parameters in a media type.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	@RegExp
	public static final String PARAMETERS =
			"(?:(?:" +
			MimeRegExp.OWS + ";" + MimeRegExp.OWS + MimeRegExp.PARAMETER +
			")*)";
	/**
	 * A custom regex matching the parameter's name in a mime.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	@RegExp
	public static final String PARAMETER_NAME =
			HttpRegExp.TOKEN;
	/**
	 * A custom regex matching the parameter's value in a mime.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	@RegExp
	public static final String PARAMETER_VALUE =
			"(?:" + HttpRegExp.TOKEN + "|" + HttpRegExp.QUOTED_STRING + ")";
	/**
	 * A regex matching the subtype in a media type.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	@RegExp
	//3.1.1.1 regex subtype = token
	public static final String SUB_TYPE = HttpRegExp.TOKEN;
	/**
	 * A regex matching the type in a media type.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	@RegExp
	//3.1.1.1 regex type = token
	public static final String TYPE = HttpRegExp.TOKEN;
	/**
	 * A regex matching media-types.
	 *
	 * @see <a href="https://datatracker.ietf.org/doc/html/rfc7231#section-3.1.1.1">RFC7231-3.1.1.1</a>
	 * @since 0.3.0 ~2022.12.26
	 */
	@RegExp
	//3.1.1.1 regex media-type = type "/" subtype *( OWS ";" OWS parameter )
	public static final String MEDIA_TYPE =
			"(?:" +
			MimeRegExp.TYPE + "/" + MimeRegExp.SUB_TYPE +
			"(?:" + MimeRegExp.OWS + ";" + MimeRegExp.OWS + MimeRegExp.PARAMETER + ")*" +
			")";

	/**
	 * Utility classes shall not have instances.
	 *
	 * @throws AssertionError when called.
	 * @since 0.0.1 ~2021.03.21
	 */
	private MimeRegExp() {
		throw new AssertionError("No instance for you!");
	}
}
