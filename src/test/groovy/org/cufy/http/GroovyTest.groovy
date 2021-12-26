package org.cufy.http

import org.cufy.http.body.BytesBody
import org.cufy.http.body.ParametersBody
import org.cufy.http.body.TextBody
import org.cufy.uri.Fragment
import org.cufy.uri.Host
import org.cufy.uri.Path
import org.cufy.uri.Port
import org.cufy.uri.Query
import org.cufy.uri.Scheme
import org.cufy.uri.Uri
import org.cufy.uri.UserInfo
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

import static org.cufy.http.client.Http.open

@Disabled
@SuppressWarnings("JUnitTestMethodWithNoAssertions")
class GroovyTest {
	@Test
	void api() {
		open(Method.GET, Uri.parse("https://maqtorah.com"))
		//				.engine(OkEngine.Companion)
				.query("categoryId", "610bb3485a7e8a19df3f9955")
				.connect()
	}

	@Test
	void main() {
		open(Method.GET, Uri.parse("https://duckduckgo.com"))
				.scheme(Scheme.HTTP)
				.userInfo(UserInfo.USERNAME, "mohammed")
				.userInfo(UserInfo.PASSWORD, "qwerty123")
				.host(Host.parse("example.com"))
				.port(Port.HTTP)
				.path(Path.parse("user"))
				.query("username", "Mohammed+Saleh")
				.query("mobile", "1032547698")
				.fragment(Fragment.parse("top"))
				.httpVersion(HttpVersion.HTTP1_1)
				.header("Authorization", "yTR1eWQ2zYX3")
				.query("name", "value")
				.query {
					it["name"] = "ahmed"
					it["age"] = "27"
				}
				.query(new Query({
					it["id"] = "1234567890"
				}))
				.body(new BytesBody("content".getBytes()))
				.body(new TextBody({
					it << "username=Mohammed Saleh\n"
					it << "password=qwerty123\n"
					it << "token=yTR1eWQ2zYX3\n"
				}))
				.body(new ParametersBody({
					it["username"] = "Mohammed+Saleh"
					it["password"] = "qwerty123"
					it["token"] = "yTR1eWQ2zYX3"
				}))
		//				.resume(On.REQUEST) {
		//					System.out.println "Just before connecting"
		//				}
		//				.resume(On.RESPONSE) {
		//					System.out.println "Right after the connection"
		//				}
		//				.resume(On.CONNECTED) {
		//					System.out.println "--------------- REQUEST  ---------------"
		//					System.out.println it.request
		//					System.out.println "--------------- RESPONSE ---------------"
		//					System.out.println it.response
		//					System.out.println "----------------------------------------"
		//				}
		//				.resume(On.DISCONNECTED | EXCEPTION) {
		//					if (it instanceof Call)
		//						it.exception.printStackTrace()
		//					if (it instanceof Throwable)
		//						it.printStackTrace()
		//				}
		//				.connectSync()
	}

	@Test
	void multipart() {
		open(Method.POST, Uri.parse("http://localhost:3001/upload"))
		//				.use(okHttpMiddleware())
		//				.header("Authorization", "619679d178e761412646bd00")
		//				.body(new MultipartBody({
		//					it.contentType = "multipart/form-data"
		//					it << new BodyPart(
		//							new Headers({
		//								it["Content-Disposition"] =
		//										"form-data; name=\"file\"; filename=\"file.png\""
		//							}),
		//							new FileBody({
		//								it.contentType = "image/png"
		//								it.file = new File("C:\\Projects\\cufy\\http\\delete.png")
		//							})
		//					)
		//				}))
		//				.resume(On.DISCONNECTED | EXCEPTION) {
		//					if (it instanceof Call)
		//						it.exception.printStackTrace()
		//					if (it instanceof Throwable)
		//						it.printStackTrace()
		//				}
		//				.resume(On.CONNECTED) {
		//					String content = it.request.body.toString()
		//
		//					println("---------------------------------------------")
		//					println(it.request.requestLine)
		//					println(it.request.headers)
		//					println(content[0..1000])
		//					println("...")
		//					println(content[-1000..-1])
		//					println("---------------------------------------------")
		//					println(it.response)
		//				}
		//				.connectSync()
	}
}
