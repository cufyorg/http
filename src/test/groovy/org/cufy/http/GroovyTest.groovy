package org.cufy.http

import org.cufy.http.request.HttpVersion
import org.cufy.http.request.Method
import org.cufy.http.uri.Port
import org.cufy.http.uri.Scheme
import org.junit.Test

import static org.cufy.http.body.Body.body
import static org.cufy.http.groovy.builders.*
import static org.cufy.http.middleware.okhttp.OkHttpMiddleware.okHttpMiddleware

class GroovyTest {
	@Test
	void main() {
		Client {
			it << okHttpMiddleware()

			request.method = Method.POST
			request.scheme = Scheme.HTTP
			request.userInfo[0] = "mohammed"
			request.userInfo[1] = "qwerty123"
			request.host = "example.com"
			request.port = Port.HTTP
			request.path = "user"
			request.query["username"] = "Mohammed+Saleh"
			request.query["mobile"] = "1032547698"
			request.fragment = "top"
			request.httpVersion = HttpVersion.HTTP1_1
			request.headers["Authorization"] = "yTR1eWQ2zYX3"
			request.body = body("content", "mime")
			request.body = TextBody {
				it << "username=Mohammed Saleh\n"
				it << "password=qwerty123\n"
				it << "token=yTR1eWQ2zYX3\n"
			}
			request.body = ParametersBody {
				it["username"] = "Mohammed+Saleh"
				it["password"] = "qwerty123"
				it["token"] = "yTR1eWQ2zYX3"
			}
			request.body = JsonBody {
				it["username"] = "mohammed saleh"
				it["password"] = "qwerty123"
				it["token"] = "yTR1eWQ2zYX3"
			}

			on(CONNECT | CONNECTED) { c, r ->
				println r
			}
			on(DISCONNECTED | EXCEPTION) { c, e ->
				e.printStackTrace()
			}
			on(CONNECTED | DISCONNECTED | EXCEPTION) { c, o ->
				synchronized (it) {
					it.notifyAll()
				}
			}

			connect()

			synchronized (it) {
				it.wait(5_000)
			}
		}
	}
}
