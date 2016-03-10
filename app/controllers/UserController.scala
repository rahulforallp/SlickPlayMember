package controllers

import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import model.{AwardRepo,Award}
import play.api.i18n.Messages.Implicits._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Play.current
/**
  * Created by knoldus on 8/3/16.
  */
class UserController @Inject()(service:AwardRepo) extends Controller{


  def displayAwards=Action.async { implicit request =>
    val user: Option[String] = request.session.get("username")
    val awardList = service.getAward(user)
    awardList.map { award =>
      Ok(views.html.awards(award))
    }
  }

  def displayAwardById(id:Int)=Action.async { implicit request =>
    val user: Option[String] = request.session.get("username")
    val awardList = service.getAwardById(id)
    awardList.map { award =>
      Ok(""+awardList)
    }
  }

  def addAward(id:Int,serlno:String,name:String,description:String,year:String)=Action.async{
    implicit request =>
      val user: Option[String] = request.session.get("username")
      val tempuser = user.mkString
      val award=Award(id,tempuser,serlno,name,description,year)
      val status = service.insert(award)
      status.map{
        a => Ok("added"+status)
      }
  }

  def displayLanguages=Action{ implicit request =>
    Ok(views.html.languages())
  }

  def displayAssignment=Action{ implicit request =>
    Ok(views.html.assignment())
  }

  def displayProgramming=Action{ implicit request =>
    Ok(views.html.programming())
  }
}
