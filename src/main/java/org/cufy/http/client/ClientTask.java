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
package org.cufy.http.client;

import org.cufy.http.client.wrapper.ClientRequestContext;
import org.cufy.http.client.wrapper.ClientResponseContext;
import org.cufy.http.concurrent.Task;
import org.cufy.http.pipeline.Next;
import org.cufy.http.pipeline.Pipe;

/**
 * Common client tasks.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2022.12.26
 */
public final class ClientTask {
	/**
	 * A performance that performs the necessary steps to start a connection.
	 *
	 * @since 0.3.0 ~2021.12.23
	 */
	public static final Task<ClientRequestContext<?>> CONNECT = (req, callback) -> {
		ClientEngine engine = req.engine();
		ClientResponseContext res = req.res();
		Pipe pipe = req.pipe();
		Next next = req.next();

		engine.connect(req, error -> {
			if (error != null) {
				try {
					next.invoke(error);
				} finally {
					callback.run();
				}
				return;
			}

			try {
				pipe.invoke(res, next);
			} catch (Throwable throwable) {
				next.invoke(throwable);
			} finally {
				callback.run();
			}
		});
	};

	/**
	 * Utility classes shall have no instances.
	 *
	 * @throws AssertionError when called.
	 * @since 0.3.0 ~2021.11.17
	 */
	private ClientTask() {
		throw new AssertionError("No instance for you!");
	}
}
