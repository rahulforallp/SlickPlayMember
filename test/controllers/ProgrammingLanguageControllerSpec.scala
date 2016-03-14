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

    "add programming language" in new WithApplication {
      val language = ProgrammingLanguage(7,"sonum","C++","Good")
      when(service.insert(language)).thenReturn(Future(1))
      val res = call(controller.addProgrammingLanguage, FakeRequest(GET,"/addProgrammingLanguage"))
      status(res) must equalTo (200)
    }


    "add programming language by admin" in new WithApplication {
      val language = ProgrammingLanguage(7,"sonum","C++","Good")
      when(service.insert(language)).thenReturn(Future(1))
      val res = call(controller.addProgrammingLanguageByAdmin, FakeRequest(GET,"/addProgrammingLanguageByAdmin"))
      status(res) must equalTo (200)
    }

    "edit programming language" in new WithApplication {
      val language = ProgrammingLanguage(7,"sonum","C++","Very Good")
      when(service.update(language)).thenReturn(Future(1))
      val res = call(controller.editProgrammingLanguage, FakeRequest(GET,"/editProgrammingLanguage"))
      status(res) must equalTo (200)
    }

    "get programming language" in new WithApplication {
      val language = ProgrammingLanguage(7,"sonum","C++","Very Good")
      when(service.getLanguageById(7)).thenReturn(Future(Option(language)))
      val res = call(controller.getProgrammingLanguageById(7), FakeRequest(GET,"/getProgrammingLanguageById"))
      status(res) must equalTo (200)
    }

    "delete programming language" in new WithApplication {
      val language = ProgrammingLanguage(7,"sonum","C++","Very Good")
      when(service.delete(7)).thenReturn(Future(1))
      val res = call(controller.deleteProgrammingLanguage(7), FakeRequest(GET,"/deleteProgrammingLanguage"))
      status(res) must equalTo (SEE_OTHER)
    }

    "delete programming language" in new WithApplication {
      val language = ProgrammingLanguage(7,"sonum","C++","Very Good")
      when(service.getLanguage("sonum")).thenReturn(Future(List(language)))
      val res = call(controller.displayProgrammingByUser("sonum"), FakeRequest(GET,"/displayProgrammingByUser"))
      status(res) must equalTo (OK)
    }



  }
}
