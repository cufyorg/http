package org.cufy.http.groovy

import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FromString
import org.cufy.http.body.JsonBody
import org.cufy.http.body.ParametersBody
import org.cufy.http.body.TextBody
import org.cufy.http.connect.AbstractClient
import org.cufy.http.connect.Client
import org.cufy.http.request.*
import org.cufy.http.response.AbstractResponse
import org.cufy.http.response.AbstractStatusLine
import org.cufy.http.response.Response
import org.cufy.http.response.StatusLine
import org.cufy.http.uri.*

/**
 * Construct a new json body with the given {@code builder}.
 *
 * @param builder the builder to apply to the new json body.
 * @return the json body constructed from the given {@code builder}.
 * @throws NullPointerException if the given {@code builder} is null.
 * @since 0.2.8 ~2021.08.27
 */
static JsonBody JsonBody(
		@DelegatesTo(value = JsonBody.class, strategy = Closure.DELEGATE_FIRST)
		@ClosureParams(value = FromString.class, options = "org.cufy.http.body.JsonBody")
				Closure builder
) {
	JsonBody jsonBody = new JsonBody()
	builder.delegate = jsonBody
	builder.resolveStrategy = Closure.DELEGATE_FIRST
	builder(jsonBody)
	return jsonBody
}

/**
 * Construct a new parameters body with the given {@code builder}.
 *
 * @param builder the builder to apply to the new parameters body.
 * @return the parameters body constructed from the given {@code builder}.
 * @throws NullPointerException if the given {@code builder} is null.
 * @since 0.2.8 ~2021.08.27
 */
static ParametersBody ParametersBody(
		@DelegatesTo(value = ParametersBody.class, strategy = Closure.DELEGATE_FIRST)
		@ClosureParams(value = FromString.class, options = "org.cufy.http.body.ParametersBody")
				Closure builder
) {
	ParametersBody parametersBody = new ParametersBody()
	builder.delegate = parametersBody
	builder.resolveStrategy = Closure.DELEGATE_FIRST
	builder(parametersBody)
	return parametersBody
}

/**
 * Construct a new text body with the given {@code builder}.
 *
 * @param builder the builder to apply to the new text body.
 * @return the text body constructed from the given {@code builder}.
 * @throws NullPointerException if the given {@code builder} is null.
 * @since 0.2.8 ~2021.08.27
 */
static TextBody TextBody(
		@DelegatesTo(value = TextBody.class, strategy = Closure.DELEGATE_FIRST)
		@ClosureParams(value = FromString.class, options = "org.cufy.http.body.TextBody")
				Closure builder
) {
	TextBody textBody = new TextBody()
	builder.delegate = textBody
	builder.resolveStrategy = Closure.DELEGATE_FIRST
	builder(textBody)
	return textBody
}

/**
 * Construct a new client with the given {@code builder}.
 *
 * @param builder the builder to apply to the new client.
 * @return the client constructed from the given {@code builder}.
 * @throws NullPointerException if the given {@code builder} is null.
 * @since 0.2.8 ~2021.08.27
 */
static Client Client(
		@DelegatesTo(value = Client.class, strategy = Closure.DELEGATE_FIRST)
		@ClosureParams(value = FromString.class, options = "org.cufy.http.connect.Client")
				Closure builder
) {
	Client client = new AbstractClient()
	builder.delegate = client
	builder.resolveStrategy = Closure.DELEGATE_FIRST
	builder(client)
	return client
}

/**
 * Construct a new headers with the given {@code builder}.
 *
 * @param builder the builder to apply to the new headers.
 * @return the headers constructed from the given {@code builder}.
 * @throws NullPointerException if the given {@code builder} is null.
 * @since 0.2.8 ~2021.08.27
 */
static Headers Headers(
		@DelegatesTo(value = Headers.class, strategy = Closure.DELEGATE_FIRST)
		@ClosureParams(value = FromString.class, options = "org.cufy.http.request.Headers")
				Closure builder
) {
	Headers headers = new AbstractHeaders()
	builder.delegate = headers
	builder.resolveStrategy = Closure.DELEGATE_FIRST
	builder(headers)
	return headers
}

/**
 * Construct a new request with the given {@code builder}.
 *
 * @param builder the builder to apply to the new request.
 * @return the request constructed from the given {@code builder}.
 * @throws NullPointerException if the given {@code builder} is null.
 * @since 0.2.8 ~2021.08.27
 */
static Request Request(
		@DelegatesTo(value = Request.class, strategy = Closure.DELEGATE_FIRST)
		@ClosureParams(value = FromString.class, options = "org.cufy.http.request.Request")
				Closure builder
) {
	Request request = new AbstractRequest()
	builder.delegate = request
	builder.resolveStrategy = Closure.DELEGATE_FIRST
	builder(request)
	return request
}

/**
 * Construct a new request line with the given {@code builder}.
 *
 * @param builder the builder to apply to the new request line.
 * @return the request line constructed from the given {@code builder}.
 * @throws NullPointerException if the given {@code builder} is null.
 * @since 0.2.8 ~2021.08.27
 */
static RequestLine RequestLine(
		@DelegatesTo(value = RequestLine.class, strategy = Closure.DELEGATE_FIRST)
		@ClosureParams(value = FromString.class, options = "org.cufy.http.request.RequestLine")
				Closure builder
) {
	RequestLine requestLine = new AbstractRequestLine()
	builder.delegate = requestLine
	builder.resolveStrategy = Closure.DELEGATE_FIRST
	builder(requestLine)
	return requestLine
}

/**
 * Construct a new response with the given {@code builder}.
 *
 * @param builder the builder to apply to the new response.
 * @return the response constructed from the given {@code builder}.
 * @throws NullPointerException if the given {@code builder} is null.
 * @since 0.2.8 ~2021.08.27
 */
static Response Response(
		@DelegatesTo(value = Response.class, strategy = Closure.DELEGATE_FIRST)
		@ClosureParams(value = FromString.class, options = "org.cufy.http.response.Response")
				Closure builder
) {
	Response response = new AbstractResponse()
	builder.delegate = response
	builder.resolveStrategy = Closure.DELEGATE_FIRST
	builder(response)
	return response
}

/**
 * Construct a new status line with the given {@code builder}.
 *
 * @param builder the builder to apply to the new status line.
 * @return the status line constructed from the given {@code builder}.
 * @throws NullPointerException if the given {@code builder} is null.
 * @since 0.2.8 ~2021.08.27
 */
static StatusLine StatusLine(
		@DelegatesTo(value = StatusLine.class, strategy = Closure.DELEGATE_FIRST)
		@ClosureParams(value = FromString.class, options = "org.cufy.http.response.StatusLine")
				Closure builder
) {
	StatusLine statusLine = new AbstractStatusLine()
	builder.delegate = statusLine
	builder.resolveStrategy = Closure.DELEGATE_FIRST
	builder(statusLine)
	return statusLine
}

/**
 * Construct a new authority with the given {@code builder}.
 *
 * @param builder the builder to apply to the new authority.
 * @return the authority constructed from the given {@code builder}.
 * @throws NullPointerException if the given {@code builder} is null.
 * @since 0.2.8 ~2021.08.27
 */
static Authority Authority(
		@DelegatesTo(value = Authority.class, strategy = Closure.DELEGATE_FIRST)
		@ClosureParams(value = FromString.class, options = "org.cufy.http.uri.Authority")
				Closure builder
) {
	Authority authority = new AbstractAuthority()
	builder.delegate = authority
	builder.resolveStrategy = Closure.DELEGATE_FIRST
	builder(authority)
	return authority
}

/**
 * Construct a new query with the given {@code builder}.
 *
 * @param builder the builder to apply to the new query.
 * @return the query constructed from the given {@code builder}.
 * @throws NullPointerException if the given {@code builder} is null.
 * @since 0.2.8 ~2021.08.27
 */
static Query Query(
		@DelegatesTo(value = Query.class, strategy = Closure.DELEGATE_FIRST)
		@ClosureParams(value = FromString.class, options = "org.cufy.http.uri.Query")
				Closure builder
) {
	Query query = new AbstractQuery()
	builder.delegate = query
	builder.resolveStrategy = Closure.DELEGATE_FIRST
	builder(query)
	return query
}

/**
 * Construct a new uri with the given {@code builder}.
 *
 * @param builder the builder to apply to the new uri.
 * @return the uri constructed from the given {@code builder}.
 * @throws NullPointerException if the given {@code builder} is null.
 * @since 0.2.8 ~2021.08.27
 */
static Uri Uri(
		@DelegatesTo(value = Uri.class, strategy = Closure.DELEGATE_FIRST)
		@ClosureParams(value = FromString.class, options = "org.cufy.http.uri.Uri")
				Closure builder
) {
	Uri uri = new AbstractUri()
	builder.delegate = uri
	builder.resolveStrategy = Closure.DELEGATE_FIRST
	builder(uri)
	return uri
}

/**
 * Construct a new user info with the given {@code builder}.
 *
 * @param builder the builder to apply to the new user info.
 * @return the user info constructed from the given {@code builder}.
 * @throws NullPointerException if the given {@code builder} is null.
 * @since 0.2.8 ~2021.08.27
 */
static UserInfo UserInfo(
		@DelegatesTo(value = UserInfo.class, strategy = Closure.DELEGATE_FIRST)
		@ClosureParams(value = FromString.class, options = "org.cufy.http.uri.UserInfo")
				Closure builder
) {
	UserInfo userInfo = new AbstractUserInfo()
	builder.delegate = userInfo
	builder.resolveStrategy = Closure.DELEGATE_FIRST
	builder(userInfo)
	return userInfo
}
