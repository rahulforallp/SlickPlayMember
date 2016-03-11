package controllers

import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms._
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
      "year" -> nonEmptyText,
      "description" -> text
    )(Award.apply)(Award.unapply)
  )


  def displayAwards=Action.async { implicit request =>
    val user: Option[String] = request.session.get("username")
    val awardList = service.getAward(user.get)
    awardList.map { award =>
      Ok(views.html.awards(award,awardForm))
    }
  }

  def displayAwardById(id:Int)=Action.async { implicit request =>
    val user: Option[String] = request.session.get("username")
    val awardList = service.getAwardById(id)
    awardList.map { award =>
      Ok(""+awardList)
    }
  }

  def addAward=Action.async{
    implicit request =>
      awardForm.bindFromRequest.fold(
        badForm =>Future{ Ok("Error"+badForm)},
        data =>{
          val user:Option[String] =  request.session.get("username")
          val id=data.id
          val name=data.name
          val serialNo =data.serialNo
          val desc =data.description
          val year= data.year
          val award= Award(id,user.get,serialNo,name,desc,year)
          val res = service.insert(award)
          res.map{
            r => if(r==1) Redirect("/awards") else Ok("Bye")
          }
        })
  }
}
