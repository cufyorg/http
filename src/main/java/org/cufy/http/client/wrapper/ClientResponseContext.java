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
package org.cufy.http.client.wrapper;

import org.cufy.http.Endpoint;
import org.cufy.http.concurrent.wrapper.TaskContext;
import org.cufy.http.pipeline.wrapper.PipelineContext;
import org.cufy.http.wrapper.ResponseContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * An extended version of the interface {@link ResponseContext} containing additional
 * client side properties.
 *
 * @param <E> the type of the endpoint.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.12
 */
public interface ClientResponseContext<E extends Endpoint> extends
		ResponseContext<E, ClientRequestContext<E>, ClientResponseContext<E>>,
		ClientExtension<ClientRequestContext<E>, ClientResponseContext<E>, ClientResponseContext<E>>,
		PipelineContext<ClientResponseContext<E>, ClientResponseContext<E>>,
		TaskContext<ClientResponseContext<E>> {
	/**
	 * A shortcut for {@link #req()}.{@link ClientRequestContext#connect}.
	 *
	 * @return this.
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	@Contract("->this")
	default ClientResponseContext<E> connect() {
		this.req().connect();
		return this;
	}
}
