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
package org.cufy.http.model;

import org.cufy.http.impl.MethodImpl;
import org.cufy.http.raw.RawMethod;
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
	Method CONNECT = new MethodImpl("CONNECT");
	/**
	 * The DELETE method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Method DELETE = new MethodImpl("DELETE");
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
	Method GET = new MethodImpl("GET");
	/**
	 * The HEAD method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Method HEAD = new MethodImpl("HEAD");
	/**
	 * The OPTIONS method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Method OPTIONS = new MethodImpl("OPTIONS");
	/**
	 * The PATCH method constant.
	 *
	 * @since 0.3.0 ~2021.11.16
	 */
	Method PATCH = new MethodImpl("PATCH");
	/**
	 * The POST method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Method POST = new MethodImpl("POST");
	/**
	 * The PUT method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Method PUT = new MethodImpl("PUT");
	/**
	 * The TRACE method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Method TRACE = new MethodImpl("TRACE");

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
