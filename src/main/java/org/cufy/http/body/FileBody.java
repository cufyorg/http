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
import org.cufy.http.internal.util.StreamUtil;
import org.cufy.http.mime.Mime;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A body implementation that reads from a file.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.18
 */
public class FileBody extends Body {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 5200102396365014555L;

	/**
	 * The file of this body.
	 *
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	protected File file;

	/**
	 * Construct a new file body.
	 *
	 * @since 0.3.0 ~2021.11.26
	 */
	public FileBody() {
		this.mime = null;
		this.file = new File("");
	}

	/**
	 * Construct a new file body with the given parameters.
	 *
	 * @param file the file.
	 * @throws NullPointerException if the given {@code file} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	public FileBody(@NotNull File file) {
		Objects.requireNonNull(file, "file");
		this.mime = null;
		this.file = file;
	}

	/**
	 * Construct a new file body with the given parameters.
	 *
	 * @param mime the mime of the constructed body.
	 * @param file the file.
	 * @throws NullPointerException if the given {@code file} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	public FileBody(@Nullable Mime mime, @NotNull File file) {
		Objects.requireNonNull(file, "file");
		this.mime = mime;
		this.file = file;
	}

	/**
	 * Construct a new file body with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new file body.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	public FileBody(@NotNull Consumer<@NotNull FileBody> builder) {
		Objects.requireNonNull(builder, "builder");
		this.mime = null;
		this.file = new File("");
		//noinspection ThisEscapedInObjectConstruction
		builder.accept(this);
	}

	/**
	 * Construct a new body with its file set from the given {@code filename}.
	 *
	 * @param filename the name of the file of the constructed body.
	 * @return a new file body from the given {@code filename}.
	 * @throws NullPointerException if the given {@code filename} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static FileBody from(@NotNull String filename) {
		return new FileBody(new File(filename));
	}

	@NotNull
	@Override
	public FileBody clone() {
		FileBody clone = (FileBody) super.clone();
		if (this.mime != null)
			clone.mime = this.mime.clone();
		return clone;
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof FileBody) {
			FileBody body = (FileBody) object;

			return Objects.equals(this.mime, body.mime) &&
				   Objects.equals(this.file, body.file);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.file.hashCode();
	}

	@NotNull
	@Override
	public InputStream openInputStream() {
		try {
			return new FileInputStream(this.file);
		} catch (FileNotFoundException e) {
			throw new IOError(e);
		}
	}

	@NotNull
	@Override
	public String toString() {
		try (InputStream in = new FileInputStream(this.file)) {
			return new String(
					StreamUtil.readAllBytes(in),
					StandardCharsets.UTF_8
			);
		} catch (IOException e) {
			throw new IOError(e);
		}
	}

	/**
	 * Return the file object.
	 *
	 * @return the file.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(pure = true)
	public File getFile() {
		return this.file;
	}

	/**
	 * Set the file to the given {@code file}.
	 *
	 * @param file the file to be set.
	 * @return this.
	 * @throws NullPointerException if the given {@code file} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public FileBody setFile(@NotNull File file) {
		Objects.requireNonNull(file, "file");
		this.file = file;
		return this;
	}
}
