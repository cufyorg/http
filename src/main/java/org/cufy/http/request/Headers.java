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
package org.cufy.http.request;

import org.cufy.http.syntax.HTTPRegExp;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.*;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * <b>Mappings</b> (No Encode)
 * <br>
 * The "Headers" part of a request.
 *
 * @author LSafer
 * @version 0.0.1
 * @see <a href="https://en.wikipedia.org/wiki/List_of_HTTP_header_fields">
 * 		List of HTTP header fields
 * 		</a>
 * @since 0.0.1 ~2021.03.21
 */
public interface Headers extends Cloneable, Serializable {
	/**
	 * <b>Request</b>
	 * <br>
	 * Media type(s) that is/are acceptable for the response. See Content negotiation.
	 *
	 * @see <a href="https://tools.ietf.org/html/rfc2616">RFC2616</a>
	 * @see <a href="https://tools.ietf.org/html/rfc7231">RFC7231</a>
	 * @since 0.0.1 ~2021.03.23
	 */
	String ACCEPT = "Accept";
	/**
	 * <b>Response</b>
	 * <br>
	 * Requests HTTP Client Hints.
	 *
	 * @see <a href="https://tools.ietf.org/html/rfc8942">RFC8942</a>
	 * @since 0.0.1 ~2021.03.23
	 */
	String ACCEPT_CH = "Accept-CH";
	/**
	 * <b>Request</b>
	 * <br>
	 * Character sets that are acceptable.
	 *
	 * @see <a href="https://tools.ietf.org/html/rfc2616">RFC2616</a>
	 * @since 0.0.1 ~2021.03.23
	 */
	String ACCEPT_CHARSET = "Accept-Charset";
	/**
	 * <b>Request</b>
	 * <br>
	 * Acceptable version in time.
	 *
	 * @see <a href="https://tools.ietf.org/html/rfc7089">RFC7089</a>
	 * @since 0.0.1 ~2021.03.23
	 */
	String ACCEPT_DATETIME = "Accept-Datetime";
	/**
	 * <b>Request</b>
	 * <br>
	 * List of acceptable encodings.
	 *
	 * @see <a href="https://tools.ietf.org/html/rfc2616">RFC2616</a>
	 * @see <a href="https://tools.ietf.org/html/rfc7231">RFC7231</a>
	 * @since 0.0.1 ~2021.03.23
	 */
	String ACCEPT_ENCODING = "Accept-Encoding";
	/**
	 * <b>Request</b>
	 * <br>
	 * List of acceptable human languages for response.
	 *
	 * @see <a href="https://tools.ietf.org/html/rfc2616">RFC2616</a>
	 * @since 0.0.1 ~2021.03.23
	 */
	String ACCEPT_LANGUAGE = "Accept-Language";
	/**
	 * <b>Response</b>
	 * <br>
	 * Specifies which patch document formats this server supports.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String ACCEPT_PATCH = "Accept-Patch";
	/**
	 * <b>Response</b>
	 * <br>
	 * What partial content range types this server supports via byte serving.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String ACCEPT_RANGES = "Accept-Ranges";
	/**
	 * <b>Response</b>
	 * <br>
	 * Specifying which web sites can participate in cross-origin resource sharing.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
	/**
	 * <b>Response</b>
	 * <br>
	 * Specifying which web sites can participate in cross-origin resource sharing.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
	/**
	 * <b>Response</b>
	 * <br>
	 * Specifying which web sites can participate in cross-origin resource sharing.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
	/**
	 * <b>Response</b>
	 * <br>
	 * Specifying which web sites can participate in cross-origin resource sharing.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	/**
	 * <b>Response</b>
	 * <br>
	 * Specifying which web sites can participate in cross-origin resource sharing.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
	/**
	 * <b>Response</b>
	 * <br>
	 * Specifying which web sites can participate in cross-origin resource sharing.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
	/**
	 * <b>Request</b>
	 * <br>
	 * Initiates a request for cross-origin resource sharing with Origin.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
	/**
	 * <b>Request</b>
	 * <br>
	 * Initiates a request for cross-origin resource sharing with Origin.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
	/**
	 * <b>Response</b>
	 * <br>
	 * The age the object has been in a proxy cache in seconds.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String AGE = "Age";
	/**
	 * <b>Response</b>
	 * <br>
	 * Valid methods for a specified resource. To be used for a 405 Method not allowed.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String ALLOW = "Allow";
	/**
	 * <b>Response</b>
	 * <br>
	 * A server uses "Alt-Svc" header (meaning Alternative Services) to indicate that its
	 * resources can also be accessed at a different network location (host or port) or
	 * using a different protocol
	 * <br>
	 * When using HTTP/2, servers should instead send an ALTSVC frame.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String ALT_SVC = "Alt-Svc";
	/**
	 * <b>Request</b>
	 * <br>
	 * Authentication credentials for HTTP authentication.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String AUTHORIZATION = "Authorization";
	/**
	 * <b>Request</b>
	 * <br>
	 * Acceptable instance-manipulations for the request.
	 *
	 * @see <a href="https://tools.ietf.org/html/rfc3229">RFC3229</a>
	 * @since 0.0.1 ~2021.03.23
	 */
	String A_IM = "A-IM";
	/**
	 * <b>Request</b>
	 * <br>
	 * Used to specify directives that must be obeyed by all caching mechanisms along the
	 * request-response chain.
	 * <br>
	 * <b>Response</b>
	 * <br>
	 * Tells all caching mechanisms from server to client whether they may cache this
	 * object. It is measured in seconds
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String CACHE_CONTROL = "Cache-Control";
	/**
	 * <b>Request</b>
	 * <br>
	 * Control options for the current connection and list of hop-by-hop request fields.
	 * <br>
	 * Must not be used with HTTP/2.
	 * <br>
	 * <b>Response</b>
	 * <br>
	 * Control options for the current connection and list of hop-by-hop response fields.
	 * <br>
	 * Must not be used with HTTP/2.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String CONNECTION = "Connection";
	/**
	 * <b>Response</b>
	 * <br>
	 * An opportunity to raise a "File Download" dialogue box for a known MIME type with
	 * binary format or suggest a filename for dynamic content. Quotes are necessary with
	 * special characters.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String CONTENT_DISPOSITION = "Content-Disposition";
	/**
	 * <b>Request</b>
	 * <br>
	 * The type of encoding used on the data.
	 * <br>
	 * <b>Request</b>
	 * <br>
	 * The type of encoding used on the data.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String CONTENT_ENCODING = "Content-Encoding";
	/**
	 * <b>Response</b>
	 * <br>
	 * The natural language or languages of the intended audience for the enclosed
	 * content.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String CONTENT_LANGUAGE = "Content-Language";
	/**
	 * <b>Request</b>
	 * <br>
	 * The length of the request body in octets (8-bit bytes).
	 * <br>
	 * <b>Response</b>
	 * <br>
	 * The length of the response body in octets (8-bit bytes)
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String CONTENT_LENGTH = "Content-Length";
	/**
	 * <b>Response</b>
	 * <br>
	 * An alternate location for the returned data.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String CONTENT_LOCATION = "Content-Location";
	/**
	 * <b>Request</b>
	 * <br>
	 * A Base64-encoded binary MD5 sum of the content of the request body.
	 * <br>
	 * <b>Request</b>
	 * <br>
	 * A Base64-encoded binary MD5 sum of the content of the response
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String CONTENT_MD5 = "Content-MD5";
	/**
	 * <b>Response</b>
	 * <br>
	 * Where in a full body message this partial message belongs.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String CONTENT_RANGE = "Content-Range";
	/**
	 * <b>Request</b>
	 * <br>
	 * The Media type of the body of the request (used with POST and PUT requests).
	 * <br>
	 * <b>Response</b>
	 * <br>
	 * The MIME type of this content.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String CONTENT_TYPE = "Content-Type";
	/**
	 * <b>Request</b>
	 * <br>
	 * An HTTP cookie previously sent by the server with Set-Cookie.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String COOKIE = "Cookie";
	/**
	 * <b>Request</b>
	 * <br>
	 * The date and time at which the message was originated (in "HTTP-date" format as
	 * defined by RFC 7231 Date/Time Formats).
	 * <br>
	 * <b>Response</b>
	 * <br>
	 * The date and time that the message was sent (in "HTTP-date" format as defined by
	 * RFC 7231).
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String DATE = "Date";
	/**
	 * <b>Response</b>
	 * <br>
	 * Specifies the delta-encoding entity tag of the response.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String DELTA_BASE = "Delta-Base";
	/**
	 * An empty headers constant.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	Headers EMPTY = new RawHeaders();
	/**
	 * <b>Response</b>
	 * <br>
	 * An identifier for a specific version of a resource, often a message digest.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String ETAG = "ETag";
	/**
	 * <b>Request</b>
	 * <br>
	 * Indicates that particular server behaviors are required by the client.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String EXPECT = "Expect";
	/**
	 * <b>Response</b>
	 * <br>
	 * Gives the date/time after which the response is considered stale (in "HTTP-date"
	 * format as defined by RFC 7231).
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String EXPIRES = "Expires";
	/**
	 * <b>Request</b>
	 * <br>
	 * Disclose original information of a client connecting to a web server through an
	 * HTTP proxy.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String FORWARDED = "Forwarded";
	/**
	 * <b>Request</b>
	 * <br>
	 * The email address of the user making the request.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String FROM = "From";
	/**
	 * <b>Request</b>
	 * <br>
	 * The domain name of the server (for virtual hosting), and the TCP port number on
	 * which the server is listening. The port number may be omitted if the port is the
	 * standard port for the service requested. Mandatory since HTTP/1.1 If the request is
	 * generated directly in HTTP/2, it should not be used.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String HOST = "Host";
	/**
	 * <b>Request</b>
	 * <br>
	 * A request that upgrades from HTTP/1.1 to HTTP/2 MUST include exactly one
	 * HTTP2-Setting header field. The HTTP2-Settings header field is a
	 * connection-specific header field that includes parameters that govern the HTTP/2
	 * connection, provided in anticipation of the server accepting the request to
	 * upgrade.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String HTTP2_SETTINGS = "HTTP2-Settings";
	/**
	 * <b>Request</b>
	 * <br>
	 * Only perform the action if the client supplied entity matches the same entity on
	 * the server. This is mainly for methods like PUT to only update a resource if it has
	 * not been modified since the user last updated it.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String IF_MATCH = "If-Match";
	/**
	 * <b>Request</b>
	 * <br>
	 * Allows a 304 Not Modified to be returned if content is unchanged.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String IF_MODIFIED_SINCE = "If-Modified-Since";
	/**
	 * <b>Request</b>
	 * <br>
	 * Allows a 304 Not Modified to be returned if content is unchanged.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String IF_NONE_MATCH = "If-None-Match";
	/**
	 * <b>Request</b>
	 * <br>
	 * Only send the response if the entity has not been modified since a specific time.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
	/**
	 * <b>Response</b>
	 * <br>
	 * Instance-manipulations applied to the response.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String IM = "IM";
	/**
	 * <b>Request</b>
	 * <br>
	 * If the entity is unchanged, send me the part(s) that I am missing; otherwise, send
	 * me the entire new entity.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String If_RANGE = "If-Range";
	/**
	 * <b>Response</b>
	 * <br>
	 * The last modified date for the requested object (in "HTTP-date" format as defined
	 * by RFC 7231).
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String LAST_MODIFIED = "Last-Modified";
	/**
	 * <b>Response</b>
	 * <br>
	 * Used to express a typed relationship with another resource, where the relation type
	 * is defined by RFC 5988.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String LINK = "Link";
	/**
	 * <b>Response</b>
	 * <br>
	 * Used in redirection, or when a new resource has been created.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String LOCATION = "Location";
	/**
	 * <b>Request</b>
	 * <br>
	 * Limit the number of times the message can be forwarded through proxies or
	 * gateways.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String MAX_FORWARDS = "Max-Forwards";
	/**
	 * <b>Request</b>
	 * <br>
	 * Initiates a request for cross-origin resource sharing (asks server for
	 * Access-Control-* response fields).
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String ORIGIN = "Origin";
	/**
	 * <b>Response</b>
	 * <br>
	 * This field is supposed to set P3P policy, in the form of
	 * P3P:CP="your_compact_policy". However, P3P did not take off,[52] most browsers have
	 * never fully implemented it, a lot of websites set this field with fake policy text,
	 * that was enough to fool browsers the existence of P3P policy and grant permissions
	 * for third party cookies.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String P3P = "P3P";
	/**
	 * <b>Request</b>
	 * <br>
	 * Implementation-specific fields that may have various effects anywhere along the
	 * request-response chain.
	 * <br>
	 * <b>Response</b>
	 * <br>
	 * Implementation-specific fields that may have various effects anywhere along the
	 * request-response chain.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String PRAGMA = "Pragma";
	/**
	 * <b>Request</b>
	 * <br>
	 * Allows client to request that certain behaviors be employed by a server while
	 * processing a request.
	 *
	 * @see <a href="https://tools.ietf.org/html/rfc7240">RFC7240</a>
	 * @since 0.0.1 ~2021.03.23
	 */
	String PREFER = "Prefer";
	/**
	 * <b>Response</b>
	 * <br>
	 * Indicates which Prefer tokens were honored by the server and applied to the
	 * processing of the request.
	 *
	 * @see <a href="https://tools.ietf.org/html/rfc7240">RFC7240</a>
	 * @since 0.0.1 ~2021.03.23
	 */
	String PREFERENCE_APPLIED = "Preference-Applied";
	/**
	 * <b>Response</b>
	 * <br>
	 * Request authentication to access the proxy.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String PROXY_AUTHENTICATE = "Proxy-Authenticate";
	/**
	 * <b>Request</b>
	 * <br>
	 * Authorization credentials for connecting to a proxy.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String PROXY_AUTHORIZATION = "Proxy-Authorization";
	/**
	 * <b>Response</b>
	 * <br>
	 * HTTP Public Key Pinning, announces hash of website's authentic TLS certificate.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String PUBLIC_KEY_PINS = "Public-Key-Pins";
	/**
	 * <b>Request</b>
	 * <br>
	 * Request only part of an entity. Bytes are numbered from 0.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String RANGE = "Range";
	/**
	 * <b>Request</b>
	 * <br>
	 * This is the address of the previous web page from which a link to the currently
	 * requested page was followed. (The word "referrer" has been misspelled in the RFC as
	 * well as in most implementations to the point that it has become standard usage and
	 * is considered correct terminology)
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String REFERER = "Referer";
	/**
	 * <b>Response</b>
	 * <br>
	 * If an entity is temporarily unavailable, this instructs the client to try again
	 * later. Value could be a specified period of time (in seconds) or a HTTP-date.
	 *
	 * @see <a href="https://tools.ietf.org/html/rfc2616">RFC2616</a>
	 * @see <a href="https://tools.ietf.org/html/rfc7231">RFC7231</a>
	 * @since 0.0.1 ~2021.03.23
	 */
	String RETRY_AFTER = "Retry-After";
	/**
	 * <b>Response</b>
	 * <br>
	 * A name for the server.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String SERVER = "Server";
	/**
	 * <b>Response</b>
	 * <br>
	 * An HTTP cookie.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String SET_COOKIE = "Set-Cookie";
	/**
	 * <b>Response</b>
	 * <br>
	 * A HSTS Policy informing the HTTP client how long to cache the HTTPS only policy and
	 * whether this applies to subdomains.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";
	/**
	 * <b>Request</b>
	 * <br>
	 * The transfer encodings the user agent is willing to accept: the same values as for
	 * the response header field Transfer-Encoding can be used, plus the "trailers" value
	 * (related to the "chunked" transfer method) to notify the server it expects to
	 * receive additional fields in the trailer after the last, zero-sized, chunk.
	 * <br>
	 * Only trailers is supported in HTTP/2.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String TE = "TE";
	/**
	 * <b>Response</b>
	 * <br>
	 * Tracking Status header, value suggested to be sent in response to a
	 * DNT(do-not-track).
	 * <br>
	 * possible values:
	 * <ul>
	 *     <li>"!" — under construction</li>
	 *     <li>"?" — dynamic</li>
	 *     <li>"G" — gateway to multiple parties</li>
	 *     <li>"N" — not tracking</li>
	 *     <li>"T" — tracking</li>
	 *     <li>"C" — tracking with consent</li>
	 *     <li>"P" — tracking only if consented</li>
	 *     <li>"D" — disregarding DNT</li>
	 *     <li>"U" — updated</li>
	 * </ul>
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String TK = "Tk";
	/**
	 * <b>Request</b>
	 * <br>
	 * The Trailer general field value indicates that the given set of header fields is
	 * present in the trailer of a message encoded with chunked transfer coding.
	 * <br>
	 * <b>Response</b>
	 * <br>
	 * The Trailer general field value indicates that the given set of header fields is
	 * present in the trailer of a message encoded with chunked transfer coding.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String TRAILER = "Trailer";
	/**
	 * <b>Request</b>
	 * <br>
	 * The form of encoding used to safely transfer the entity to the user. Currently
	 * defined methods are: chunked, compress, deflate, gzip, identity.
	 * <br>
	 * Must not be used with HTTP/2.
	 * <br>
	 * <b>Response</b>
	 * <br>
	 * The form of encoding used to safely transfer the entity to the user. Currently
	 * defined methods are: chunked, compress, deflate, gzip, identity.
	 * <br>
	 * Must not be used with HTTP/2.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String TRANSFER_ENCODING = "Transfer-Encoding";
	/**
	 * <b>Request</b>
	 * <br>
	 * Ask the server to upgrade to another protocol.
	 * <br>
	 * Must not be used in HTTP/2.
	 * <br>
	 * <b>Request</b>
	 * <br>
	 * Ask the client to upgrade to another protocol.
	 * <br>
	 * Must not be used in HTTP/2
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String UPGRADE = "Upgrade";
	/**
	 * <b>Request</b>
	 * <br>
	 * The user agent string of the user agent.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String USER_AGENT = "User-Agent";
	/**
	 * <b>Response</b>
	 * <br>
	 * Tells downstream proxies how to match future request headers to decide whether the
	 * cached response can be used rather than requesting a fresh one from the origin
	 * server.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String VARY = "Vary";
	/**
	 * <b>Request</b>
	 * <br>
	 * Informs the server of proxies through which the request was sent.
	 * <br>
	 * <b>Response</b>
	 * <br>
	 * Informs the client of proxies through which the response was sent.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String VIA = "Via";
	/**
	 * <b>Request</b>
	 * <br>
	 * A general warning about possible problems with the entity body.
	 * <br>
	 * <b>Response</b>
	 * <br>
	 * A general warning about possible problems with the entity body.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String WARNING = "Warning";
	/**
	 * <b>Response</b>
	 * <br>
	 * Indicates the authentication scheme that should be used to access the requested
	 * entity.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String WWW_AUTHENTICATE = "WWW-Authenticate";
	/**
	 * <b>Response</b>
	 * <br>
	 * Clickjacking protection: deny - no rendering within a frame, sameorigin - no
	 * rendering if origin mismatch, allow-from - allow from specified location, allowall
	 * - non-standard, allow from any location.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String X_FRAME_OPTIONS = "X-Frame-Options";

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new headers from copying the given {@code headers}.
	 *
	 * @param headers the headers to copy.
	 * @return a new copy of the given {@code headers}.
	 * @throws NullPointerException if the given {@code headers} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Headers copy(@NotNull Headers headers) {
		return new AbstractHeaders(headers);
	}

	/**
	 * <b>Default</b>
	 * <br>
	 * Return a new headers instance to be a placeholder if a the user has not specified a
	 * headers.
	 *
	 * @return a new default headers.
	 * @since 0.0.1 ~2021.03.21
	 */
	static Headers defaultHeaders() {
		return new AbstractHeaders();
	}

	/**
	 * <b>Empty</b>
	 * <br>
	 * Return an empty unmodifiable headers.
	 *
	 * @return an empty unmodifiable headers.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Headers empty() {
		return Headers.EMPTY;
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new headers from parsing the given {@code source}.
	 *
	 * @param source the source of the constructed headers.
	 * @return a new headers from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  HTTPRegExp#HEADERS}.
	 * @since 0.0.1 ~2021.03.21
	 */
	static Headers parse(@NotNull @NonNls @Pattern(HTTPRegExp.HEADERS) String source) {
		return new AbstractHeaders(source);
	}

	/**
	 * <b>Raw</b>
	 * <br>
	 * Construct a new raw headers with the given {@code value}.
	 *
	 * @param value the value of the constructed headers.
	 * @return a new raw headers.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Headers raw(@NotNull @NonNls String value) {
		return new RawHeaders(value);
	}

	/**
	 * <b>Unmodifiable</b>
	 * <br>
	 * Construct an unmodifiable copy of the given {@code headers}.
	 *
	 * @param headers the headers to be copied.
	 * @return an unmodifiable copy of the given {@code headers}.
	 * @throws NullPointerException if the given {@code headers} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Headers unmodifiable(@NotNull Headers headers) {
		return new RawHeaders(headers);
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new headers from combining the given {@code values} with the crlf
	 * "\r\n" as the delimiter. The null keys or values in the given {@code source} will
	 * be treated as it does not exist.
	 *
	 * @param values the headers values.
	 * @return a new headers from parsing and joining the given {@code values}.
	 * @throws NullPointerException     if the given {@code values} is null.
	 * @throws IllegalArgumentException if a key in the given {@code values} does not
	 *                                  match {@link HTTPRegExp#FIELD_NAME}; if a value in
	 *                                  the given {@code values} does not match {@link
	 *                                  HTTPRegExp#FIELD_VALUE}.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Headers with(@NotNull Map<@Nullable @NonNls String, @Nullable @NonNls String> values) {
		return new AbstractHeaders(values);
	}

	/**
	 * Set the attribute of the given {@code name} to be the results of invoking the given
	 * {@code operator} with the first argument being the current value assigned to the
	 * given {@code name} or an empty string if currently it is not set. If the {@code
	 * operator} returned {@code null} then the value with the given {@code name} will be
	 * removed.
	 * <br>
	 * Throwable thrown by the {@code operator} will fall throw this method unhandled.
	 *
	 * @param name     the name of the attribute to be computed.
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code name} is null.
	 * @throws IllegalArgumentException      if the given {@code name} does not match
	 *                                       {@link HTTPRegExp#FIELD_NAME}; if the value
	 *                                       returned from the {@code operator} does not
	 *                                       match {@link HTTPRegExp#FIELD_VALUE}.
	 * @throws UnsupportedOperationException if this headers is unmodifiable and the given
	 *                                       {@code operator} returned another value.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default Headers compute(@NotNull @NonNls @Pattern(HTTPRegExp.FIELD_NAME) String name, UnaryOperator<@NonNls String> operator) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(operator, "operator");
		String v = this.get(name);
		if (v == null) {
			@Subst("application/json") String value = operator.apply("");

			if (value != null)
				this.put(name, value);
		} else {
			@Subst("application/json") String value = operator.apply(v);

			if (value == null)
				this.remove(name);
			else if (!value.equals(v))
				this.put(name, value);
		}

		return this;
	}

	/**
	 * If absent, set the value of the given {@code name} to be the results of invoking
	 * the given {@code supplier}. If the {@code supplier} returned {@code null} nothing
	 * happens.
	 * <br>
	 * Throwable thrown by the {@code supplier} will fall throw this method unhandled.
	 *
	 * @param name     the name of the attribute to be computed.
	 * @param supplier the computing supplier.
	 * @return this.
	 * @throws NullPointerException          if the given {@code name} or {@code supplier}
	 *                                       is null.
	 * @throws IllegalArgumentException      if the given {@code name} does not match
	 *                                       {@link HTTPRegExp#FIELD_NAME}; if the value
	 *                                       returned from the {@code operator} does not
	 *                                       match {@link HTTPRegExp#FIELD_VALUE}.
	 * @throws UnsupportedOperationException if this headers is unmodifiable and the given
	 *                                       {@code operator} returned another value.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default Headers computeIfAbsent(@NotNull @NonNls @Pattern(HTTPRegExp.FIELD_NAME) String name, Supplier<@NonNls String> supplier) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(supplier, "supplier");
		String v = this.get(name);
		if (v == null) {
			@Subst("application/json") String value = supplier.get();

			if (value != null)
				this.put(name, value);
		}

		return this;
	}

	/**
	 * If present, set the value of the given {@code name} to be the results of invoking
	 * the given {@code operator} with the first argument being the current value assigned
	 * to the given {@code name}. If the {@code operator} returned {@code null} then the
	 * value with the given {@code name} will be removed.
	 * <br>
	 * Throwable thrown by the {@code operator} will fall throw this method unhandled.
	 *
	 * @param name     the name of the attribute to be computed.
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code name} or {@code operator}
	 *                                       is null.
	 * @throws IllegalArgumentException      if the given {@code name} does not match
	 *                                       {@link HTTPRegExp#FIELD_NAME}; if the value
	 *                                       returned from the {@code operator} does not
	 *                                       match {@link HTTPRegExp#FIELD_VALUE}.
	 * @throws UnsupportedOperationException if this headers is unmodifiable and the given
	 *                                       {@code operator} returned another value.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default Headers computeIfPresent(@NotNull @NonNls @Pattern(HTTPRegExp.FIELD_NAME) String name, UnaryOperator<@NonNls String> operator) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(operator, "operator");
		String v = this.get(name);
		if (v != null) {
			@Subst("application/json") String value = operator.apply(v);

			if (value == null)
				this.remove(name);
			else if (!value.equals(v))
				this.put(name, value);
		}

		return this;
	}

	/**
	 * Set the value of the attribute with the given {@code name} to the given {@code
	 * value}.
	 *
	 * @param name  the name of the attribute to be set.
	 * @param value the new value for to set to the attribute.
	 * @return this.
	 * @throws NullPointerException          if the given {@code name} or {@code value} is
	 *                                       null.
	 * @throws IllegalArgumentException      if the given {@code name} does not match
	 *                                       {@link HTTPRegExp#FIELD_NAME}; if the given
	 *                                       {@code value} does not match {@link
	 *                                       HTTPRegExp#FIELD_VALUE}.
	 * @throws UnsupportedOperationException if this headers is unmodifiable.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default Headers put(@NotNull @NonNls @Pattern(HTTPRegExp.FIELD_NAME) String name, @NotNull @NonNls @Pattern(HTTPRegExp.FIELD_VALUE) String value) {
		throw new UnsupportedOperationException("put");
	}

	/**
	 * Remove the attribute with the given {@code name}.
	 *
	 * @param name the name of the attribute to be removed.
	 * @return this.
	 * @throws NullPointerException          if the given {@code name} is null.
	 * @throws IllegalArgumentException      if the given {@code name} does not match
	 *                                       {@link HTTPRegExp#FIELD_NAME}.
	 * @throws UnsupportedOperationException if this headers is unmodifiable.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Headers remove(@NotNull @NonNls @Pattern(HTTPRegExp.FIELD_NAME) String name) {
		throw new UnsupportedOperationException("remove");
	}

	/**
	 * Capture this header into a new object.
	 *
	 * @return a clone of this header.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	Headers clone();

	/**
	 * Two headers are equal when they are the same instance or have an equal {@link
	 * #values()}.
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a headers and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	boolean equals(@Nullable Object object);

	/**
	 * The hash code of an headers is the {@code xor} of the hash codes of its values.
	 * (optional)
	 *
	 * @return the hash code of this headers.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	int hashCode();

	/**
	 * A string representation of this Headers. Invoke to get the text representing this
	 * in a request.
	 * <br>
	 * Typically (plural separated by CRLF):
	 * <pre>
	 *     FieldName: FieldValue
	 * </pre>
	 * Example:
	 * <pre>
	 *     Content-Type: application/x-www-form-urlencoded
	 *     Content-Length: length
	 * </pre>
	 *
	 * @return a string representation of this Header.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@NonNls
	@Contract(pure = true)
	@Pattern(HTTPRegExp.HEADERS)
	@Override
	String toString();

	/**
	 * Get the value assigned to the given {@code name}.
	 *
	 * @param name the name of the value to be returned.
	 * @return the value assigned to the given {@code name}. Or {@code null} if no such
	 * 		value.
	 * @throws NullPointerException     if the given {@code name} is null.
	 * @throws IllegalArgumentException if the given {@code name} does not match {@link
	 *                                  HTTPRegExp#FIELD_NAME}.
	 * @since 0.0.1 ~2021.03.21
	 */
	@Nullable
	@NonNls
	@Contract(pure = true)
	@Pattern(HTTPRegExp.FIELD_VALUE)
	String get(@NotNull @NonNls @Pattern(HTTPRegExp.FIELD_NAME) String name);

	/**
	 * Return an unmodifiable view of the values of this query.
	 *
	 * @return an unmodifiable view of the values of this.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@UnmodifiableView
	@Contract(value = "->new", pure = true)
	Map<@NotNull @NonNls String, @NotNull @NonNls String> values();
}
//
//	/**
//	 * Construct a new query from combining the given {@code values} with the and-sign "&"
//	 * as the delimiter. The null elements are skipped.
//	 *
//	 * @param values the query values.
//	 * @return a new headers from the given {@code values}.
//	 * @throws NullPointerException     if the given {@code values} is null.
//	 * @throws IllegalArgumentException if an element in the given {@code values} does not
//	 *                                  match {@link HTTPRegExp#HEADER}.
//	 * @since 0.0.1 ~2021.03.21
//	 */
//	static Headers parse(@Nullable @NonNls @Pattern(HTTPRegExp.HEADER) String @NotNull ... values) {
//		return new AbstractHeaders(values);
//	}
