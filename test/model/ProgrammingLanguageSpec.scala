package model

import org.specs2.mutable.Specification
import play.api.Application
import play.api.test.WithApplication
import scala.concurrent.duration._
import scala.concurrent.Await

/**
  * Created by knoldus on 10/3/16.
  */
class ProgrammingLanguageSpec extends Specification {
  "ProgrammingLanguage repository" should {

    def languageRepo(implicit app: Application) = Application.instanceCache[ProgrammingLanguageRepo].apply(app)

    "insert single row" in new WithApplication {
      val result = Await.result(languageRepo.insert(ProgrammingLanguage(1, "rahul", "Scala", "Good")), 2 second)
      result === 1
    }

    "get all row" in new WithApplication {
      val result = Await.result(languageRepo.getLanguage("rahul"), 2 second)
      result === List((ProgrammingLanguage(1, "rahul", "Scala", "Good")))
    }

    "update single row" in new WithApplication {
      val result = Await.result(languageRepo.update(ProgrammingLanguage(1, "rahul", "Scala", "Very Good")), 2 second)
      result === 1
    }

    "delete single row" in new WithApplication {
      val result = Await.result(languageRepo.delete(1), 2 second)
      result === 1
    }
  }
}
