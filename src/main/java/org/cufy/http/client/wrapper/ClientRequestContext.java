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
import org.cufy.http.client.ClientTask;
import org.cufy.http.concurrent.wrapper.TaskContext;
import org.cufy.http.wrapper.RequestContext;
import org.cufy.http.pipeline.wrapper.PipelineContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * An extended version of the interface {@link RequestContext} containing additional client side
 * properties.
 *
 * @param <E> the type of the endpoint.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.12
 */
public interface ClientRequestContext<E extends Endpoint> extends
		RequestContext<E, ClientResponseContext<E>, ClientRequestContext<E>>,
		ClientExtension<ClientRequestContext<E>, ClientResponseContext<E>, ClientRequestContext<E>>,
		PipelineContext<ClientResponseContext<E>, ClientRequestContext<E>>,
		TaskContext<ClientRequestContext<E>> {
	/**
	 * Perform the connection.
	 *
	 * @return this.
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	@Contract("->this")
	default ClientRequestContext<E> connect() {
		this.perform(ClientTask.CONNECT);
		return this;
	}
}
