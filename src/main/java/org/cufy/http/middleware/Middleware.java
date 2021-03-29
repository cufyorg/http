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
package org.cufy.http.middleware;

import org.cufy.http.connect.Caller;

/**
 * A middleware that has callbacks to be added to a caller. The middleware is just an
 * object that inject its callbacks on a caller when the {@link
 * Caller#middleware(Middleware)} get invoked with it as the parameter.
 *
 * @param <C> the type of callers this middleware accepts. (optional)
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.23
 */
@FunctionalInterface
public interface Middleware<C extends Caller<C>> {
	/**
	 * Inject this middleware to the given {@code caller}.
	 *
	 * @param caller the caller to inject this middleware to.
	 * @throws NullPointerException     if the given {@code caller} is null.
	 * @throws IllegalArgumentException if this middleware cannot be injected to the given
	 *                                  {@code caller} for some aspect of it.
	 * @since 0.0.1 ~2021.03.23
	 */
	void inject(Caller<C> caller);
}
