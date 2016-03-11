package controllers

import model.{ProgrammingLanguage,ProgrammingLanguageRepo}
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
class ProgrammingLanguageControllerSpec extends PlaySpecification with Mockito {

  "Assignment controller" should {

    val service = mock[ProgrammingLanguageRepo]
    val controller = new ProgrammingLanguageController(service)

    "send 404 on a bad request" in new WithApplication {
      route(FakeRequest(GET, "/mypage")) must beSome.which(status(_) == NOT_FOUND)
    }

    "render the award page" in new WithApplication {

      when(service.getLanguage("sonum")).thenReturn(Future(List(ProgrammingLanguage(1,"sonum","java","good"),ProgrammingLanguage(2,"sonum","scala","good"))))
      val res = call(controller.displayProgrammingLanguages,FakeRequest(GET, "/awards").withSession("username"->"sonum"))
      status(res) must equalTo(OK)

    }

  }
}
