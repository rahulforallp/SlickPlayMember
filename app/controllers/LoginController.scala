package controllers

import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import model._
import play.api.i18n.Messages.Implicits._
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.Future

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

  def processLogin = Action.async{ implicit request =>
    login.bindFromRequest.fold(
      badForm =>Future {Ok("error")},
      data => service.getMember(data.username, data.password).map {
        mem => mem.isDefined match {

          case true => Ok(views.html.user("")).withSession("username"->data.username)
          case false => Redirect("/displayLogin")
        }
      }
    )
  }


}
