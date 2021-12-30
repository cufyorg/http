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
package org.cufy.http.body;

import org.cufy.http.Body;
import org.cufy.http.json.JsonElement;
import org.cufy.http.json.JsonObject;
import org.cufy.http.json.JsonPath;
import org.cufy.http.mime.Mime;
import org.cufy.http.mime.MimeSubtype;
import org.cufy.http.mime.MimeType;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A json body implementation of the interface {@link Body}.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.23
 */
@ApiStatus.Experimental
public class JsonBody extends Body {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 3939573043048617013L;

	/**
	 * The object backing this body.
	 *
	 * @since 0.3.0 ~2021.11.21
	 */
	@NotNull
	protected JsonObject object;

	/**
	 * Construct a new json body.
	 *
	 * @since 0.3.0 ~2021.11.26
	 */
	public JsonBody() {
		this.mime = new Mime(
				MimeType.APPLICATION,
				MimeSubtype.JSON
		);
		this.object = new JsonObject();
	}

	/**
	 * Construct a new json body with the given components.
	 *
	 * @param object the json object.
	 * @throws NullPointerException if the given {@code object} is null.
	 * @since 0.3.0 ~2021.11.21
	 */
	public JsonBody(@NotNull JsonObject object) {
		Objects.requireNonNull(object, "object");
		this.mime = new Mime(
				MimeType.APPLICATION,
				MimeSubtype.JSON
		);
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.object = object;
	}

	/**
	 * Construct a new json body with the given components.
	 *
	 * @param mime   the mime of the constructed body.
	 * @param object the json object.
	 * @throws NullPointerException if the given {@code object} is null.
	 * @since 0.3.0 ~2021.11.21
	 */
	public JsonBody(@Nullable Mime mime, @NotNull JsonObject object) {
		Objects.requireNonNull(object, "object");
		this.mime = mime;
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.object = object;
	}

	/**
	 * Construct a new json body with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new json body.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.3.0 ~2021.11.22
	 */
	public JsonBody(@NotNull Consumer<@NotNull JsonBody> builder) {
		Objects.requireNonNull(builder, "builder");
		this.mime = new Mime(
				MimeType.APPLICATION,
				MimeSubtype.JSON
		);
		this.object = new JsonObject();
		//noinspection ThisEscapedInObjectConstruction
		builder.accept(this);
	}

	/**
	 * Construct a new json body from copying the given {@code body}.
	 *
	 * @param body the body to copy.
	 * @return a new json body copy of the given {@code body}.
	 * @throws NullPointerException     if the given {@code body} is null.
	 * @throws IllegalArgumentException if the content of the given {@code body} is not a
	 *                                  valid json object.
	 * @throws IOError                  if any I/O occurs while reading the content of the
	 *                                  given {@code body}.
	 * @since 0.3.0 ~2021.11.24
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static JsonBody from(@NotNull Body body) {
		Objects.requireNonNull(body, "body");
		try (InputStream stream = body.openInputStream()) {
			String string = new String(stream.readAllBytes());
			JsonObject object = JsonObject.parse(string);

			return new JsonBody(
					body.getMime(),
					object
			);
		} catch (IOException e) {
			throw new IOError(e);
		}
	}

	/**
	 * Construct a new body with its value set from the given {@code source}.
	 *
	 * @param source the source of the constructed body.
	 * @return a new json body from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} is an invalid json
	 *                                  object.
	 * @since 0.3.0 ~2021.11.24
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static JsonBody parse(@NotNull @Language("json") String source) {
		return new JsonBody(JsonObject.parse(source));
	}

	@NotNull
	@Override
	public JsonBody clone() {
		JsonBody clone = (JsonBody) super.clone();
		if (this.mime != null)
			clone.mime = this.mime.clone();
		clone.object = this.object.clone();
		return clone;
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof JsonBody) {
			JsonBody body = (JsonBody) object;

			return Objects.equals(this.mime, body.mime) &&
				   Objects.equals(this.object, body.object);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.object.hashCode();
	}

	@NotNull
	@Override
	public InputStream openInputStream() {
		return new ByteArrayInputStream(
				this.object.json().getBytes(StandardCharsets.UTF_8)
		);
	}

	@NotNull
	@Override
	public String toString() {
		return this.object.json();
	}

	/**
	 * Return the element at the given {@code path}.
	 *
	 * @param path the path to return the element of.
	 * @return the element at the given {@code path}.
	 * @throws NullPointerException     if the given {@code path} is null.
	 * @throws IllegalArgumentException if one of the middle elements cannot access its
	 *                                  targeted sub-element due to the impossibility of
	 *                                  containing it in its structure. (e.g. accessing
	 *                                  array members with non-numeric keys)
	 * @throws IllegalStateException    if one of the middle elements exist but is not a
	 *                                  json struct.
	 * @since 0.3.0 ~2021.11.23
	 */
	@Nullable
	@Contract(pure = true)
	public JsonElement get(@NotNull String path) {
		return this.object.query(JsonPath.parse(path));
	}

	/**
	 * Assign the given {@code element} to the given {@code path}.
	 *
	 * @param path    the path to assign the element to.
	 * @param element the element to be assigned.
	 * @throws NullPointerException     if the given {@code path} or {@code element} is
	 *                                  null.
	 * @throws IllegalArgumentException if one of the middle elements cannot access its
	 *                                  targeted sub-element due to the impossibility of
	 *                                  containing it in its structure. (e.g. accessing
	 *                                  array members with non-numeric keys)
	 * @throws NoSuchElementException   if one of the middle elements does not exist.
	 * @throws IllegalStateException    if one of the middle elements exist but is not a
	 *                                  json struct.
	 * @since 0.3.0 ~2021.11.23
	 */
	@Contract(mutates = "this")
	public void put(@NotNull String path, @NotNull JsonElement element) {
		this.object.assign(JsonPath.parse(path), element);
	}

	/**
	 * Delete the element at the given {@code path}.
	 *
	 * @param path the path to be deleted.
	 * @throws NullPointerException          if the given {@code path} is null.
	 * @throws UnsupportedOperationException if query is unmodifiable.
	 * @throws IllegalArgumentException      if one of the middle elements cannot access
	 *                                       its targeted sub-element due to the
	 *                                       impossibility of containing it in its
	 *                                       structure. (e.g. accessing array members with
	 *                                       non-numeric keys)
	 * @throws NoSuchElementException        if one of the middle elements does not
	 *                                       exist.
	 * @throws IllegalStateException         if one of the middle elements exist but is
	 *                                       not a json struct.
	 * @since 0.3.0 ~2021.11.23
	 */
	@Contract(mutates = "this")
	public void remove(@NotNull String path) {
		this.object.delete(JsonPath.parse(path));
	}
}
