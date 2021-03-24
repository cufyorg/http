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
package org.cufy.http.component;

import org.cufy.http.syntax.ABNFPattern;
import org.cufy.http.syntax.HTTPPattern;
import org.cufy.http.syntax.HTTPRegExp;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A basic implementation of the interface {@link Headers}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.22
 */
public class AbstractHeaders implements Headers {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -3881735342601146869L;

	/**
	 * The headers.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected HashMap<@NotNull @NonNls String, @NotNull @NonNls String> values = new HashMap<>();

	/**
	 * Construct a new empty headers.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractHeaders() {

	}

	/**
	 * Construct a new headers from parsing the given {@code source}.
	 *
	 * @param source the source of the constructed headers.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  HTTPRegExp#HEADERS}.
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractHeaders(@NotNull @NonNls @Pattern(HTTPRegExp.HEADERS) String source) {
		Objects.requireNonNull(source, "source");
		if (!HTTPPattern.HEADERS.matcher(source).matches())
			throw new IllegalArgumentException("invalid headers: " + source);
		for (String s : ABNFPattern.CRLF.split(source))
			if (!s.isEmpty()) {
				String[] ss = s.split("\\:", 2);
				this.values.put(
						ss[0],
						ss.length == 2 ? ss[1] : ""
				);
			}
	}

	/**
	 * Construct a new query from combining the given {@code values} with the and-sign "&"
	 * as the delimiter. The null elements are skipped.
	 *
	 * @param values the query values.
	 * @throws NullPointerException     if the given {@code values} is null.
	 * @throws IllegalArgumentException if an element in the given {@code values} does not
	 *                                  match {@link HTTPRegExp#HEADER}.
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractHeaders(@Nullable @NonNls @Pattern(HTTPRegExp.HEADER) String @NotNull ... values) {
		Objects.requireNonNull(values, "values");
		for (String value : values)
			if (value != null) {
				if (!HTTPPattern.HEADER.matcher(value).matches())
					throw new IllegalArgumentException("invalid header: " + value);

				String[] s = value.split("\\:", 2);
				this.values.put(
						s[0],
						s.length == 2 ? s[1] : ""
				);
			}
	}

	@NotNull
	@Override
	public AbstractHeaders clone() {
		try {
			AbstractHeaders clone = (AbstractHeaders) super.clone();
			clone.values = (HashMap<String, String>) this.values.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof Headers) {
			Headers headers = (Headers) object;

			//noinspection ObjectInstantiationInEqualsHashCode
			return Objects.equals(this.values(), headers.values());
		}

		return false;
	}

	@Nullable
	@NonNls
	@Pattern(HTTPRegExp.FIELD_VALUE)
	@Override
	public String get(@NotNull @NonNls String name) {
		Objects.requireNonNull(name, "name");
		if (!HTTPPattern.FIELD_NAME.matcher(name).matches())
			throw new IllegalArgumentException("invalid field name: " + name);

		@Subst("query") String s = this.values.get(name);
		return s;
	}

	@Override
	public int hashCode() {
		//noinspection NonFinalFieldReferencedInHashCode
		return this.values.hashCode();
	}

	@NotNull
	@Override
	public Headers put(@NotNull @NonNls String name, @NotNull @NonNls String value) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(value, "value");
		if (!HTTPPattern.FIELD_NAME.matcher(name).matches())
			throw new IllegalArgumentException("invalid field name: " + name);
		if (!HTTPPattern.FIELD_VALUE.matcher(value).matches())
			throw new IllegalArgumentException("invalid field value: " + value);

		this.values.put(name, value);
		return this;
	}

	@NotNull
	@Override
	public Headers remove(@NotNull @NonNls String name) {
		Objects.requireNonNull(name, "name");
		if (!HTTPPattern.FIELD_NAME.matcher(name).matches())
			throw new IllegalArgumentException("invalid field name: " + name);

		this.values.remove(name);
		return this;
	}

	@NotNull
	@NonNls
	@Pattern(HTTPRegExp.HEADERS)
	@Override
	public String toString() {
		@Subst("Content-Length: 1024\r\nContent-Type: application/json\r\n") String s = this.values.entrySet()
				.stream()
				.map(entry -> entry.getKey() + ": " + entry.getValue() + "\r\n")
				.collect(Collectors.joining());
		return s;
	}

	@NotNull
	@UnmodifiableView
	@Override
	public Map<@NotNull @NonNls String, @NotNull @NonNls String> values() {
		return Collections.unmodifiableMap(this.values);
	}
}
