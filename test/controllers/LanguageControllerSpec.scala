package controllers

import model.{Language,LanguageRepo}
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
class LanguageControllerSpec extends PlaySpecification with Mockito {

  "Assignment controller" should {

    val service = mock[LanguageRepo]
    val controller = new LanguageController(service)

    "send 404 on a bad request" in new WithApplication {
      route(FakeRequest(GET, "/mypage")) must beSome.which(status(_) == NOT_FOUND)
    }

    "render the assignment page" in new WithApplication {

      when(service.getLanguage("sonum")).thenReturn(Future(List(Language(1,"sonum","hindi","excellent"),Language(1,"sonum","english","excellent"),Language(1,"sonum","punjabi","need improvement"),Language(1,"sonum","french","not known"))))
      val res = call(controller.displayLanguages,FakeRequest(GET, "/assignment").withSession("username"->"sonum"))
      status(res) must equalTo(OK)

    }

  }
}
