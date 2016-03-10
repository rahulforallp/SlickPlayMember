package controllers

import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import model.{AwardRepo}
import play.api.i18n.Messages.Implicits._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Play.current
/**
  * Created by knoldus on 8/3/16.
  */
class AwardController @Inject()(service:AwardRepo) extends Controller{


  def displayAwards=Action.async { implicit request =>
    val user: Option[String] = request.session.get("username")
    val awardList = service.getAward(user)
    awardList.map { award =>
      Ok(views.html.awards(award))
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
