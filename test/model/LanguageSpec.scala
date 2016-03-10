package model

import org.specs2.mutable.Specification
import play.api.Application
import play.api.test.WithApplication
import scala.concurrent.duration._
import scala.concurrent.Await

/**
  * Created by knoldus on 10/3/16.
  */
class LanguageSpec extends Specification {
  "Language repository" should {

    def languageRepo(implicit app: Application) = Application.instanceCache[LanguageRepo].apply(app)

    "insert single row" in new WithApplication {
      val result = Await.result(languageRepo.insert(Language(1, "rahul", "Hindi","Good")), 2 second)
      result === 1
    }

    "get all row" in new WithApplication {
      val result = Await.result(languageRepo.getLanguage("rahul"), 2 second)
      result === 1
    }

    "modify single row" in new WithApplication {
      val result = Await.result(languageRepo.update(Language(1, "rahul", "English","Very Good")), 2 second)
      result === 1
    }

    "delete single row" in new WithApplication {
      val result = Await.result(languageRepo.delete(1), 2 second)
      result === 1
    }
  }
}
