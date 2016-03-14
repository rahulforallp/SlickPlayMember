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

    "display by user" in new WithApplication {
      when(service.getAssignment("sonum")).thenReturn(Future(List(Assignment(1,"sonum","scala teat","objective test",33,"some improvement needed"))))
      val res = call(controller.displayAssignmentByUser("sonum"),FakeRequest(GET,"displayAssignmentByUser"))
      status(res) must equalTo(OK)
    }

    "add assignment" in new WithApplication {
      val assignment = Assignment(5,"sonum","UI","Objective", 33, "good")
      when(service.insert(assignment)).thenReturn(Future(1))
      val res = call(controller.addAssignment, FakeRequest(GET,"/addAssignment"))
      status(res) must equalTo (301)
    }

    "get assignment by ID" in new WithApplication {
      val assignment = Assignment(5,"sonum","UI","Objective", 33, "good")
      when(service.getAssignmentById(5)).thenReturn(Future(Option(assignment)))
      val res = call(controller.getAssignmentById(5), FakeRequest(GET,"/getAssignmentById"))
      status(res) must equalTo (OK)
    }

    "edit assignment" in new WithApplication {
      val assignment = Assignment(5,"sonum","UI","Objective Test", 33, "good")
      when(service.update(assignment)).thenReturn(Future(1))
      val res = call(controller.editAssignment, FakeRequest(GET,"/editAssignment"))
      status(res) must equalTo (303)
    }


    "delete assignment" in new WithApplication {
      val assignment = Assignment(5,"sonum","UI","Objective Test", 33, "good")
      when(service.delete(5)).thenReturn(Future(1))
      val res = call(controller.deleteAssignment(5), FakeRequest(GET,"/deleteAssignment"))
      status(res) must equalTo (SEE_OTHER)
    }


  }
}
