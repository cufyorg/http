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
 * @since 0.3.0 ~2022.12.26
 */
public final class MimeSubtype {
	/**
	 * <h3>AAC audio</h3>
	 * The mime subtype for {@code .aac} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#AUDIO}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String AAC = "acc";
	/**
	 * <h3>Any mime subtype</h3>
	 * The mime subtype for any kind of file.
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String ANY = "*";
	/**
	 * <h3>Windows OS/2 Bitmap Graphics</h3>
	 * The mime subtype for {@code .bmp} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#IMAGE}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String BMP = "bmp";
	/**
	 * <h3>Cascading Style Sheets (CSS)</h3>
	 * The mime subtype for {@code .css} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#TEXT}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String CSS = "css";
	/**
	 * <h3>Comma-separated values (CSV)</h3>
	 * The mime subtype for {@code .csv} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#TEXT}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String CSV = "csv";
	/**
	 * <h3>Multipart Form Data</h3>
	 * Form data multipart.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#MULTIPART}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String FORM_DATA = "form-data";
	/**
	 * <h3>Graphics Interchange Format (GIF)</h3>
	 * The mime subtype for {@code .gif} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#IMAGE}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String GIF = "gif";
	/**
	 * <h3>GZip Compressed Archive</h3>
	 * The mime subtype for {@code .gz} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#APPLICATION}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String GZIP = "gzip";
	/**
	 * <h3>HyperText Markup Language (HTML)</h3>
	 * The mime subtype for {@code .htm} and {@code .html} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#TEXT}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String HTML = "html";
	/**
	 * <h3>JavaScript</h3>
	 * The mime subtype for {@code .js} and {@code .mjs} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#APPLICATION}</li>
	 *     <li>{@link MimeType#TEXT}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String JAVASCRIPT = "javascript";
	/**
	 * <h3>Java Archive (JAR)</h3>
	 * The mime subtype for {@code .jar} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#APPLICATION}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String JAVA_ARCHIVE = "java-archive";
	/**
	 * <h3>JPEG images</h3>
	 * The mime subtype for {@code .jpeg} and {@code .jpg} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#IMAGE}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String JPEG = "jpeg";
	/**
	 * <h3>JSON format</h3>
	 * The mime subtype for {@code .json} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#APPLICATION}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String JSON = "json";
	/**
	 * <h3>Multipart Mixed</h3>
	 * Mixed multipart.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#MULTIPART}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String MIXED = "mixed";
	/**
	 * <h3>MP4 video</h3>
	 * The mime subtype for {@code .mp4} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#VIDEO}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String MP4 = "mp4";
	/**
	 * <h3>MPEG</h3>
	 * The mime subtype for {@code .mp3} and {@code .mpeg} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#AUDIO}</li>
	 *     <li>{@link MimeType#VIDEO}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String MPEG = "mpeg";
	/**
	 * <h3>Any kind of binary data</h3>
	 * The mime subtype for {@code .bin} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#APPLICATION}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String OCTET_STREAM = "octet-stream";
	/**
	 * <h3>OGG</h3>
	 * The mime subtype for {@code .oga} and {@code .ogv} and {@code ogx} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#AUDIO}</li>
	 *     <li>{@link MimeType#VIDEO}</li>
	 *     <li>{@link MimeType#APPLICATION}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String OGG = "ogg";
	/**
	 * <h3>Opus audio</h3>
	 * The mime subtype for {@code .opus} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#AUDIO}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String OPUS = "opus";
	/**
	 * <h3>OpenType font</h3>
	 * The mime subtype for {@code .otf} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#FONT}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String OTF = "otf";
	/**
	 * <h3>Adobe Portable Document Format (PDF)</h3>
	 * The mime subtype for {@code .pdf} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#APPLICATION}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String PDF = "pdf";
	/**
	 * <h3>Text, (generally ASCII or ISO 8859-n)</h3>
	 * The mime subtype for {@code .txt} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#TEXT}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String PLAIN = "plain";
	/**
	 * <h3>Portable Network Graphics</h3>
	 * The mime subtype for {@code .png} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#IMAGE}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String PNG = "png";
	/**
	 * <h3>Rich Text Format (RTF)</h3>
	 * The mime subtype for {@code .rtf} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#APPLICATION}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String RTF = "rtf";
	/**
	 * <h3>Scalable Vector Graphics (SVG)</h3>
	 * The mime subtype for {@code .svg} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#IMAGE}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String SVG_XML = "svg+xml";
	/**
	 * <h3>TrueType Font</h3>
	 * The mime subtype for {@code .ttf} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#FONT}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String TTF = "ttf";
	/**
	 * <h3>WEBM</h3>
	 * The mime subtype for {@code .weba} and {@code .webm} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#AUDIO}</li>
	 *     <li>{@link MimeType#VIDEO}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String WEBM = "webm";
	/**
	 * <h3>WEBP image</h3>
	 * The mime subtype for {@code .webp} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#IMAGE}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String WEBP = "webp";
	/**
	 * <h3>Web Open Font Format (WOFF)</h3>
	 * The mime subtype for {@code .woff} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#FONT}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String WOFF = "woff";
	/**
	 * <h3>Web Open Font Format (WOFF)</h3>
	 * The mime subtype for {@code .woff2} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#FONT}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String WOFF2 = "woff2";
	/**
	 * <h3>XML</h3>
	 * The mime subtype for {@code .xml} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#APPLICATION}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String XML = "xml";
	/**
	 * <h3>URL ENCODED</h3>
	 * URL Encoded body.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#APPLICATION}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String X_WWW_FORM_URLENCODED = "x-www-form-urlencoded";
	/**
	 * <h3>ZIP archive</h3>
	 * The mime subtype for {@code .zip} files.
	 * <br>
	 * Use these mime types with this:
	 * <ul>
	 *     <li>{@link MimeType#APPLICATION}</li>
	 * </ul>
	 *
	 * @since 0.3.0 ~2022.12.26
	 */
	public static final String ZIP = "zip";

	/**
	 * Utility classes shall have no instances.
	 *
	 * @throws AssertionError when called.
	 * @since 0.3.0 ~2022.12.26
	 */
	private MimeSubtype() {
		throw new AssertionError("No instance for you!");
	}

	/**
	 * Return the given {@code source} if it is a valid mime subtype. Otherwise, throw an
	 * exception.
	 *
	 * @param source the source to return.
	 * @return the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  MimeRegExp#SUB_TYPE}.
	 * @since 0.3.0 ~2022.12.26
	 */
	@NotNull
	@Contract(value = "_->param1", pure = true)
	public static String parse(@NotNull @Pattern(MimeRegExp.SUB_TYPE) String source) {
		Objects.requireNonNull(source, "source");
		if (!MimePattern.TYPE.matcher(source).matches())
			throw new IllegalArgumentException("invalid mime subtype: " + source);
		return source;
	}
}
