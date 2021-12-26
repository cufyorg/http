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
import org.cufy.http.Headers;
import org.cufy.internal.syntax.HttpRegExp;
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A multipart body implementation of the interface {@link Body}.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.18
 */
public class MultipartBody extends Body {
	/**
	 * The typical content type for a multipart body.
	 *
	 * @since 0.3.0 ~2021.11.18
	 */
	@Pattern(HttpRegExp.FIELD_VALUE)
	public static final String CONTENT_TYPE = "multipart/mixed";
	/**
	 * A regex catching most typical multipart body mimes.
	 *
	 * @since 0.3.0 ~2021.11.24
	 */
	@Language("RegExp")
	public static final String CONTENT_TYPE_PATTERN = "^multipart\\/.*$";

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 5200102396365014555L;

	/**
	 * The boundary string.
	 *
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Pattern(HttpRegExp.FIELD_VALUE)
	protected String boundary;
	/**
	 * The parts list.
	 *
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	protected List<@NotNull BodyPart> parts;

	/**
	 * Construct a new multipart body.
	 *
	 * @since 0.3.0 ~2021.11.26
	 */
	public MultipartBody() {
		this.contentType = MultipartBody.CONTENT_TYPE;
		this.boundary = UUID.randomUUID().toString();
		this.parts = new LinkedList<>();
	}

	/**
	 * Construct a new multipart body with the given parameters.
	 *
	 * @param parts the parts list.
	 * @throws NullPointerException if the given {@code parts} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	public MultipartBody(@NotNull List<@NotNull BodyPart> parts) {
		Objects.requireNonNull(parts, "values");
		this.contentType = MultipartBody.CONTENT_TYPE;
		this.boundary = UUID.randomUUID().toString();
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.parts = parts;
	}

	/**
	 * Construct a new multipart body with the given parameters.
	 *
	 * @param contentType the content-type of the constructed body.
	 * @param boundary    the boundary string.
	 * @param parts       the parts list.
	 * @throws NullPointerException if the given {@code boundary} or {@code parts} is
	 *                              null.
	 * @since 0.3.0 ~2021.11.18
	 */
	public MultipartBody(@Nullable @Pattern(HttpRegExp.FIELD_VALUE) String contentType, @NotNull @Pattern(HttpRegExp.FIELD_VALUE) String boundary, @NotNull List<@NotNull BodyPart> parts) {
		Objects.requireNonNull(boundary, "boundary");
		Objects.requireNonNull(parts, "values");
		this.contentType = contentType;
		this.boundary = boundary;
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.parts = parts;
	}

	/**
	 * Construct a new multipart body with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new multipart body.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	public MultipartBody(@NotNull Consumer<@NotNull MultipartBody> builder) {
		Objects.requireNonNull(builder, "builder");
		this.contentType = MultipartBody.CONTENT_TYPE;
		this.boundary = UUID.randomUUID().toString();
		this.parts = new LinkedList<>();
		//noinspection ThisEscapedInObjectConstruction
		builder.accept(this);
	}

	@NotNull
	@Override
	public MultipartBody clone() {
		MultipartBody clone = (MultipartBody) super.clone();
		clone.parts = this.parts
				.stream()
				.map(BodyPart::clone)
				.collect(Collectors.toList());
		return clone;
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == null)
			return true;
		if (object instanceof MultipartBody) {
			MultipartBody body = (MultipartBody) object;

			return Objects.equals(this.contentType, body.contentType) &&
				   Objects.equals(this.boundary, body.boundary) &&
				   Objects.equals(this.parts, body.parts);
		}

		return false;
	}

	@NotNull
	@Pattern(HttpRegExp.FIELD_VALUE)
	@Override
	public String getContentType() {
		return this.contentType + "; boundary=" + this.boundary;
	}

	@Override
	public int hashCode() {
		return this.parts.hashCode();
	}

	@NotNull
	@Override
	public InputStream openInputStream() {
		List<InputStream> streams = new ArrayList<>();

		byte[] partStartBytes = ("--" + this.boundary + "\r\n")
				.getBytes(StandardCharsets.UTF_8);
		byte[] partEndBytes = "\r\n"
				.getBytes(StandardCharsets.UTF_8);
		byte[] endBytes = ("--" + this.boundary + "---")
				.getBytes(StandardCharsets.UTF_8);

		for (BodyPart part : this.parts) {
			Headers headers = part.getHeaders().clone();
			Body body = part.getBody();

			// if not set, set Content-Type header from the body content type
			if (body != null && headers.get(Headers.CONTENT_TYPE) == null) {
				String contentType = body.getContentType();

				if (contentType != null)
					headers.put(Headers.CONTENT_TYPE, contentType);
			}

			byte[] headersBytes = (headers + "\r\n").getBytes(StandardCharsets.UTF_8);

			streams.add(new ByteArrayInputStream(partStartBytes));
			streams.add(new ByteArrayInputStream(headersBytes));
			if (body != null)
				streams.add(body.openInputStream());
			streams.add(new ByteArrayInputStream(partEndBytes));
		}

		streams.add(new ByteArrayInputStream(endBytes));

		return new SequenceInputStream(Collections.enumeration(streams));
	}

	@NotNull
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		String partStartString = "--" + this.boundary + "\r\n";
		String partEndString = "\r\n";
		String endString = "--" + this.boundary + "--";

		for (BodyPart part : this.parts) {
			Headers headers = part.getHeaders().clone();
			Body body = part.getBody();

			// if not set, set Content-Type header from the body content type
			if (body != null && headers.get(Headers.CONTENT_TYPE) == null) {
				String contentType = body.getContentType();

				if (contentType != null)
					headers.put(Headers.CONTENT_TYPE, contentType);
			}

			String headersString = headers + "\r\n";

			builder.append(partStartString);
			builder.append(headersString);
			if (body != null)
				builder.append(body);
			builder.append(partEndString);
		}

		builder.append(endString);

		return builder.toString();
	}

	/**
	 * Add the given {@code part} to the end of the parts list.
	 *
	 * @param part the part to be added.
	 * @throws NullPointerException if the given {@code part} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@Contract(mutates = "this")
	public void add(@NotNull BodyPart part) {
		Objects.requireNonNull(part, "part");
		this.parts.add(part);
	}

	/**
	 * Get the part at the given {@code index}.
	 *
	 * @param index the index of the part to be returned.
	 * @return the part at the given {@code index}. Or {@code null} if no such part.
	 * @throws IllegalArgumentException if the given {@code index} is negative.
	 * @since 0.3.0 ~2021.11.18
	 */
	@Nullable
	@Contract(pure = true)
	public BodyPart get(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
		//noinspection ConstantConditions
		if (index < 0)
			throw new IllegalArgumentException("index < 0");
		return index < this.parts.size() ? this.parts.get(index) : null;
	}

	/**
	 * Return the boundary string.
	 *
	 * @return the boundary string.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Pattern(HttpRegExp.FIELD_VALUE)
	@Contract(pure = true)
	public String getBoundary() {
		return this.boundary;
	}

	/**
	 * Set the {@code index}-th part to be the given {@code part}.
	 *
	 * @param index the index of the part.
	 * @param part  the part to be set.
	 * @throws NullPointerException     if the given {@code part} is null.
	 * @throws IllegalArgumentException if the given {@code index} is negative.
	 * @since 0.3.0 ~2021.11.18
	 */
	@Contract(mutates = "this")
	public void put(@Range(from = 0, to = Integer.MAX_VALUE) int index, @NotNull BodyPart part) {
		//noinspection ConstantConditions
		if (index < 0)
			throw new IllegalArgumentException("index < 0");

		int size = this.parts.size();

		IntStream.range(0, Math.max(0, index + 1 - size))
				 .forEach(i -> this.parts.add(new BodyPart()));

		this.parts.set(index, part);
	}

	/**
	 * Remove the {@code index}-th part and the parts after it.
	 *
	 * @param index the index of the part.
	 * @throws IllegalArgumentException if the given {@code index} is negative.
	 * @since 0.3.0 ~2021.11.18
	 */
	@Contract(mutates = "this")
	public void remove(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
		//noinspection ConstantConditions
		if (index < 0)
			throw new IllegalArgumentException("index < 0");

		int size = this.parts.size();

		this.parts.subList(Math.min(index, size), size).clear();
	}

	/**
	 * Set the boundary string to the given {@code boundary}.
	 *
	 * @param boundary the new boundary string.
	 * @throws NullPointerException if the given {@code boundary} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@Contract(mutates = "this")
	public void setBoundary(@NotNull @Pattern(HttpRegExp.FIELD_VALUE) String boundary) {
		Objects.requireNonNull(boundary, "boundary");
		this.boundary = boundary;
	}
}
