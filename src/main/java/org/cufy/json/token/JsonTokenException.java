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
package org.cufy.json.token;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.security.PrivilegedActionException;

/**
 * An exception thrown to indicate an exception while parsing a json token.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.24
 */
public class JsonTokenException extends IllegalArgumentException {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 5223107672862868233L;

	/**
	 * The index of the error.
	 */
	protected final long index;

	/**
	 * Constructs a json exception with no detail message.
	 *
	 * @param index the index where the error occur.
	 * @since 0.3.0 ~2021.11.24
	 */
	public JsonTokenException(long index) {
		this.index = index;
	}

	/**
	 * Constructs a json exception with the specified detail message.
	 *
	 * @param message the detail message.
	 * @param index   the index where the error occur.
	 * @since 0.3.0 ~2021.11.24
	 */
	public JsonTokenException(@Nullable String message, long index) {
		super(message);
		this.index = index;
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.
	 * <br>
	 * Note that the detail message associated with {@code cause} is
	 * <i>not</i> automatically incorporated in this exception's detail
	 * message.
	 *
	 * @param message the detail message (which is saved for later retrieval by the {@link
	 *                Throwable#getMessage()} method).
	 * @param cause   the cause (which is saved for later retrieval by the {@link
	 *                Throwable#getCause()} method).  (A {@code null} value is permitted,
	 *                and indicates that the cause is nonexistent or unknown.)
	 * @param index   the index where the error occur.
	 * @since 0.3.0 ~2021.11.24
	 */
	public JsonTokenException(@Nullable String message, @Nullable Throwable cause, long index) {
		super(message, cause);
		this.index = index;
	}

	/**
	 * Constructs a new exception with the specified cause and a detail message of {@code
	 * (cause==null ? null : cause.toString())} (which typically contains the class and
	 * detail message of {@code cause}). This constructor is useful for exceptions that
	 * are little more than wrappers for other throwables (for example, {@link
	 * PrivilegedActionException}).
	 *
	 * @param cause the cause (which is saved for later retrieval by the {@link
	 *              Throwable#getCause()} method).  (A {@code null} value is permitted,
	 *              and indicates that the cause is nonexistent or unknown.)
	 * @param index the index where the error occur.
	 * @since 0.3.0 ~2021.11.24
	 */
	public JsonTokenException(@Nullable Throwable cause, long index) {
		super(cause);
		this.index = index;
	}

	/**
	 * Return the index the error occurred at.
	 *
	 * @return the index.
	 * @since 0.3.0 ~2021.11.24
	 */
	@Contract(pure = true)
	public long index() {
		return this.index;
	}
}
