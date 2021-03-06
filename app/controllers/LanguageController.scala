package controllers

import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._
import model.{Language, LanguageRepo}
import play.api.i18n.Messages.Implicits._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Play.current

import scala.concurrent.Future

/**
  * Created by knoldus on 10/3/16.
  */
class LanguageController @Inject()(service:LanguageRepo) extends Controller{

  val languageForm:Form[Language]=Form(
    mapping(
      "id" -> number,
      "username" -> text,
      "name" -> nonEmptyText,
      "frequency" -> nonEmptyText
    )(Language.apply)(Language.unapply)
  )

  def displayLanguages=Action.async { implicit request =>
    val user: Option[String] = request.session.get("username")
    val languageList = service.getLanguage(user.get)
    languageList.map { language =>
      Ok(views.html.languages(language.sortBy(_.id),languageForm))
    }
  }

  def addLanguage=Action.async{
    implicit request =>
      languageForm.bindFromRequest.fold(
        badForm =>Future{ Ok("Error "+badForm)},
        data =>{
          val user:Option[String] =  request.session.get("username")
          val id=data.id
          val name=data.name
          val frequency =data.frequency
          val language= Language(id,user.get,name,frequency)
          val res = service.insert(language)
          res.map{
            r => if(r==1) Redirect("/languages") else Ok("Bye")
          }
        })
  }

  def addLanguageByAdmin=Action.async{
    implicit request =>
      languageForm.bindFromRequest.fold(
        badForm =>Future{ Ok("Error "+badForm)},
        data =>{
          val user:String = data.username
          val id=data.id
          val name=data.name
          val frequency =data.frequency
          val language= Language(id,user,name,frequency)
          val res = service.insert(language)
          res.map{
            r => if(r==1) Redirect("/languages") else Ok("Bye")
          }
        })
  }

  def getLanguageById(id:Int)= Action.async{
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

  def editLanguage = Action.async{
    implicit request =>
      languageForm.bindFromRequest.fold(
        badForm => Future{Ok("bad form")},
        data => {
          val user:Option[String]=request.session.get("username")
          val id=data.id
          val name=data.name
          val frequency=data.frequency
          val language= Language(id,user.get,name,frequency)
          val res = service.update(language)
          res.map{
            r => if(r==1) Redirect("/languages") else Ok("Bye")
          }
        })
  }

  def displayLanguageByUser(user:String)= Action.async { implicit request =>
    //val admin: Option[String] = request.session.get("username")
    val languageList = service.getLanguage(user)
    languageList.map { lang =>
      Ok(views.html.internlanguage(lang, languageForm))

    }
  }


  def deleteLanguage(id:Int)=Action.async{ implicit request =>
    val result = service.delete(id)
    result.map{
      res => if (res ==1) Redirect("/languages") else Ok("bye")
    }
  }

}
