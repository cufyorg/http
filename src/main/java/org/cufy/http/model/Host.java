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

import org.cufy.http.impl.HostImpl;
import org.cufy.http.syntax.UriRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * <b>Constant</b> (PCT Encode)
 * <br>
 * The "Host" part of the "Authority" part of a Uri.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public interface Host extends Serializable {
	/**
	 * Local host constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Host LOCALHOST = new HostImpl("localhost");
	/**
	 * Unspecified host constant.
	 *
	 * @since 0.3.0 ~2021.11.16
	 */
	Host UNSPECIFIED = new HostImpl("");

	/**
	 * Two hosts are equal when they are the same instance or have the same {@code
	 * host-literal} (the value returned from {@link #toString()}).
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a host and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	boolean equals(@Nullable Object object);

	/**
	 * The hash code of a host is the hash code of its host-literal. (optional)
	 *
	 * @return the hash code of this host.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	int hashCode();

	/**
	 * A string representation of this Host. Invoke to get the text representing this in a
	 * request.
	 * <br>
	 * Example:
	 * <pre>
	 *     www.example.com
	 * </pre>
	 *
	 * @return a string representation of this Host.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	@Pattern(UriRegExp.HOST)
	@Override
	String toString();
}
