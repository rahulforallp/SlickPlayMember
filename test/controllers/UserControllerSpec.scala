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

  "User controller" should {

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

    "display award by user" in new WithApplication {
      val award = Award(7,"sonum","123","Debate","second","2013")
      when(service.getAward("sonum")).thenReturn(Future(List(award)))
      val res = call(controller.displayAwardByUser("sonum"), FakeRequest(POST,"/displayAwardByUser"))
      status(res) must equalTo (200)
    }

    "add award" in new WithApplication {
      val award = Award(7,"sonum","123","Debate","second","2013")
      when(service.insert(award)).thenReturn(Future(1))
      val res = call(controller.addAward, FakeRequest(GET,"/addAward"))
      status(res) must equalTo (200)
    }

    "add award by admin" in new WithApplication {
      val award = Award(7,"sonum","123","Debate","second","2013")
      when(service.insert(award)).thenReturn(Future(1))
      val res = call(controller.addAwardByAdmin, FakeRequest(GET,"/addAwardByAdmin"))
      status(res) must equalTo (200)
    }

    "get award by id" in new WithApplication {
      val award = Award(7,"sonum","123","Debate","second","2013")
      when(service.getAwardById(7)).thenReturn(Future(Option(award)))
      val res = call(controller.getAwardById(7), FakeRequest(GET,"/getAwardById"))
      status(res) must equalTo (200)
    }

    "edit award" in new WithApplication {
      val award = Award(7,"sonum","123","Debate","second","2012")
      when(service.update(award)).thenReturn(Future(1))
      val res = call(controller.editAward, FakeRequest(GET,"/editAward"))
      status(res) must equalTo (200)
    }


    "delete award" in new WithApplication {
      val award = Award(7,"sonum","123","Debate","second","2012")
      when(service.delete(7)).thenReturn(Future(1))
      val res = call(controller.deleteAward(7), FakeRequest(GET,"/deleteAward"))
      status(res) must equalTo (SEE_OTHER)
    }



  }
}
