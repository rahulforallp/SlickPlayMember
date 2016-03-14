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
class LoginController @Inject()(service:MemberRepo) extends Controller {
  val login: Form[Member] = Form(
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText,
      "usertype" -> text
    )(Member.apply)(Member.unapply)
  )

  def displayLogin = Action { implicit request =>
    Ok(views.html.login(login))
  }
  def processLogin = Action.async { implicit request =>
    login.bindFromRequest.fold(
      badForm => Future {
        Ok("error")
      },
      data => {
        val member=  service.getMember(data.username, data.password)
        member.map{mem =>
          val m = mem.get
          if(m.usertype=="admin") Ok(views.html.user("admin")).withSession("username"->data.username)
          else Ok(views.html.user("user")).withSession("username" -> data.username)
        }
      })
  }

  def logout = Action {
    implicit request =>
      if(request.session.get("username").isDefined) {
        Redirect(routes.LoginController.displayLogin).withNewSession
      }
      else{
        Ok("No")
      }
  }

  def listAllMember = Action.async{implicit request =>
  val list = service.getAllMember
    list map {
      ele =>  Ok(views.html.interns(ele))
    }

  }
}
