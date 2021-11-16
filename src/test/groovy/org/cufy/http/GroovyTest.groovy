package org.cufy.http

import org.cufy.http.model.*
import org.junit.Test

import static org.cufy.http.ext.builders.*
import static org.cufy.http.ext.okhttp.OkHttpMiddleware.okHttpMiddleware
import static org.cufy.http.impl.BodyImpl.body
import static org.cufy.http.impl.FragmentImpl.fragment
import static org.cufy.http.impl.HostImpl.host
import static org.cufy.http.impl.PathImpl.path

class GroovyTest {
	@Test
	void main() {
		def mutex = new Object()

		Client {
			it << okHttpMiddleware()

			on(Action.REQUEST) { client, call ->
				call.request.method = Method.POST
				call.request.scheme = Scheme.HTTP
				call.request.userInfo[0] = "mohammed"
				call.request.userInfo[1] = "qwerty123"
				call.request.host = host("example.com")
				call.request.port = Port.HTTP
				call.request.path = path("user")
				call.request.query["username"] = "Mohammed+Saleh"
				call.request.query["mobile"] = "1032547698"
				call.request.fragment = fragment("top")
				call.request.httpVersion = HttpVersion.HTTP1_1
				call.request.headers["Authorization"] = "yTR1eWQ2zYX3"
				call.request.body = body("content".getBytes(), "mime")
				call.request.body = TextBody {
					it << "username=Mohammed Saleh\n"
					it << "password=qwerty123\n"
					it << "token=yTR1eWQ2zYX3\n"
				}
				call.request.body = ParametersBody {
					it["username"] = "Mohammed+Saleh"
					it["password"] = "qwerty123"
					it["token"] = "yTR1eWQ2zYX3"
				}
			}
			on(Action.CONNECT | Action.CONNECTED) { client, call ->
				println call
			}
			on(Action.DISCONNECTED | Action.EXCEPTION) { c, callOrException ->
				if (callOrException instanceof Call)
					callOrException.exception.printStackTrace()
				if (callOrException instanceof Throwable)
					callOrException.printStackTrace()
			}
			on(Action.CONNECTED | Action.DISCONNECTED | Action.EXCEPTION) { c, o ->
				synchronized (mutex) {
					mutex.notifyAll()
				}
			}

			connect()
		}

		synchronized (mutex) {
			mutex.wait(5_000)
		}
	}
}
