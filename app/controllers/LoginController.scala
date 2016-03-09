package controllers

import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import model._
import play.api.i18n.Messages.Implicits._
import play.api.Play.current
/**
  * Created by knoldus on 8/3/16.
  */
class LoginController @Inject()(service:MemberRepo) extends Controller{
  val login:Form[Member]=Form(
    mapping(
      "username"-> nonEmptyText,
      "password"-> nonEmptyText
    )(Member.apply)(Member.unapply)
  )

  def displayLogin = Action{ implicit request =>
    Ok(views.html.login(login))
  }

  def processLogin=Action{ implicit request =>
    Ok(views.html.user(""))
  }
}
