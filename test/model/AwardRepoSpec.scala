package model

import org.specs2.mutable.Specification
import play.api.Application
import play.api.test.WithApplication
import scala.concurrent.duration._
import scala.concurrent.Await

/**
  * Created by knoldus on 9/3/16.
  */
class AwardRepoSpec extends Specification {
  "Award repository" should {

    def awrdRepo(implicit app: Application) = Application.instanceCache[AwardRepo].apply(app)

    "insert single row" in new WithApplication {
      val result = Await.result(awrdRepo.insert(Award(1,"rahul", "co81", "Code Olympic", "First","2015")), 2 second)
      result === 1
    }

    "update single row" in new WithApplication {
      val result = Await.result(awrdRepo.update(Award(1,"rahul", "Do81", "Code Olympic", "First","2016")), 2 second)
      result === 1
    }

    "get one row" in new WithApplication {
      val result = Await.result(awrdRepo.getAwardById(1), 2 second)
      result === Some(Award(1,"rahul", "Do81", "Code Olympic", "First","2016"))

    "delete single row" in new WithApplication {
      val result = Await.result(awrdRepo.delete(1), 2 second)
      result === 1
    }

    "get all row" in new WithApplication {
      val result = Await.result(awrdRepo.getAward(("rahul")), 2 second)
      result === List()
    }

  }


}
