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
package org.cufy.http.wrapper;

import org.cufy.http.Endpoint;
import org.cufy.http.Request;
import org.jetbrains.annotations.NotNull;

/**
 * A multipurpose request wrapper.
 *
 * @param <E>    the type of the endpoint.
 * @param <Res>  the type of the response wrapper.
 * @param <Self> the type of the wrapper.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.12
 */
public interface RequestContext<E extends Endpoint, Res extends ResponseContext<E, Self, Res>, Self extends RequestContext<E, Res, Self>>
		extends MessageContext<E, Request, Self, Res, Self>, RequestWrapper<Self> {
	@NotNull
	@Override
	default Self req() {
		return (Self) this;
	}
}
