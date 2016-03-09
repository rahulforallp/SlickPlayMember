package model

import org.specs2.mutable.Specification
import play.api.Application
import play.api.test.WithApplication
import scala.concurrent.duration._
import scala.concurrent.Await

/**
  * Created by knoldus on 9/3/16.
  */

class MemberRepoSpec extends Specification {
  "Member repository" should  {

    def memRepo(implicit app: Application) = Application.instanceCache[MemberRepo].apply(app)

    "insert single row" in new WithApplication {
      val result = Await.result(memRepo.insert(Member("Diksha","1234","user")),2 second)
      result === 1
    }

    "get single row" in new WithApplication {
      val result:Option[Member] = Await.result(memRepo.getMember("sonum","sonu"),2 second)
      result.get.username === "sonum"
    }
  }

}
