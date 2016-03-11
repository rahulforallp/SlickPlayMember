package controllers

import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import model.{ProgrammingLanguageRepo}
import play.api.i18n.Messages.Implicits._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Play.current

/**
  * Created by knoldus on 10/3/16.
  */
class ProgrammingLanguageController @Inject()(service:ProgrammingLanguageRepo) extends Controller{

  def displayProgrammingLanguages=Action.async { implicit request =>
    val user: Option[String] = request.session.get("username")
    val programmingLanguageList = service.getLanguage(user.get)
    programmingLanguageList.map { planguage =>
      Ok(views.html.programming(planguage))
    }
  }
}