package controllers

import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import model.{Language, ProgrammingLanguage, ProgrammingLanguageRepo}
import play.api.i18n.Messages.Implicits._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Play.current

import scala.concurrent.Future

/**
  * Created by knoldus on 10/3/16.
  */
class ProgrammingLanguageController @Inject()(service:ProgrammingLanguageRepo) extends Controller{

  val programmingLanguageForm:Form[ProgrammingLanguage]=Form(
    mapping(
      "id" -> number,
      "username" -> text,
      "name" -> nonEmptyText,
      "frequency" -> nonEmptyText
    )(ProgrammingLanguage.apply)(ProgrammingLanguage.unapply)
  )

  def displayProgrammingLanguages=Action.async { implicit request =>
    val user: Option[String] = request.session.get("username")
    val programmingLanguageList = service.getLanguage(user.get)
    programmingLanguageList.map { planguage =>
      Ok(views.html.programming(planguage,programmingLanguageForm))
    }
  }


  def addProgrammingLanguage=Action.async{
    implicit request =>
      programmingLanguageForm.bindFromRequest.fold(
        badForm =>Future{ Ok("Error "+badForm)},
        data =>{
          val user:Option[String] =  request.session.get("username")
          val id=data.id
          val name=data.name
          val frequency =data.frequency
          val language= ProgrammingLanguage(id,user.get,name,frequency)
          val res = service.insert(language)
          res.map{
            r => if(r==1) Redirect("/programming") else Ok("Bye")
          }
        })
  }
}
