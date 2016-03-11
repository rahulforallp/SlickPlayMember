package controllers

import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import model.{AssignmentRepo}
import play.api.i18n.Messages.Implicits._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Play.current

/**
  * Created by knoldus on 10/3/16.
  */
class AssignmentController @Inject()(service:AssignmentRepo) extends Controller{

  def displayAssignment=Action.async { implicit request =>
    val user: Option[String] = request.session.get("username")
    val assignmentList = service.getAssignment(user.get)
    assignmentList.map { assgn =>
      Ok(views.html.assignment(assgn))
    }
  }
}
