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

  "language controller" should {

    val service = mock[LanguageRepo]
    val controller = new LanguageController(service)

    "send 404 on a bad request" in new WithApplication {
      route(FakeRequest(GET, "/mypage")) must beSome.which(status(_) == NOT_FOUND)
    }

    "render the language page" in new WithApplication {

      when(service.getLanguage("sonum")).thenReturn(Future(List(Language(1,"sonum","hindi","excellent"),Language(1,"sonum","english","excellent"),Language(1,"sonum","punjabi","need improvement"),Language(1,"sonum","french","not known"))))
      val res = call(controller.displayLanguages,FakeRequest(GET, "/assignment").withSession("username"->"sonum"))
      status(res) must equalTo(OK)

    }

    "add language" in new WithApplication {
      val language = Language(5,"sonum","Punjabi","Fair")
      when(service.insert(language)).thenReturn(Future(1))
      val res = call(controller.addLanguage, FakeRequest(GET,"/addLanguage"))
      status(res) must equalTo (200)
    }

    "get language by id" in new WithApplication {
      val language = Language(5,"sonum","Punjabi","Fair")
      when(service.getLanguageById(5)).thenReturn(Future(Option(language)))
      val res = call(controller.getLanguageById(5), FakeRequest(GET,"/getLanguageById"))
      status(res) must equalTo (OK)
    }

    "edit language" in new WithApplication {
      val language = Language(5,"sonum","Punjabi","Fairly good")
      when(service.update(language)).thenReturn(Future(1))
      val res = call(controller.editLanguage, FakeRequest(GET,"/editLanguage"))
      status(res) must equalTo (200)
    }

    "display language by user" in new WithApplication {
      val language = Language(5,"sonum","Punjabi","Fair")
      when(service.getLanguage("sonum")).thenReturn(Future(List(language)))
      val res = call(controller.getLanguageById(5), FakeRequest(GET,"/displayLanguageByUser"))
      status(res) must equalTo (200)
    }


    "delete language" in new WithApplication {
      val language = Language(5,"sonum","Punjabi","Fair")
      when(service.delete(5)).thenReturn(Future(1))
      val res = call(controller.deleteLanguage(5), FakeRequest(GET,"/deleteLanguage"))
      status(res) must equalTo (SEE_OTHER)
    }


  }
}

