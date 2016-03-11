package controllers

import model.{Award,AwardRepo}
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
class UserControllerSpec extends PlaySpecification with Mockito {

  "Assignment controller" should {

    val service = mock[AwardRepo]
    val controller = new UserController(service)

    "send 404 on a bad request" in new WithApplication {
      route(FakeRequest(GET, "/mypage")) must beSome.which(status(_) == NOT_FOUND)
    }

    "render the award page" in new WithApplication {

      when(service.getAward("sonum")).thenReturn(Future(List(Award(1,"sonum","1","debate runnerup","2nd position","2013"),Award(2,"sonum","2","essay competition","3rd position","2012"))))
      val res = call(controller.displayAwards,FakeRequest(GET, "/awards").withSession("username"->"sonum"))
      status(res) must equalTo(OK)

    }

  }
}
