/*
 *	Copyright 2021 Cufy and ProgSpaceSA
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
package org.cufy.http.mime;

import org.cufy.http.internal.syntax.MimePattern;
import org.cufy.http.internal.syntax.MimeRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * <b>Constant</b> (No Encode)
 * <br>
 * The mime's subtype.
 *
 * @author LSafer
 * @version 0.3.0
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types">
 * 		Common Mime Types
 * 		</a>
 * @see <a href="https://www.iana.org/assignments/media-types/media-types.xhtml#application">
 * 		Media Types
 * 		</a>
 * @since 0.3.0 ~2022.12.26
 */
public final class MimeType {
	/**
	 * The {@code Application} mime type.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String APPLICATION = "application";
	/**
	 * The {@code Audio} mime type.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String AUDIO = "audio";
	/**
	 * The {@code Font} mime type.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String FONT = "font";
	/**
	 * The {@code Image} mime type.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String IMAGE = "image";
	/**
	 * The {@code Message} mime type.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String MESSAGE = "message";
	/**
	 * The {@code Model} mime type.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String MODEL = "model";
	/**
	 * The {@code Multipart} mime type.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String MULTIPART = "multipart";
	/**
	 * The {@code Text} mime type.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String TEXT = "text";
	/**
	 * The {@code Video} mime type.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String VIDEO = "video";

	/**
	 * Utility classes shall have no instances.
	 *
	 * @throws AssertionError when called.
	 * @since 0.3.0 ~2022.12.26
	 */
	private MimeType() {
		throw new AssertionError("No instance for you!");
	}

	/**
	 * Return the given {@code source} if it is a valid mime type. Otherwise, throw an
	 * exception.
	 *
	 * @param source the source to return.
	 * @return the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  MimeRegExp#TYPE}.
	 * @since 0.3.0 ~2022.12.26
	 */
	@NotNull
	@Contract(value = "_->param1", pure = true)
	public static String parse(@NotNull @Pattern(MimeRegExp.TYPE) String source) {
		Objects.requireNonNull(source, "source");
		if (!MimePattern.TYPE.matcher(source).matches())
			throw new IllegalArgumentException("invalid mime type: " + source);
		return source;
	}
}
