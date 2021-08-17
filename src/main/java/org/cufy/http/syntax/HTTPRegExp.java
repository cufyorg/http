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
 * Regular expressions for request-line components. (except uri)
 *
 * @author LSafer
 * @version 0.0.1
 * @see <a href="https://www.rfc-editor.org/rfc/rfc2616">RFC2616</a>
 * @since 0.0.1 ~2021.03.21
 */
public final class HTTPRegExp {
	/**
	 * A custom regex matching the body of a request.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//4.3 regex x-body = *(.)
	public static final String BODY = ".*";
	/**
	 * A regex matching the http-version component of a request-line.
	 *
	 * @see <a href="https://www.tutorialspoint.com/http/http_quick_guide.htm">
	 * 		HTTP Quick Guide
	 * 		</a>
	 * @since 0.0.1 ~2021.03.21
	 */
	@RegExp
	//3.1 regex HTTP-Version = "HTTP" "/" 1*DIGIT "." 1*DIGIT
	public static final String HTTP_VERSION = "(?:(?i)HTTP\\/[0-9]+(?:\\.[0-9]+)?)";
	/**
	 * HTTP/1.1 header field values can be folded onto multiple lines if the continuation
	 * line begins with a space or horizontal tab. All linear white space, including
	 * folding, has the same semantics as SP. A recipient MAY replace any linear white
	 * space with a single SP before interpreting the field value or forwarding the
	 * message downstream.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//2.2 regex LWS = [CRLF] 1*( SP | HT )
	public static final String LWS =
			ABNFRegExp.CRLF + "?[" + ABNFRegExp.SP + ABNFRegExp.HTAB + "]+";
	/**
	 * The regex matching qdtext. (any TEXT except <">)
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//2.2 regex qdtext = <any TEXT except <">>
	public static final String QDTEXT = "(?:[\\x00-\\xff&&[^\\x00-\\x1f\\x7f\"]]|(?:(?:\\x0d|(?:\\x0d?\\x0a))?[\\x20\\x09])+)";
	/**
	 * The regex matching quoted-pair.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//2.2 regex quoted-pair = "\" CHAR
	public static final String QUOTED_PAIR = "(?:\\" + ABNFRegExp.CHAR + ")";
	/**
	 * The regex matching quoted strings.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//2.2 regex quoted-string = ( <"> *(qdtext | quoted-pair ) <"> )
	public static final String QUOTED_STRING =
			"(?:\"(?:" + HTTPRegExp.QDTEXT + "|" + HTTPRegExp.QUOTED_PAIR + ")*\")";
	/**
	 * The response reason phrase.
	 *
	 * @see <a href="https://www.rfc-editor.org/rfc/rfc2616#section-6.1.1">RFC2616-6.1.1</a>
	 * @since 0.0.1 ~2021.03.23
	 */
	@RegExp
	//6.1.1 regex Reason-Phrase = *<TEXT, excluding CR, LF>
	public static final String REASON_PHRASE = "(?:(?:[\\x00-\\xff&&[^\\x00-\\x1f\\x7f]])*)";
	/**
	 * The separators characters class.
	 *
	 * @see <a href="https://www.rfc-editor.org/rfc/rfc2616#section-2.2">RFC2616 2.2</a>
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//2.2 class separators = "(" | ")" | "<" | ">" | "@" | "," | ";" | ":" | "\" | <"> | "/" | "[" | "]" | "?" | "=" | "{" | "}" | SP | HT
	public static final String SEPARATORS = "[\\(\\)\\<\\>\\@\\,\\;\\:\\\"\\/\\[\\]\\?\\=\\{\\}\\ \\\t]";
	/**
	 * A custom regex matching response status code.
	 * <pre>
	 *     x-status-code = 3DIGIT
	 * </pre>
	 * <pre>
	 *     STATUS_CODE = {@link ABNFRegExp#DIGIT DIGIT}{3}
	 * </pre>
	 *
	 * @see <a href="https://www.rfc-editor.org/rfc/rfc2616#section-6.1.1">RFC2616-6.1.1</a>
	 * @since 0.0.1 ~2021.03.23
	 */
	@RegExp
	//6.1.1 regex x-status-code = 3DIGIT
	public static final String STATUS_CODE = "(?:" + ABNFRegExp.DIGIT + "{3})";
	/**
	 * The response status-line.
	 *
	 * @see <a href="https://www.rfc-editor.org/rfc/rfc2616#section-6.1">RFC2616-6.1</a>
	 * @since 0.0.1 ~2021.03.23
	 */
	@RegExp
	//6.1 regex Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase CRLF
	public static final String STATUS_LINE =
			"(?:" +
			HTTPRegExp.HTTP_VERSION + " " +
			HTTPRegExp.STATUS_CODE + " " +
			HTTPRegExp.REASON_PHRASE +
			ABNFRegExp.CRLF + "?" +
			")";
	/**
	 * The regex matching TEXT. (any OCTET except CTLs, but including LWS)
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//2.2 regex TEXT = <any OCTET except CTLs, but including LWS>
	public static final String TEXT = "(?:[\\x00-\\xff&&[^\\x00-\\x1f\\x7f]]|(?:(?:\\x0d|(?:\\x0d?\\x0a))?[\\x20\\x09])+)";
	/**
	 * A valid word in most of HTTP fields.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//2.2 regex token = 1*( <any CHAR(%x01-7f) except CTL(%x00-1f %x7f) and separators> )
	public static final String TOKEN = "(?:[\\x01-\\x7f&&[^\\x00-\\x1f\\x7f\\(\\)\\<\\>\\@\\,\\;\\:\\\"\\/\\[\\]\\?\\=\\{\\}\\ \\\t]]+)";
	/**
	 * A regex matching request-method.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@RegExp
	//5.1.1 regex Method = "OPTIONS" / "GET" / "HEAD" / "POST" / "PUT" / "DELETE" / "TRACE" / "CONNECT" / extension-method
	public static final String METHOD = HTTPRegExp.TOKEN;
	/**
	 * A regex matching request-line.
	 *
	 * @see <a href="https://www.rfc-editor.org/rfc/rfc2616#section-5.1">RFC2616 5.1</a>
	 * @since 0.0.1 ~2021.03.21
	 */
	@RegExp
	//5.1 regex Request-Line = Method SP Request-URI SP HTTP-Version CRLF
	public static final String REQUEST_LINE =
			"(?:" +
			HTTPRegExp.METHOD + " " +
			URIRegExp.URI_REFERENCE + " " +
			HTTPRegExp.HTTP_VERSION +
			ABNFRegExp.CRLF + "?" +
			")";
	/**
	 * A regex matching header field name.
	 * <pre>
	 *     field-name = token
	 * </pre>
	 * <pre>
	 *     FIELD_NAME = {@link #TOKEN}
	 * </pre>
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//4.2 regex field-name = token
	public static final String FIELD_NAME = HTTPRegExp.TOKEN;
	/**
	 * A regex matching the field content. (the OCTETs making up the field-value and
	 * consisting of either *TEXT or combinations of token, separators, and
	 * quoted-string)
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//4.2 regex field-content = <the OCTETs making up the field-value and consisting of either *TEXT or combinations of token, separators, and quoted-string>
	public static final String FIELD_CONTENT =
			"(?:" + HTTPRegExp.TEXT + "*|(?:" + HTTPRegExp.TOKEN + HTTPRegExp.SEPARATORS +
			HTTPRegExp.QUOTED_STRING + ")*)";
	/**
	 * A regex matching the header field value.
	 * <pre>
	 *     field-value = *( field-content | LWS )
	 * </pre>
	 * <pre>
	 *     FIELD_VALUE = ({@link #FIELD_CONTENT}|{@link #LWS})*
	 * </pre>
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//4.2 regex field-value = *( field-content | LWS )
	public static final String FIELD_VALUE =
			"(?:(?:" + HTTPRegExp.FIELD_CONTENT + "|" + HTTPRegExp.LWS + ")*)";
	/**
	 * A custom regex matching a header.
	 * <pre>
	 *     x-header = field-name ":" [ field-value ]
	 * </pre>
	 * <pre>
	 *     HEADER = {@link #FIELD_NAME}:{@link #FIELD_VALUE}?
	 * </pre>
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//4.2 regex x-header = field-name ":" [ field-value ]
	public static final String HEADER =
			"(?:" + HTTPRegExp.FIELD_NAME + ":" + HTTPRegExp.FIELD_VALUE + "?)";
	/**
	 * A custom regex matching headers.
	 * <pre>
	 *     x-headers = *(x-header CRLF)
	 * </pre>
	 * <pre>
	 *     HEADERS = ({@link #HEADER} {@link ABNFRegExp#CRLF CRLF})*
	 * </pre>
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//5 regex x-headers = *(x-header CRLF)
	public static final String HEADERS =
			"(?:(?:" + HTTPRegExp.HEADER + ABNFRegExp.CRLF + ")*)";
	/**
	 * A regex matching request.
	 *
	 * @see <a href="https://www.rfc-editor.org/rfc/rfc2616#section-5">RFC2616-5</a>
	 * @since 0.0.1 ~2021.03.22
	 */
	@RegExp
	//5 regex x-request = Request-Line HEADERS [CRLF BODY]
	public static final String REQUEST =
			"(?:" +
			HTTPRegExp.REQUEST_LINE +
			"(?:(?<!" + ABNFRegExp.CRLF + ")" + ABNFRegExp.CRLF + ")" +
			HTTPRegExp.HEADERS +
			"(?:" + ABNFRegExp.CRLF + HTTPRegExp.BODY + ")?" +
			")";
	/**
	 * A regex matching response.
	 *
	 * @see <a href="https://www.rfc-editor.org/rfc/rfc2616#section-6">RFC2616-6</a>
	 * @since 0.0.1 ~2021.03.23
	 */
	@RegExp
	//6 regex x-response = Status-Line HEADERS [CRLF BODY]
	public static final String RESPONSE =
			"(?:" +
			HTTPRegExp.STATUS_LINE +
			"(?:(?<!" + ABNFRegExp.CRLF + ")" + ABNFRegExp.CRLF + ")" +
			HTTPRegExp.HEADERS +
			"(?:" + ABNFRegExp.CRLF + HTTPRegExp.BODY + ")?" +
			")";

	/**
	 * Utility classes shall have no instances.
	 *
	 * @throws AssertionError when called.
	 * @since 0.0.1 ~2021.03.21
	 */
	private HTTPRegExp() {
		throw new AssertionError("No instance for you!");
	}
}
