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
package org.cufy.http.concurrent;

import org.jetbrains.annotations.NotNull;

/**
 * A task is a function that when invoked, it performs some task in another thread and
 * calls the callback given to it when it's done.
 *
 * @param <T> the type of the parameter.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.23
 */
@FunctionalInterface
public interface Task<T> {
	/**
	 * Perform this task with the given {@code parameter} in another thread. Then, call
	 * the given {@code callback} when that task is done.
	 *
	 * @param parameter the parameter to perform this task with.
	 * @param callback  the callback to be called when the task is done.
	 * @throws NullPointerException if the given {@code parameter} or {@code callback} is
	 *                              null.
	 * @since 0.3.0 ~2021.12.23
	 */
	void start(@NotNull T parameter, @NotNull Runnable callback);
}
