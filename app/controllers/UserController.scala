package controllers

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import model.Member
import play.api.i18n.Messages.Implicits._
import play.api.Play.current
/**
  * Created by knoldus on 8/3/16.
  */
class UserController extends Controller{


  def displayAwards=Action{ implicit request =>
    Ok(views.html.awards())
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
