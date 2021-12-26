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
package org.cufy.http.json.token;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.Objects;

/**
 * A reader that can be a json token source.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.24
 */
@ApiStatus.Internal
public class JsonTokenSource extends Reader {
	/**
	 * The reader backing this.
	 *
	 * @since 0.3.0 ~2021.11.24
	 */
	protected final Reader reader;

	/**
	 * The current index.
	 *
	 * @since 0.3.0 ~2021.11.24
	 */
	protected long index;
	/**
	 * The last mark.
	 *
	 * @since 0.3.0 ~2021.11.25
	 */
	protected long mark;

	/**
	 * Construct a new token source with the given {@code reader}.
	 *
	 * @param reader the reader to read from.
	 * @throws NullPointerException if the given {@code reader} is null.
	 * @since 0.3.0 ~2021.11.24
	 */
	public JsonTokenSource(@NotNull Reader reader) {
		Objects.requireNonNull(reader, "reader");
		this.reader = reader.markSupported() ?
					  reader :
					  new BufferedReader(reader);
	}

	/**
	 * Construct a new token source with the given {@code lock} and {@code reader}.
	 *
	 * @param lock   the lock to be used.
	 * @param reader the reader to read from.
	 * @throws NullPointerException if the given {@code lock} or {@code reader} is null.
	 * @since 0.3.0 ~2021.11.24
	 */
	public JsonTokenSource(@NotNull Object lock, @NotNull Reader reader) {
		super(Objects.requireNonNull(lock, "lock"));
		Objects.requireNonNull(reader, "reader");
		this.reader = reader.markSupported() ?
					  reader :
					  new BufferedReader(reader);
	}

	@Override
	public void close() throws IOException {
		this.reader.close();
	}

	@Override
	public void mark(int readAheadLimit) throws IOException {
		this.reader.mark(readAheadLimit);
		this.mark = this.index;
	}

	@Override
	public boolean markSupported() {
		return this.reader.markSupported();
	}

	@Override
	public int read(@NotNull CharBuffer target) throws IOException {
		int read = this.reader.read(target);
		if (read > 0)
			this.index += read;
		return read;
	}

	@Override
	public int read() throws IOException {
		int read = this.reader.read();
		if (read > -1)
			this.index++;
		return read;
	}

	@Override
	public int read(char[] cbuf) throws IOException {
		int read = super.read(cbuf);
		if (read > 0)
			this.index += read;
		return read;
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		int read = this.reader.read(cbuf, off, len);
		if (read > 0)
			this.index += read;
		return read;
	}

	@Override
	public boolean ready() throws IOException {
		return this.reader.ready();
	}

	@Override
	public void reset() throws IOException {
		this.reader.reset();
		this.index = this.mark;
	}

	@Override
	public long skip(long n) throws IOException {
		long skip = this.reader.skip(n);
		if (skip > 0)
			this.index += skip;
		return skip;
	}

	/**
	 * Return the index of the next character.
	 *
	 * @return the index of the next character.
	 * @since 0.3.0 ~2021.11.24
	 */
	@Contract(pure = true)
	public long nextIndex() {
		return this.index;
	}
}
