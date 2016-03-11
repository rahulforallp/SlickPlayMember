package controllers

import model.{Member, MemberRepo}
import org.specs2.mock.Mockito
import org.mockito.Mockito._
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future


@RunWith(classOf[JUnitRunner])
class LoginControllerSpec extends PlaySpecification with Mockito{

  "Login controller" should {

    val service = mock[MemberRepo]
    val controller = new LoginController(service)

    "send 404 on a bad request" in new WithApplication{
      route(FakeRequest(GET,"/mypage")) must beSome.which(status(_)==NOT_FOUND)
    }

    "render the login page" in new WithApplication{
      val login= route(FakeRequest(GET,"/displayLogin")).get
      status (login) must equalTo(OK)

    }

    "username is correct " in new WithApplication{

      when(service.getMember("sonum","sonu")).thenReturn(Future(Some(Member("sonum","sonu"))))
      val res=call(controller.processLogin,FakeRequest(GET,"/processLogin"))
      status(res) must equalTo(OK)
    }

    "username and password are not correct" in new WithApplication{

      when(service.getMember("sonum","sonueee")).thenReturn(Future(None))
      val res=call(controller.processLogin,FakeRequest(GET,"/processLogin"))
      status(res) must equalTo(303)
    }
  }
}
