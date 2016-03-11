package controllers

import model.{Assignment,AssignmentRepo}
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
class AssignmentControllerSpec extends PlaySpecification with Mockito {

  "Assignment controller" should {

    val service = mock[AssignmentRepo]
    val controller = new AssignmentController(service)

    "send 404 on a bad request" in new WithApplication {
      route(FakeRequest(GET, "/mypage")) must beSome.which(status(_) == NOT_FOUND)
    }

    "render the assignment page" in new WithApplication {

      when(service.getAssignment("sonum")).thenReturn(Future(List(Assignment(1,"sonum","scala test","based on scala",7,"need improvement"))))
      val res = call(controller.displayAssignment,FakeRequest(GET, "/assignment").withSession("username"->"sonum"))
      status(res) must equalTo(OK)

    }

  }
}
