package controllers

import com.google.inject.Inject
import org.postgresql.util.PSQLException
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
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
      Ok(views.html.programming(planguage.sortBy(_.id),programmingLanguageForm))
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
            res.map {
              r => if (r == 1) Redirect("/programming") else Redirect("/programming").flashing("error" -> "Duplicate Key")
            }

        })
  }

  def addProgrammingLanguageByAdmin=Action.async{
    implicit request =>
      programmingLanguageForm.bindFromRequest.fold(
        badForm =>Future{ Ok("Error "+badForm)},
        data =>{
          val user:String = data.username
          val id=data.id
          val name=data.name
          val frequency =data.frequency
          val language= ProgrammingLanguage(id,user,name,frequency)
          val res = service.insert(language)
          res.map {
            r => if (r == 1) Redirect("/programming") else Redirect("/programming").flashing("error" -> "Duplicate Key")
          }

        })
  }

  def editProgrammingLanguage = Action.async{
    implicit request =>
      programmingLanguageForm.bindFromRequest.fold(
        badForm => Future{Ok("bad form")},
        data => {
          val user:Option[String]=request.session.get("username")
          val id=data.id
          val name=data.name
          val frequency=data.frequency
          val language= ProgrammingLanguage(id,user.get,name,frequency)
          val res = service.update(language)
          res.map{
            r => if(r==1) Redirect("/programming") else Ok("Bye")
          }
        })
        }

  def getProgrammingLanguageById(id:Int)= Action.async{
    val language= service.getLanguageById(id)
    language map { lang =>
      val json = Json.obj(
        "id" -> lang.get.id,
        "name" -> lang.get.name,
        "frequency" -> lang.get.frequency
      )
      Ok(json)
    }
  }

  def deleteProgrammingLanguage(id:Int)=Action.async{ implicit request =>
    val result = service.delete(id)
    result.map{
      res => print(res); if (res ==1) Redirect("/programming") else Ok("bye")
    }
  }
  def displayProgrammingByUser(user:String)= Action.async { implicit request =>
    //val admin: Option[String] = request.session.get("username")
    val languageList = service.getLanguage(user)
    languageList.map { lang =>
      Ok(views.html.internprogramming(lang, programmingLanguageForm))

    }
  }


}
