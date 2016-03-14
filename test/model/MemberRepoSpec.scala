package model

import org.specs2.mutable.Specification
import play.api.Application
import play.api.test.WithApplication
import scala.concurrent.duration._
import scala.concurrent.{Future, Await}

/**
  * Created by knoldus on 9/3/16.
  */

class MemberRepoSpec extends Specification {
  "Member repository" should  {

    def memRepo(implicit app: Application) = Application.instanceCache[MemberRepo].apply(app)

    "insert single row" in new WithApplication {
      val result = Await.result(memRepo.insert(Member("Garima","1234","admin")),2 second)
      result === 1
    }

    "get single row" in new WithApplication {
      val result:Option[Member] = Await.result(memRepo.getMember("sonum","sonu"),2 second)
      result.get.username === "sonum"
    }

    "get single row" in new WithApplication {
      val result:List[Member] = Await.result(memRepo.getAllMember,2 second)
      result  === List(Member("Garima","1234","admin"))
    }

  }

}
