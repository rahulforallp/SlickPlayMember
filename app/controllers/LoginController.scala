package controllers

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import model.Member
/**
  * Created by knoldus on 8/3/16.
  */
class LoginController extends Controller{
  val login:Form[Member]=Form(
    mapping(
      "username"-> nonEmptyText,
      "password"-> nonEmptyText
    )(Member.apply)(Member.unapply)
  )

  def displayLogin = Action {
    Ok(views.html.login(login))
  }

  def processLogin=Action{

    Ok
  }
}
