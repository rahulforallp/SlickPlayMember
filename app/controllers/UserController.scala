package controllers

import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._
import model.{Member, AwardRepo, Award}
import play.api.i18n.Messages.Implicits._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Play.current

import scala.concurrent.Future

/**
  * Created by knoldus on 8/3/16.
  */
class UserController @Inject()(service:AwardRepo) extends Controller{

  val awardForm:Form[Award]=Form(
    mapping(
     "id" -> number,
      "username" -> text,
      "serialNo" -> text,
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "year" -> text
    )(Award.apply)(Award.unapply)
  )


  def displayAwards=Action.async { implicit request =>
    val user: Option[String] = request.session.get("username")
    val awardList = service.getAward(user.get)
    awardList.map { award =>
      Ok(views.html.awards(award.sortBy(_.id),awardForm))
    }
  }

//  def displayAwardById(id:Int)=Action.async { implicit request =>
//    val user: Option[String] = request.session.get("username")
//    val awardList = service.getAwardById(id)
//    awardList.map { award =>
//      Ok(""+awardList)
//    }
//  }

  def addAward=Action.async{
    implicit request =>
      awardForm.bindFromRequest.fold(
        badForm =>Future{ Ok("Error"+badForm)},
        data =>{
          val user:Option[String] = request.session.get("username")
          val id=data.id
          val name=data.name
          val serialNo =data.serialNo
          val description =data.description
          val year= data.year
          val award= Award(id,user.get,serialNo,name,description,year)
          val res = service.insert(award)
          res.map{
            r => if(r==1) Redirect("/awards") else Ok("Bye")
          }
        })
  }

  def getAwardById(id:Int)= Action.async{
    val award= service.getAwardById(id)
    award map { awrd =>
      val json = Json.obj(
        "id" -> awrd.get.id,
        "serialNo" -> awrd.get.serialNo,
        "name" -> awrd.get.name,
        "description" -> awrd.get.description,
        "year"->awrd.get.year
      )
      Ok(json)
    }
  }

  def editAward = Action.async{
    implicit request =>
      awardForm.bindFromRequest.fold(
        badForm => Future{Ok("bad form")},
        data => {
          val user:Option[String]=request.session.get("username")
          val id=data.id
          val serialNo =data.serialNo
          val name=data.name
          val description=data.description
          val year= data.year
          val award= Award(id,user.get,serialNo,name,description,year)
          val res = service.update(award)
          res.map{
            r => if(r==1) Redirect("/awards") else Ok("Bye")
          }
        })
  }
}
