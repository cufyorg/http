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
import org.cufy.http.concurrent.wrapper.Performer;
import org.cufy.http.wrapper.Req;
import org.cufy.http.pipeline.wrapper.Pipeline;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * An extended version of the interface {@link Req} containing additional client side
 * properties.
 *
 * @param <E> the type of the endpoint.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.12
 */
public interface ClientReq<E extends Endpoint> extends
		Req<E, ClientRes<E>, ClientReq<E>>,
		ClientExtension<ClientReq<E>, ClientRes<E>, ClientReq<E>>,
		Pipeline<ClientRes<E>, ClientReq<E>>,
		Performer<ClientReq<E>> {
	/**
	 * Perform the connection.
	 *
	 * @return this.
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	@Contract("->this")
	default ClientReq<E> connect() {
		this.perform(ClientTask.CONNECT);
		return this;
	}
}
