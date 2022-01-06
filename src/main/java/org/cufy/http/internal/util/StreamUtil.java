/*
 *	Copyright 2021-2022 Cufy and ProgSpaceSA
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
package org.cufy.http.internal.util;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Internal utilities to deal with streams.
 *
 * @author LSafer
 * @version 1.0.0
 * @since 1.0.0 ~2022.01.07
 */
@ApiStatus.Internal
public final class StreamUtil {
	/**
	 * Utility classes shall have no instances.
	 *
	 * @throws AssertionError when called.
	 * @since 1.0.0 ~2022.01.07
	 */
	private StreamUtil() {
		throw new AssertionError("No instance for you!");
	}

	/**
	 * A utility function to read all the bytes in a particular input stream. The stream
	 * will not be closed automatically.
	 *
	 * @param is the input stream.
	 * @return the bytes from reading the input stream.
	 * @throws IOException if any I/O exception occurs while reading the input stream.
	 * @since 1.0.0 ~2022.01.07
	 */
	@Contract(mutates = "param")
	public static byte @NotNull [] readAllBytes(@NotNull InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		//noinspection CheckForOutOfMemoryOnLargeArrayAllocation
		byte[] buffer = new byte[8192];

		while (true) {
			int read = is.read(buffer, 0, buffer.length);

			if (read < 0)
				break;

			if (read == 0)
				continue;

			baos.write(buffer, 0, read);
		}

		return baos.toByteArray();
	}
}
