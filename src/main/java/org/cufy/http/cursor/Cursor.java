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
package org.cufy.http.cursor;

import org.cufy.http.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import static org.cufy.http.Action.action;

/**
 * A cursor is a stateful object containing variables necessary to perform a single call.
 *
 * @param <C> the type of this cursor.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.20
 */
public class Cursor<C extends Cursor<C>> {
	/**
	 * The parent client cursor.
	 *
	 * @since 0.3.0 ~2021.11.20
	 */
	@Nullable
	private final Cursor parent;
	/**
	 * The wrapped call.
	 *
	 * @since 0.3.0 ~2021.11.18
	 */
	@Nullable
	private Call call;
	/**
	 * The client object wrapped by this cursor.
	 *
	 * @since 0.3.0 ~2021.11.18
	 */
	@Nullable
	private Client client;

	/**
	 * Construct a new cursor wrapping the given {@code parent} cursor.
	 *
	 * @param parent the parent cursor to be wrapped.
	 * @throws NullPointerException if the given {@code parent} is null.
	 * @since 0.3.0 ~2021.11.20
	 */
	public Cursor(@NotNull Cursor parent) {
		Objects.requireNonNull(parent, "parent");
		this.parent = parent;
	}

	/**
	 * Construct a new cursor wrapping the given {@code client}.
	 *
	 * @param client the client to be wrapped.
	 * @param call   the call to be wrapped.
	 * @throws NullPointerException if the given {@code client} or {@code call} is null.
	 * @since 0.3.0 ~2021.11.20
	 */
	public Cursor(@NotNull Client client, @NotNull Call call) {
		Objects.requireNonNull(client, "client");
		Objects.requireNonNull(call, "call");
		this.parent = null;
		this.client = client;
		this.call = call;
	}

	// Call

	/**
	 * Return the wrapped call.
	 *
	 * @return the call wrapped by this cursor.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(pure = true)
	public Call call() {
		assert this.parent == null ^ this.call == null;
		return this.parent != null ?
			   this.parent.call() :
			   this.call;
	}

	/**
	 * Set the call to the given {@code call}.
	 *
	 * @param call the call to be set.
	 * @return this.
	 * @throws NullPointerException if the given {@code call} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public C call(@Nullable Call call) {
		Objects.requireNonNull(call, "call");
		if (this.parent != null)
			this.parent.call(call);
		else
			this.call = call;
		return (C) this;
	}

	/**
	 * Invoke the given {@code operator} with the current call as its parameter.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException if the given {@code operator} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public C call(@NotNull Consumer<@NotNull Call> operator) {
		Objects.requireNonNull(operator, "operator");
		operator.accept(this.call());
		return (C) this;
	}

	// Client

	/**
	 * Return the wrapped client.
	 *
	 * @return the client wrapped by this cursor.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(pure = true)
	public Client client() {
		assert this.parent == null ^ this.client == null;
		return this.parent != null ?
			   this.parent.client() :
			   this.client;
	}

	/**
	 * Set the client to the given {@code client}.
	 *
	 * @param client the client to be set.
	 * @return this.
	 * @throws NullPointerException if the given {@code client} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public C client(@Nullable Client client) {
		Objects.requireNonNull(client, "client");
		if (this.parent != null)
			this.parent.client(client);
		else
			this.client = client;
		return (C) this;
	}

	/**
	 * Invoke the given {@code operator} with the current client as its parameter.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException if the given {@code operator} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public C client(@NotNull Consumer<@NotNull Client> operator) {
		Objects.requireNonNull(operator, "operator");
		operator.accept(this.client());
		return (C) this;
	}

	// ...

	/**
	 * Perform a connection using the wrapped client and call.
	 *
	 * @return this.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "->this", mutates = "this")
	public C connect() {
		this.client().perform(Action.CONNECT, this, error ->
				this.client().perform(Action.EXCEPTION, error, e -> {
					if (e != error)
						e.addSuppressed(error);
					//noinspection CallToPrintStackTrace
					e.printStackTrace();
				})
		);
		return (C) this;
	}

	/**
	 * Perform a connection using the wrapped client and call and wait for it to finish.
	 *
	 * @return this.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "->this", mutates = "this")
	public C connectSync() {
		Object mutex = new Object();
		boolean[] completed = {false};

		this.client().on(action(Action.CONNECTED, Action.DISCONNECTED), cursor -> {
			if (cursor.call() == this.call())
				synchronized (mutex) {
					completed[0] = true;
					mutex.notifyAll();
				}
		});

		synchronized (mutex) {
			this.client().perform(Action.CONNECT, this, error -> {
				synchronized (mutex) {
					completed[0] = true;
					mutex.notifyAll();
				}

				this.client().perform(Action.EXCEPTION, error, e -> {
					if (e != error)
						e.addSuppressed(error);
					//noinspection CallToPrintStackTrace
					e.printStackTrace();
				});
			});

			try {
				while (!completed[0])
					mutex.wait(1_000);
			} catch (InterruptedException ignored) {
			}
		}

		return (C) this;
	}

	// Exception

	/**
	 * Return the exception.
	 *
	 * @return the exception.
	 * @since 0.3.0 ~2021.11.18
	 */
	@Nullable
	@Contract(pure = true)
	public Throwable exception() {
		return this.call().getException();
	}

	/**
	 * Set the exception to the given {@code exception}.
	 *
	 * @param exception the exception to be set.
	 * @return this.
	 * @throws UnsupportedOperationException if the call does not support changing its
	 *                                       exception.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public C exception(@Nullable Throwable exception) {
		this.call().setException(exception);
		return (C) this;
	}

	/**
	 * Replace the exception to the result of invoking the given {@code operator} with the
	 * argument being the previous exception.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the call does not support changing its
	 *                                       exception and the given {@code operator}
	 *                                       returned another exception.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public C exception(@NotNull UnaryOperator<@Nullable Throwable> operator) {
		Objects.requireNonNull(operator, "operator");
		Objects.requireNonNull(operator, "operator");
		Throwable p = this.exception();
		Throwable exception = operator.apply(p);

		if (exception != p)
			this.exception(exception);

		return (C) this;
	}

	// ...

	/**
	 * Add the given {@code callback} to be performed when the given {@code action} occurs
	 * on the wrapped client. The thread executing the given {@code callback} is not
	 * specific so the given {@code callback} must not assume it will be executed in a
	 * specific thread (e.g. the application thread).
	 * <br>
	 * Exceptions thrown by the given {@code callback} will be caught safely. But,
	 * exception by a thread created by the callback is left for the callback to handle.
	 *
	 * @param action   the action to listen to.
	 * @param callback the callback to be set.
	 * @param <T>      the type of the expected parameter.
	 * @return this.
	 * @throws NullPointerException if the given {@code action} or {@code callback} is
	 *                              null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	public <T> C on(@NotNull Action<T> action, @NotNull Callback<T> callback) {
		this.client().on(action, callback);
		return (C) this;
	}

	/**
	 * Trigger the given {@code action} in the client. Invoke all the callbacks with the
	 * given {@code parameter} that was registered to be called when the given {@code
	 * action} occurs.
	 *
	 * @param parameter the parameter to invoke the callbacks with.
	 * @param action    the action to be triggered.
	 * @param <T>       the type of the parameter.
	 * @return this.
	 * @throws NullPointerException if the given {@code action} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract("_,_->this")
	public <T> C perform(@NotNull Action<T> action, @Nullable T parameter) {
		this.client().perform(action, parameter, error ->
				this.client().perform(Action.EXCEPTION, error, e -> {
					if (e != error)
						e.addSuppressed(error);
					//noinspection CallToPrintStackTrace
					e.printStackTrace();
				})
		);
		return (C) this;
	}

	/**
	 * Trigger the given {@code action} in the client. Invoke all the callbacks with
	 * {@code null} as the parameter that was registered to be called when the given
	 * {@code action} occurs.
	 *
	 * @param action the action to be triggered.
	 * @param <T>    the type of the parameter.
	 * @return this.
	 * @throws NullPointerException if the given {@code action} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract("_->this")
	public <T> C perform(@NotNull Action<T> action) {
		this.client().perform(action, null, error ->
				this.client().perform(Action.EXCEPTION, error, e -> {
					if (e != error)
						e.addSuppressed(error);
					//noinspection CallToPrintStackTrace
					e.printStackTrace();
				})
		);
		return (C) this;
	}

	// Request

	/**
	 * Return the request.
	 *
	 * @return the request.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(pure = true)
	public Request request() {
		return this.call().getRequest();
	}

	/**
	 * Set the request to the given {@code request}.
	 *
	 * @param request the request to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code request} is null.
	 * @throws UnsupportedOperationException if the call does not support changing its
	 *                                       request.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public C request(@NotNull Request request) {
		Objects.requireNonNull(request, "request");
		this.call().setRequest(request);
		return (C) this;
	}

	/**
	 * Invoke the given {@code operator} with the current request as the parameter.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException if the given {@code operator} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public C request(@NotNull Consumer<@NotNull Request> operator) {
		Objects.requireNonNull(operator, "operator");
		operator.accept(this.request());
		return (C) this;
	}

	// Response

	/**
	 * Return the response.
	 *
	 * @return the response.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(pure = true)
	public Response response() {
		return this.call().getResponse();
	}

	/**
	 * Set the response to the given {@code response}.
	 *
	 * @param response the response to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code response} is null.
	 * @throws UnsupportedOperationException if the call does not support changing its
	 *                                       response.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public C response(@NotNull Response response) {
		Objects.requireNonNull(response, "response");
		this.call().setResponse(response);
		return (C) this;
	}

	/**
	 * Invoke the given {@code operator} with the current response as its parameter.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException if the given {@code operator} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public C response(@NotNull Consumer<@NotNull Response> operator) {
		Objects.requireNonNull(operator, "operator");
		operator.accept(this.response());
		return (C) this;
	}

	// ...

	/**
	 * Inject the listeners of the given {@code middlewares} to the wrapped client (using
	 * {@link Middleware#inject(Client)}). Duplicate injection is the middleware
	 * responsibility to solve.
	 *
	 * @param middlewares the middlewares to be injected.
	 * @return this.
	 * @throws NullPointerException     if the given {@code middlewares} is null.
	 * @throws IllegalArgumentException if any of the given {@code middlewares} refused to
	 *                                  inject its callbacks to the client for some aspect
	 *                                  in it.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract("_->this")
	public C use(@Nullable Middleware @NotNull ... middlewares) {
		this.client().use(middlewares);
		return (C) this;
	}
}
