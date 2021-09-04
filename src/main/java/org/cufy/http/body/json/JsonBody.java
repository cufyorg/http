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
package org.cufy.http.body.json;

import org.cufy.http.body.Body;
import org.cufy.http.syntax.HttpRegExp;
import org.cufy.http.syntax.UriRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <b>Mapping</b>
 * <br>
 * A body implementation for json.
 * <br>
 * To use it you need to include <a href="https://github.com/stleary/JSON-java">org.json</a>
 * library.
 *
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.6 ~2021.03.30
 */
public class JsonBody implements Body {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 7605954371493703764L;

	/**
	 * The value fo this body.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	protected transient JSONObject values;

	/**
	 * <b>Default</b>
	 * <br>
	 * Construct a new json-body with an empty object.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	public JsonBody() {
		this.values = new JSONObject();
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new json-body copying the given {@code body}.
	 * <br>
	 * Note: The constructed body will NOT have the {@link #getContentType()} of the given
	 * {@code body} and might not have the exact content. (the content might get
	 * reformatted, rearranged, compressed, encoded or encrypted/decrypted)
	 *
	 * @param body the body to be copied.
	 * @throws NullPointerException     if the given {@code body} is null.
	 * @throws IllegalArgumentException if the given {@code body} cannot be converted into
	 *                                  a json body.
	 * @since 0.0.6 ~2021.03.30
	 */
	public JsonBody(@NotNull Body body) {
		Objects.requireNonNull(body, "body");
		try {
			this.values = new JSONObject(body.toString());
		} catch (JSONException e) {
			throw new IllegalArgumentException("invalid body for json", e);
		}
	}

	/**
	 * <b>Integration</b>
	 * <br>
	 * Construct a new body with its parameters set from the given {@code map}.
	 * <br>
	 * This method depends on {@link JSONObject#JSONObject(Map) JSONObject(Map)} so any
	 * illegal values will be simply replaced with {@link JSONObject#NULL null}.
	 *
	 * @param map the map to set the values of constructed body from.
	 * @throws NullPointerException     if the given {@code map} is null; if the given
	 *                                  {@code map} has a null key.
	 * @throws IllegalArgumentException if a key in the given {@code map} does not match
	 *                                  {@link UriRegExp#ATTR_NAME}; if a value in the
	 *                                  given {@code map} does not match {@link
	 *                                  UriRegExp#ATTR_VALUE}.
	 * @since 0.0.6 ~2021.03.31
	 */
	public JsonBody(@NotNull Map<@NotNull String, @Nullable Object> map) {
		Objects.requireNonNull(map, "map");
		try {
			this.values = new JSONObject(map);
		} catch (JSONException e) {
			throw new IllegalArgumentException("invalid map for json", e);
		}
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new json-body from parsing the given {@code source}.
	 *
	 * @param source the json text to construct this body from.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} is cannot be parsed
	 *                                  into json object.
	 * @since 0.0.6 ~2021.03.30
	 */
	public JsonBody(@NotNull String source) {
		Objects.requireNonNull(source, "source");
		try {
			this.values = new JSONObject(source);
		} catch (JSONException e) {
			throw new IllegalArgumentException("invalid json value", e);
		}
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new body from the given components.
	 *
	 * @param values the json object of the constructed json body.
	 * @throws NullPointerException if the given {@code values} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public JsonBody(@NotNull JSONObject values) {
		Objects.requireNonNull(values, "values");
		try {
			this.values = new JSONObject(values.toString());
		} catch (JSONException e) {
			throw new IllegalArgumentException("invalid json values", e);
		}
	}

	/**
	 * <b>Default</b>
	 * <br>
	 * Return a new json-body instance to be a placeholder if a the user has not specified
	 * a json-body.
	 *
	 * @return a new default json-body.
	 * @since 0.0.6 ~2021.03.31
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	public static JsonBody json() {
		return new JsonBody();
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new json-body from copying the given {@code body}.
	 *
	 * @param body the body to copy.
	 * @return a new copy of the given {@code body}.
	 * @throws NullPointerException if the given {@code body} is null.
	 * @since 0.0.6 ~2021.03.31
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static JsonBody json(@NotNull Body body) {
		return new JsonBody(body);
	}

	/**
	 * <b>Integration</b>
	 * <br>
	 * Construct a new body with its parameters set from the given {@code map}.
	 * <br>
	 * This method depends on {@link JSONObject#JSONObject(Map) JSONObject(Map)} so any
	 * illegal values will be simply replaced with {@link JSONObject#NULL null}.
	 *
	 * @param map the map to set the values of constructed body from.
	 * @return a new json-body from the given {@code map}.
	 * @throws NullPointerException     if the given {@code map} is null; if the given
	 *                                  {@code map} has a null key.
	 * @throws IllegalArgumentException if a key in the given {@code map} does not match
	 *                                  {@link UriRegExp#ATTR_NAME}; if a value in the
	 *                                  given {@code map} does not match {@link
	 *                                  UriRegExp#ATTR_VALUE}.
	 * @since 0.0.6 ~2021.03.31
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static JsonBody json(@NotNull Map<@NotNull String, @Nullable Object> map) {
		return new JsonBody(map);
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new json-body from parsing the given {@code source}.
	 *
	 * @param source the source to parse to construct the json-body.
	 * @return a new json-body from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} is cannot be parsed
	 *                                  into json object.
	 * @since 0.0.6 ~2021.03.31
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static JsonBody json(@NotNull String source) {
		return new JsonBody(source);
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new body from the given components.
	 *
	 * @param values the json object of the constructed json body.
	 * @return a new json-body with from the given components.
	 * @throws NullPointerException if the given {@code values} is null.
	 * @since 0.0.6 ~2021.03.31
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static JsonBody json(@NotNull JSONObject values) {
		return new JsonBody(values);
	}

	/**
	 * <b>Builder</b>
	 * <br>
	 * Construct a new json body with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new json body.
	 * @return the json body constructed from the given {@code builder}.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.2.3 ~2021.08.27
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static JsonBody json(@NotNull Consumer<JsonBody> builder) {
		Objects.requireNonNull(builder, "builder");
		JsonBody jsonBody = new JsonBody();
		builder.accept(jsonBody);
		return jsonBody;
	}

	@NotNull
	@Override
	public JsonBody clone() {
		try {
			JsonBody clone = (JsonBody) super.clone();
			clone.values = new JSONObject(this.values.toString());
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof JsonBody) {
			JsonBody body = (JsonBody) object;

			//noinspection NonFinalFieldReferenceInEquals
			return this.values.similar(body.values);
		}
		if (object instanceof Body) {
			Body body = (Body) object;

			return Objects.equals(this.getContentType(), body.getContentType()) &&
				   Objects.equals(this.toString(), body.toString());
		}

		return false;
	}

	@Override
	@Range(from = 0, to = Long.MAX_VALUE)
	public long getContentLength() {
		return this.toString()
				   .codePoints()
				   .map(cp -> cp <= 0x7ff ? cp <= 0x7f ? 1 : 2 : cp <= 0xffff ? 3 : 4)
				   .asLongStream()
				   .sum();
	}

	@Nullable
	@Pattern(HttpRegExp.FIELD_VALUE)
	@Override
	public String getContentType() {
		return "application/json; charset=utf-8";
	}

	@Override
	public int hashCode() {
		//noinspection NonFinalFieldReferencedInHashCode
		return this.values.hashCode();
	}

	@NotNull
	@Override
	public String toString() {
		return this.values.toString();
	}

	/**
	 * Set the value of the given {@code name} to be the results of invoking the given
	 * {@code operator} with the first argument being the current value assigned to the
	 * given {@code name} or {@code null} if currently it is not set. If the {@code
	 * operator} returned {@code null} then the value with the given {@code name} will be
	 * removed.
	 * <br>
	 * Throwable thrown by the {@code operator} will fall throw this method unhandled.
	 *
	 * @param name     the name of the attribute to be computed.
	 * @param operator the computing operator.
	 * @param <T>      the expected type for the current value.
	 * @return this.
	 * @throws NullPointerException          if the given {@code name} or {@code operator}
	 *                                       is null.
	 * @throws IllegalArgumentException      if the value returned from the given {@code
	 *                                       operator} is an invalid json value (see
	 *                                       {@link JSONObject#put(String, Object)}).
	 * @throws UnsupportedOperationException if the json-object of this is unmodifiable
	 *                                       and the {@code operator} returned another
	 *                                       value.
	 * @throws ClassCastException            if the given {@code operator} mistaken the
	 *                                       real type of the value.
	 * @since 0.0.6 ~2021.03.31
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	public <T> JsonBody compute(@NotNull String name, Function<@Nullable T, @Nullable Object> operator) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(operator, "operator");
		T v = this.get(name);

		if (v == null) {
			Object value = operator.apply(null);

			if (value != null)
				this.put(name, value);
		} else {
			Object value = operator.apply(v);

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
	 * @throws IllegalArgumentException      if the value returned from the given {@code
	 *                                       operator} is an invalid json value (see
	 *                                       {@link JSONObject#put(String, Object)}).
	 * @throws UnsupportedOperationException if the json-object of this is unmodifiable
	 *                                       and the {@code operator} returned another
	 *                                       value.
	 * @since 0.0.6 ~2021.03.31
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	public JsonBody computeIfAbsent(@NotNull String name, Supplier<@Nullable Object> supplier) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(supplier, "supplier");
		Object v = this.get(name);

		if (v == null) {
			Object value = supplier.get();

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
	 * @param <T>      the expected type for the current value.
	 * @return this.
	 * @throws NullPointerException          if the given {@code name} or {@code operator}
	 *                                       is null.
	 * @throws IllegalArgumentException      if the value returned from the given {@code
	 *                                       operator} is an invalid json value (see
	 *                                       {@link JSONObject#put(String, Object)}).
	 * @throws UnsupportedOperationException if the json-object of this is unmodifiable
	 *                                       and the {@code operator} returned another
	 *                                       value.
	 * @throws ClassCastException            if the given {@code operator} mistaken the
	 *                                       real type of the value.
	 * @since 0.0.6 ~2021.03.31
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	public <T> JsonBody computeIfPresent(@NotNull String name, @NotNull Function<@NotNull T, @Nullable Object> operator) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(operator, "operator");
		T v = this.get(name);

		if (v != null) {
			Object value = operator.apply(v);

			if (value == null)
				this.remove(name);
			else if (!value.equals(v))
				this.put(name, value);
		}

		return this;
	}

	/**
	 * Get the value assigned to the given {@code name}.
	 * <br>
	 * Note: type parameters are a compile-time thing. So, if the caller expected a value
	 * of type other than the real type then a {@link ClassCastException} will be thrown
	 * outside this method.
	 *
	 * @param name the name of the value to be returned. Or {@code null} if no such
	 *             value.
	 * @param <T>  the expected type of the value.
	 * @return the value assigned to the given {@code name}.
	 * @throws NullPointerException if the given {@code name} is null.
	 * @since 0.0.6 ~2021.03.31
	 */
	@Nullable
	@Contract(pure = true)
	public <T> T get(@NotNull String name) {
		Objects.requireNonNull(name, "name");
		//noinspection unchecked
		return (T) this.values.get(name);
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
	 * @throws IllegalArgumentException      if the given {@code value} is an invalid json
	 *                                       value (see {@link JSONObject#put(String,
	 *                                       Object)}).
	 * @throws UnsupportedOperationException if the json-object of this is unmodifiable.
	 * @since 0.0.6 ~2021.03.31
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	public JsonBody put(@NotNull String name, @NotNull Object value) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(value, "value");
		try {
			this.values.put(name, value);
		} catch (JSONException e) {
			throw new IllegalArgumentException("illegal json value: " + value, e);
		}
		return this;
	}

	/**
	 * Remove the attribute with the given {@code name}.
	 *
	 * @param name the name of the attribute to be removed.
	 * @return this.
	 * @throws NullPointerException          if the given {@code name} is null.
	 * @throws UnsupportedOperationException if the json-object of this is unmodifiable.
	 * @since 0.0.6 ~2021.03.31
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public JsonBody remove(@NotNull String name) {
		Objects.requireNonNull(name, "name");
		this.values.remove(name);
		return this;
	}

	/**
	 * Return the object defined for this.
	 *
	 * @return the object of this.
	 * @since 0.0.6 ~2021.03.29
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	public JSONObject values() {
		return new JSONObject(this.values.toString());
	}

	@SuppressWarnings("JavaDoc")
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		Objects.requireNonNull(stream, "stream");
		stream.defaultReadObject();
		this.values = new JSONObject((String) stream.readObject());
	}

	@SuppressWarnings("JavaDoc")
	private void writeObject(ObjectOutputStream stream) throws IOException {
		Objects.requireNonNull(stream, "stream");
		stream.defaultWriteObject();
		stream.writeObject(this.values.toString());
	}
}
