package model

import org.specs2.mutable.Specification
import play.api.Application
import play.api.test.WithApplication
import scala.concurrent.duration._
import scala.concurrent.Await

/**
  * Created by knoldus on 10/3/16.
  */
class AssignmentSpec extends Specification {
  "Assignment repository" should {

    def assignmentRepo(implicit app: Application) = Application.instanceCache[AssignmentRepo].apply(app)

    "insert single row" in new WithApplication {
      val result = Await.result(assignmentRepo.insert(Assignment(1, "rahul", "Scala", "create CURD Project",10,"Good")), 2 second)
      result === 1
    }


    "get all row" in new WithApplication {
      val result = Await.result(assignmentRepo.getAssignment("rahul"), 2 second)
      result === 1
    }

    "update single row" in new WithApplication {
      val result = Await.result(assignmentRepo.update(Assignment(1, "rahul", "Play", "create CURD Project",10,"Good")), 2 second)
      result === 1
    }

    "delete single row" in new WithApplication {
      val result = Await.result(assignmentRepo.delete(1), 2 second)
      result === 1
    }

  }
}
