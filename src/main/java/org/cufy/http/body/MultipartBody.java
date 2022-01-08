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
package org.cufy.http.body;

import org.cufy.http.Body;
import org.cufy.http.Headers;
import org.cufy.http.mime.Mime;
import org.cufy.http.mime.MimeParameters;
import org.cufy.http.mime.MimeSubtype;
import org.cufy.http.mime.MimeType;
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
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 5200102396365014555L;

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
		this.mime = new Mime(
				MimeType.MULTIPART,
				MimeSubtype.FORM_DATA,
				new MimeParameters(m -> m.put(
						"boundary", UUID.randomUUID().toString()
				))
		);
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
		this.mime = new Mime(
				MimeType.MULTIPART,
				MimeSubtype.FORM_DATA,
				new MimeParameters(m -> m.put(
						"boundary", UUID.randomUUID().toString()
				))
		);
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.parts = parts;
	}

	/**
	 * Construct a new multipart body with the given parameters.
	 *
	 * @param mime  the mime of the constructed body.
	 * @param parts the parts list.
	 * @throws NullPointerException if the given {@code parts} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	public MultipartBody(@Nullable Mime mime, @NotNull List<@NotNull BodyPart> parts) {
		Objects.requireNonNull(parts, "parts");
		this.mime = mime;
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
		this.mime = new Mime(
				MimeType.MULTIPART,
				MimeSubtype.FORM_DATA,
				new MimeParameters(m -> m.put(
						"boundary", UUID.randomUUID().toString()
				))
		);
		this.parts = new LinkedList<>();
		//noinspection ThisEscapedInObjectConstruction
		builder.accept(this);
	}

	@NotNull
	@Override
	public MultipartBody clone() {
		MultipartBody clone = (MultipartBody) super.clone();
		if (this.mime != null)
			clone.mime = this.mime.clone();
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

			return Objects.equals(this.parts, body.parts) &&
				   Objects.equals(this.mime, body.mime);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.parts.hashCode();
	}

	@NotNull
	@Override
	public InputStream openInputStream() {
		List<InputStream> streams = new ArrayList<>();

		String boundaryString =
				this.mime == null ?
				UUID.randomUUID().toString() :
				this.mime.getMimeParameters().get("boundary");
		byte[] partStartBytes = ("--" + boundaryString + "\r\n")
				.getBytes(StandardCharsets.UTF_8);
		byte[] partEndBytes = "\r\n"
				.getBytes(StandardCharsets.UTF_8);
		byte[] endBytes = ("--" + boundaryString + "---")
				.getBytes(StandardCharsets.UTF_8);

		for (BodyPart part : this.parts) {
			Headers headers = part.getHeaders().clone();
			Body body = part.getBody();

			// if not set, set Content-Type header from the body content type
			if (body != null && headers.get(Headers.CONTENT_TYPE) == null) {
				Mime mime = body.getMime();

				if (mime != null) {
					String mimeString = mime.toString();

					headers.put(Headers.CONTENT_TYPE, mimeString);
				}
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

		String boundaryString =
				this.mime == null ?
				UUID.randomUUID().toString() :
				this.mime.getMimeParameters().get("boundary");
		String partStartString = "--" + boundaryString + "\r\n";
		String partEndString = "\r\n";
		String endString = "--" + boundaryString + "--";

		for (BodyPart part : this.parts) {
			Headers headers = part.getHeaders().clone();
			Body body = part.getBody();

			// if not set, set Content-Type header from the body content type
			if (body != null && headers.get(Headers.CONTENT_TYPE) == null) {
				Mime mime = body.getMime();

				if (mime != null) {
					String mimeString = mime.toString();

					headers.put(Headers.CONTENT_TYPE, mimeString);
				}
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
}
