package controllers

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class LoginControllerSpec extends Specification{

  "Login controller" should {

    "send 404 on a bad request" in new WithApplication{
      route(FakeRequest(GET,"/mypage")) must beSome.which(status(_)==NOT_FOUND)
    }

    "render the login page" in new WithApplication{
      val login= route(FakeRequest(GET,"/displayLogin")).get
      status (login) must equalTo(OK)
    }

    "username is wrong " in new WithApplication{
      val login= route(FakeRequest(POST,"/processLogin").withFormUrlEncodedBody("username"->"abc@xyz.com","password"->"1234")).get
      status (login) must equalTo(303)
    }

    "username and password are correct" in new WithApplication{
      val login= route(FakeRequest(POST,"/processLogin").withFormUrlEncodedBody("username"->"sonum","password"->"sonu")).get
      status (login) must equalTo(200)
    }
  }

}
