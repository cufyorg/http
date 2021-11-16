package org.cufy.http.ext

import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FromString
import org.cufy.http.ext.form.ParametersBody
import org.cufy.http.ext.text.TextBody
import org.cufy.http.impl.*
import org.cufy.http.model.*

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
	Client client = new ClientImpl()
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
	Headers headers = new HeadersImpl()
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
	Request request = new RequestImpl()
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
	RequestLine requestLine = new RequestLineImpl()
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
	Response response = new ResponseImpl()
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
	StatusLine statusLine = new StatusLineImpl()
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
	Authority authority = new AuthorityImpl()
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
	Query query = new QueryImpl()
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
	Uri uri = new UriImpl()
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
	UserInfo userInfo = new UserInfoImpl()
	builder.delegate = userInfo
	builder.resolveStrategy = Closure.DELEGATE_FIRST
	builder(userInfo)
	return userInfo
}
