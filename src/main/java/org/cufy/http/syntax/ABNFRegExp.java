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

import org.intellij.lang.annotations.RegExp;

/**
 * The core expressions.
 *
 * @author LSafer
 * @version 0.0.1
 * @see <a href="https://tools.ietf.org/html/rfc2234">RFC2234</a>
 * @since 0.0.1 ~2021.03.22
 */
public final class ABNFRegExp {
	/**
	 * The english alphabet class.
	 *
	 * @see <a href="https://tools.ietf.org/html/rfc2234#section-6.1">RFC2234 6.1</a>
	 * @since 0.0.1 ~2021.03.21
	 */
	@RegExp
	//6.1 class ALPHA = %x41-5A / %x61-7A
	public static final String ALPHA = "[\\x41-\\x5a\\x61-\\x7a]";
	/**
	 * The 10 character class. (binary character class)
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//6.1 class BIT = "0" / "1"
	public static final String BIT = "[01]";
	/**
	 * The US-ASCII characters class.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//6.1 class CHAR = %x01-7F
	public static final String CHAR = "[\\x01-\\x7f]";
	/**
	 * The carriage return.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//6.1 class CR = %x0D
	public static final String CR = "\\x0d";
	/**
	 * The control characters class.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//6.1 class CTL = %x00-1F / %x7F
	public static final String CTL = "[\\x00-\\x1f\\x7f]";
	/**
	 * The decimal numbers class.
	 *
	 * @see <a href="https://tools.ietf.org/html/rfc2234#section-6.1">RFC2234 6.1</a>
	 * @since 0.0.1 ~2021.03.21
	 */
	@RegExp
	//6.1 class DIGIT = %x30-39
	public static final String DIGIT = "[\\x30-\\x39]";
	/**
	 * The double quote.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//6.1 regex DQUOTE = %x22
	public static final String DQUOTE = "\\x22";
	/**
	 * The characters class allowed in hexdig.
	 *
	 * @see <a href="https://tools.ietf.org/html/rfc2234#section-6.1">RFC2234 6.1</a>
	 * @since 0.0.1 ~2021.03.21
	 */
	@RegExp
	//6.1 class HEXDIG = DIGIT / "A" / "B" / "C" / "D" / "E" / "F"
	public static final String HEXDIG = "(?:" + ABNFRegExp.DIGIT + "|[A-Fa-f])";
	/**
	 * The horizontal tab.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//6.1 class HTAB = %x09
	public static final String HTAB = "\\x09";
	/**
	 * The linefeed.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//6.1 class LF = %x0A
	public static final String LF = "\\x0a";
	/**
	 * The internet standard newline.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//6.1 regex CRLF = CR LF
	public static final String CRLF =
			"(?:" + ABNFRegExp.CR + "|(?:" + ABNFRegExp.CR + "?" + ABNFRegExp.LF + "))";
	/**
	 * The characters class that takes up 8bits of data.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//6.1 class OCTET = %x00-FF
	public static final String OCTET = "[\\x00-\\xff]";
	/**
	 * The space.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	//6.1 class SP = %x20
	public static final String SP = "\\x20";
	/**
	 * The visible characters class.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//6.1 class VCHAR = %x21-7E
	public static final String VCHAR = "[\\x21-\\x7e]";
	/**
	 * The whitespace class.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//6.1 class WSP = SP / HTAB
	public static final String WSP = "[" + ABNFRegExp.SP + ABNFRegExp.HTAB + "]";
	/**
	 * A regex matching a linear whitespace (past newline).
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//6.1 regex LWSP = *(WSP / CRLF WSP)
	public static final String LWSP =
			"(?:(?:" + ABNFRegExp.WSP + "|" + ABNFRegExp.CRLF + ABNFRegExp.WSP + ")*)";

	/**
	 * Utility classes shall have no instances.
	 *
	 * @throws AssertionError when called.
	 * @since 0.0.1 ~2021.03.21
	 */
	private ABNFRegExp() {
		throw new AssertionError("No instance for you!");
	}
}
