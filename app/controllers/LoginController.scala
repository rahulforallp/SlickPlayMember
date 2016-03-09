package controllers

import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import model._
import play.api.i18n.Messages.Implicits._
import play.api.Play.current
import scala.concurrent.duration._

import scala.concurrent.{Await, Future}

/**
  * Created by knoldus on 8/3/16.
  */
class LoginController @Inject()(service:MemberRepo) extends Controller{
  val login=Form(
    tuple(
      "username"-> nonEmptyText,
      "password"-> nonEmptyText
    )
  )

  def displayLogin = Action{ implicit request =>
    Ok(views.html.login(login))
  }

  def processLogin=Action{

    Ok
  }
}
