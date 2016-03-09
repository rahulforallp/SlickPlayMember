package model

import org.specs2.mutable.Specification
import play.api.Application
import play.api.test.WithApplication
import scala.concurrent.duration._
import scala.concurrent.Await
import java.util.Date
import scala.concurrent.Future
/**
  * Created by knoldus on 9/3/16.
  */

class MemberRepoSpec extends Specification {
  "Member repository" should  {

    def memRepo(implicit app: Application) = Application.instanceCache[MemberRepo].apply(app)

    "insert single row" in new WithApplication {
      val result = Await.result(memRepo.insert(Member("SONU","1234","user","Rahul Kumar")),2 second)
      result === 1
    }

    "get single row" in new WithApplication {
      val result:Option[Member] = Await.result(memRepo.getMember("Rahul","1234"),2 second)
      result.get.name === "Rahul Kumar"
    }
  }

}
