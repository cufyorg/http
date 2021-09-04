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
package org.cufy.http.middleware.cache;

import org.cufy.http.cache.Cache;
import org.cufy.http.request.Request;
import org.cufy.http.response.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * A cache repository that stores the caches on a pre-specified file.
 *
 * @author LSafer
 * @version 0.2.11
 * @since 0.2.11 ~2021.09.04
 */
public class FileCacheRepository implements CacheRepository {
	/**
	 * The file of the repository.
	 *
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	protected final File file;
	/**
	 * The cache validator.
	 *
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	protected final CacheValidator validator;

	/**
	 * Construct a new cache repository that stores the caches at the given {@code file}.
	 *
	 * @param file      the file for the repository to store caches on.
	 * @param validator the validator.
	 * @throws NullPointerException if the given {@code file} is null.
	 * @since 0.2.11 ~2021.09.04
	 */
	public FileCacheRepository(@NotNull File file, @NotNull CacheValidator validator) {
		Objects.requireNonNull(file, "file");
		this.file = file;
		this.validator = validator;
	}

	@Override
	public boolean cache(@NotNull Request request, @NotNull Response response) {
		Objects.requireNonNull(request, "request");
		Objects.requireNonNull(response, "response");
		List<Cache> caches = new ArrayList<>(this.read());

		//loop to clean
		Iterator<Cache> iterator = caches.iterator();
		while (iterator.hasNext()) {
			Cache cache = iterator.next();

			switch (this.validator.match(request, cache)) {
				case EXPIRED:
					//remove expired cache
					iterator.remove();
			}
		}

		//add the cache
		caches.add(Cache.cache(request, response, Instant.now()));

		//write updated caches
		return this.write(caches);
	}

	@Nullable
	@Override
	public Cache find(@NotNull Request request) {
		Objects.requireNonNull(request, "request");
		List<Cache> caches = this.read();

		//loop to find valid cache + clean
		Iterator<Cache> iterator = caches.iterator();
		while (iterator.hasNext()) {
			Cache cache = iterator.next();

			switch (this.validator.match(request, cache)) {
				case VALID:
					//write partially cleaned catches
					this.write(caches);
					return cache;
				case EXPIRED:
					//remove expired cache
					iterator.remove();
			}
		}

		//no valid cache was found
		return null;
	}

	/**
	 * Read all the caches in the backing file.
	 *
	 * @return the caches in the backing file.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	protected List<Cache> read() {
		try (
				FileInputStream fis = new FileInputStream(this.file);
				ObjectInputStream ois = new ObjectInputStream(fis)
		) {
			//noinspection unchecked
			return (List<Cache>) ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			return new ArrayList<>();
		}
	}

	/**
	 * Write the given {@code caches} list to the backing file.
	 *
	 * @param caches the caches list to be written.
	 * @return true, if the writing succeed.
	 * @throws NullPointerException if the given {@code caches} is null.
	 * @since 0.2.11 ~2021.09.04
	 */
	protected boolean write(@NotNull List<Cache> caches) {
		Objects.requireNonNull(caches, "caches");
		try (
				FileOutputStream fos = new FileOutputStream(this.file);
				ObjectOutputStream oos = new ObjectOutputStream(fos)
		) {
			oos.writeObject(caches);
			return true;
		} catch (IOException ignored) {
			return false;
		}
	}
}
