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

import org.cufy.http.syntax.HttpRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * <b>Constant</b> (No Encode)
 * <br>
 * The request method; an object describing the method used in a http request.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public interface Method extends Serializable {
	/**
	 * The CONNECT method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Method CONNECT = new AbstractMethod("CONNECT");
	/**
	 * The DELETE method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Method DELETE = new AbstractMethod("DELETE");
	/**
	 * An empty method constant.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	Method EMPTY = new RawMethod();
	/**
	 * The GET method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Method GET = new AbstractMethod("GET");
	/**
	 * The HEAD method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Method HEAD = new AbstractMethod("HEAD");
	/**
	 * The OPTIONS method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Method OPTIONS = new AbstractMethod("OPTIONS");
	/**
	 * The POST method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Method POST = new AbstractMethod("POST");
	/**
	 * The PUT method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Method PUT = new AbstractMethod("PUT");
	/**
	 * The TRACE method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Method TRACE = new AbstractMethod("TRACE");

	/**
	 * <b>Default</b>
	 * <br>
	 * Return a method instance to be a placeholder if a the user has not specified a
	 * method.
	 *
	 * @return the default method.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	static Method method() {
		return Method.GET;
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new default-implementation method component with its method literal
	 * being the given {@code source}.
	 *
	 * @param source the source of the method literal of the constructed method
	 *               component.
	 * @return a new method from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  HttpRegExp#METHOD}.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Method method(@NotNull @Pattern(HttpRegExp.METHOD) String source) {
		return new AbstractMethod(source);
	}

	/**
	 * <b>Raw</b>
	 * <br>
	 * Construct a new raw method with the given {@code value}.
	 *
	 * @param value the value of the constructed method.
	 * @return a new raw method.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Method raw(@NotNull String value) {
		return new RawMethod(value);
	}

	/**
	 * Two methods are equal when they are the same instance or have the same {@code
	 * method-literal} (the value returned from {@link #toString()}).
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a method and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	boolean equals(@Nullable Object object);

	/**
	 * The hash code of a method is the hash code of its method-literal. (optional)
	 *
	 * @return the hash code of this method.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	int hashCode();

	/**
	 * A string representation of this Method. Invoke to get the text representing this in
	 * a request.
	 * <br>
	 * Example:
	 * <pre>
	 *     GET
	 * </pre>
	 *
	 * @return a string representation of this Method.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	@Pattern(HttpRegExp.METHOD)
	@Override
	String toString();
}
