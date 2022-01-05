/*
 *	Copyright 2021-2022 Cufy
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
package org.cufy.http.json;

import org.cufy.http.json.token.JsonObjectToken;
import org.cufy.http.json.token.JsonTokenException;
import org.cufy.http.json.token.JsonTokenSource;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A json object is an object that contain other elements by mapping each element with a
 * string name.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.23
 */
@ApiStatus.Experimental
public class JsonObject implements JsonStruct, Map<@NotNull JsonString, @NotNull JsonElement> {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -4078761236974001355L;

	/**
	 * The map backing this object.
	 *
	 * @since 0.3.0 ~2021.11.23
	 */
	@NotNull
	protected Map<@NotNull JsonString, @NotNull JsonElement> map;

	/**
	 * Construct a new json object.
	 *
	 * @since 0.3.0 ~2021.11.23
	 */
	public JsonObject() {
		this.map = new LinkedHashMap<>();
	}

	/**
	 * Construct a new json object.
	 *
	 * @param map the map to be backing the constructed json object.
	 * @throws NullPointerException if the given {@code map} is null.
	 * @since 0.3.0 ~2021.11.23
	 */
	public JsonObject(@NotNull Map<@NotNull JsonString, @NotNull JsonElement> map) {
		Objects.requireNonNull(map, "map");
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.map = map;
	}

	/**
	 * Construct a new json object with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new json object.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.3.0 ~2021.12.15
	 */
	public JsonObject(@NotNull Consumer<JsonObject> builder) {
		Objects.requireNonNull(builder, "builder");
		this.map = new LinkedHashMap<>();
		//noinspection ThisEscapedInObjectConstruction
		builder.accept(this);
	}

	/**
	 * Construct a new json object from parsing the given {@code source}.
	 *
	 * @param source the source string to be parsed.
	 * @return a new json object from parsing the given source.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} is invalid json object.
	 * @since 0.3.0 ~2021.12.15
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static JsonObject parse(@NotNull @Language("json") String source) {
		Objects.requireNonNull(source, "source");
		try {
			return new JsonObjectToken(new JsonTokenSource(new StringReader(source)))
					.nextElement();
		} catch (JsonTokenException e) {
			throw new IllegalArgumentException(e.formatMessage(source), e);
		} catch (IOException e) {
			throw new InternalError(e);
		}
	}

	@Nullable
	@Override
	public JsonElement assign(@NotNull JsonPath path, @NotNull JsonElement element) {
		Objects.requireNonNull(path, "path");
		Objects.requireNonNull(element, "element");
		String name = path.getName();
		JsonPath next = path.getNext();

		JsonString string = new JsonString(name);

		if (next != null) {
			JsonElement e = this.get(string);

			if (e instanceof JsonStruct) {
				JsonStruct s = (JsonStruct) e;

				return s.assign(next, element);
			}

			if (e == null) {
				if (path.isOptional())
					return null;

				throw new NoSuchElementException(
						"Missing property " + next
				);
			}

			if (path.isLenient())
				return null;

			throw new IllegalStateException(
					"Cannot access non struct property " + next
			);
		}

		return this.put(string, element);
	}

	@Nullable
	@Override
	public JsonElement delete(@NotNull JsonPath path) {
		Objects.requireNonNull(path, "path");
		String name = path.getName();
		JsonPath next = path.getNext();

		JsonString string = new JsonString(name);

		if (next != null) {
			JsonElement e = this.get(string);

			if (e instanceof JsonStruct) {
				JsonStruct s = (JsonStruct) e;

				return s.delete(next);
			}

			if (e == null) {
				if (path.isOptional())
					return null;

				throw new NoSuchElementException(
						"Missing property " + next
				);
			}

			if (path.isLenient())
				return null;

			throw new IllegalStateException(
					"Cannot access non struct property " + next
			);
		}

		return this.remove(string);
	}

	@NotNull
	@Override
	public String json(@NotNull String indent, @NotNull String tab) {
		Objects.requireNonNull(indent, "indent");
		Objects.requireNonNull(tab, "tab");
		String indentTab = indent + tab;
		StringBuilder builder = new StringBuilder();

		builder.append("{");

		Iterator<Entry<JsonString, JsonElement>> iterator = this.entrySet().iterator();

		if (iterator.hasNext())
			while (true) {
				Entry<JsonString, JsonElement> next = iterator.next();

				builder.append("\n");
				builder.append(indentTab);
				builder.append(next.getKey().json());
				builder.append(":");
				builder.append(next.getValue().json(indentTab, tab));

				if (!iterator.hasNext())
					break;

				builder.append(",");
			}

		builder.append("}");

		return builder.toString();
	}

	@NotNull
	@Override
	public String json() {
		StringBuilder builder = new StringBuilder();

		builder.append("{");

		Iterator<Entry<JsonString, JsonElement>> iterator = this.entrySet().iterator();

		if (iterator.hasNext())
			while (true) {
				Entry<JsonString, JsonElement> next = iterator.next();

				builder.append(next.getKey().json());
				builder.append(":");
				builder.append(next.getValue().json());

				if (!iterator.hasNext())
					break;

				builder.append(",");
			}

		builder.append("}");

		return builder.toString();
	}

	@Nullable
	@Override
	public JsonElement query(@NotNull JsonPath path) {
		Objects.requireNonNull(path, "path");
		String name = path.getName();
		JsonPath next = path.getNext();

		JsonString string = new JsonString(name);

		if (next != null) {
			JsonElement e = this.get(string);

			if (e instanceof JsonStruct) {
				JsonStruct s = (JsonStruct) e;

				return s.query(next);
			}

			if (e == null) {
				if (path.isOptional())
					return null;

				throw new NoSuchElementException(
						"Missing property " + next
				);
			}

			if (path.isLenient())
				return null;

			throw new IllegalStateException(
					"Cannot access non struct property " + next
			);
		}

		return this.get(string);
	}

	/**
	 * Capture this object into a new object.
	 *
	 * @return a clone of this object.
	 * @since 0.3.0 ~2021.11.23
	 */
	@NotNull
	@Override
	public JsonObject clone() {
		try {
			JsonObject clone = (JsonObject) super.clone();
			clone.map = new LinkedHashMap<>(this.map);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@NotNull
	@Override
	public String toString() {
		return this.json();
	}

	// delegate

	@Override
	public void clear() {
		this.map.clear();
	}

	@Nullable
	@Override
	public JsonElement compute(@NotNull JsonString key, @NotNull BiFunction<? super @NotNull JsonString, ? super @Nullable JsonElement, ? extends @Nullable JsonElement> function) {
		return this.map.compute(key, function);
	}

	@Nullable
	@Override
	public JsonElement computeIfAbsent(@NotNull JsonString key, @NotNull Function<? super @NotNull JsonString, ? extends @Nullable JsonElement> function) {
		return this.map.computeIfAbsent(key, function);
	}

	@Nullable
	@Override
	public JsonElement computeIfPresent(@NotNull JsonString key, @NotNull BiFunction<? super @NotNull JsonString, ? super @NotNull JsonElement, ? extends @Nullable JsonElement> function) {
		return this.map.computeIfPresent(key, function);
	}

	@Override
	public boolean containsKey(@Nullable Object key) {
		return this.map.containsKey(key);
	}

	@Override
	public boolean containsValue(@Nullable Object value) {
		return this.map.containsValue(value);
	}

	@NotNull
	@Override
	public Set<@NotNull Entry<@NotNull JsonString, @NotNull JsonElement>> entrySet() {
		return this.map.entrySet();
	}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(@Nullable Object object) {
		return this == object || this.map.equals(object);
	}

	@Override
	public void forEach(@NotNull BiConsumer<? super @NotNull JsonString, ? super @NotNull JsonElement> action) {
		this.map.forEach(action);
	}

	@Nullable
	@Override
	public JsonElement get(@Nullable Object key) {
		return this.map.get(key);
	}

	@NotNull
	@Override
	public JsonElement getOrDefault(@Nullable Object key, @NotNull JsonElement defaultValue) {
		//noinspection SuspiciousMethodCalls
		return this.map.getOrDefault(key, defaultValue);
	}

	@Override
	public int hashCode() {
		return this.map.hashCode();
	}

	@Override
	public boolean isEmpty() {
		return this.map.isEmpty();
	}

	@NotNull
	@Override
	public Set<@NotNull JsonString> keySet() {
		return this.map.keySet();
	}

	@Nullable
	@Override
	public JsonElement merge(@NotNull JsonString key, @NotNull JsonElement value, @NotNull BiFunction<? super @NotNull JsonElement, ? super @NotNull JsonElement, ? extends @Nullable JsonElement> function) {
		return this.map.merge(key, value, function);
	}

	@Nullable
	@Override
	public JsonElement put(@NotNull JsonString key, @NotNull JsonElement value) {
		return this.map.put(key, value);
	}

	@Override
	public void putAll(@NotNull Map<? extends @NotNull JsonString, ? extends @NotNull JsonElement> map) {
		this.map.putAll(map);
	}

	@Nullable
	@Override
	public JsonElement putIfAbsent(@NotNull JsonString key, @NotNull JsonElement value) {
		return this.map.putIfAbsent(key, value);
	}

	@Override
	public boolean remove(@Nullable Object key, @Nullable Object value) {
		return this.map.remove(key, value);
	}

	@Nullable
	@Override
	public JsonElement remove(@Nullable Object key) {
		return this.map.remove(key);
	}

	@Nullable
	@Override
	public JsonElement replace(@NotNull JsonString key, @NotNull JsonElement value) {
		return this.map.replace(key, value);
	}

	@Override
	public boolean replace(@NotNull JsonString key, @NotNull JsonElement oldValue, @NotNull JsonElement newValue) {
		return this.map.replace(key, oldValue, newValue);
	}

	@Override
	public void replaceAll(@NotNull BiFunction<? super @NotNull JsonString, ? super @NotNull JsonElement, ? extends @NotNull JsonElement> function) {
		this.map.replaceAll(function);
	}

	@Override
	public int size() {
		return this.map.size();
	}

	@NotNull
	@Override
	public Collection<@NotNull JsonElement> values() {
		return this.map.values();
	}
}
