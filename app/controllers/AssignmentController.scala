package controllers

import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._
import model.{Assignment,AssignmentRepo}
import play.api.i18n.Messages.Implicits._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Play.current

import scala.concurrent.Future

/**
  * Created by knoldus on 10/3/16.
  */
class AssignmentController @Inject()(service:AssignmentRepo) extends Controller{

  val assignmentForm:Form[Assignment] = Form(
    mapping(
      "id" -> number,
      "username" -> text,
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "marks"-> number,
      "remarks" -> text
    )(Assignment.apply)(Assignment.unapply)
  )

  def displayAssignment=Action.async { implicit request =>
    val user: Option[String] = request.session.get("username")
    val assignmentList = service.getAssignment(user.get)
    assignmentList.map { assgn =>
      Ok(views.html.assignment(assgn))
    }
  }

  def displayAssignmentByUser(user:String)= Action.async { implicit request =>
    //val admin: Option[String] = request.session.get("username")
    val assignmentList = service.getAssignment(user)
    assignmentList.map { list =>
      Ok(views.html.internassignment(list,assignmentForm))

    }
  }

  def addAssignment=Action.async{
    implicit request =>
      assignmentForm.bindFromRequest.fold(
        badForm =>Future{ Ok("Error "+badForm)},
        data =>{
          val user:String= data.username
          val id=data.id
          val name=data.name
          val description =data.description
          val marks=data.marks
          val remarks=data.remarks
          val assignment= Assignment(id,user,name,description,marks,remarks)
          val res = service.insert(assignment)
          res.map{
            r => if(r==1) Redirect("/interns") else Ok("Bye")
          }
        })
  }

  def getAssignmentById(id:Int)= Action.async{
    val assignment= service.getAssignmentById(id)
    assignment map { assgn =>
      val json = Json.obj(
        "id" -> assgn.get.id,
        "name" -> assgn.get.name,
        "description" -> assgn.get.description,
        "marks" -> assgn.get.marks,
        "remarks"-> assgn.get.remarks
      )
      Ok(json)
    }
  }

  def editAssignment = Action.async{
    implicit request =>
      assignmentForm.bindFromRequest.fold(
        badForm => Future{Ok("bad form")},
        data => {
          val user:String=data.username
          val id=data.id
          val name=data.name
          val description=data.description
          val marks=data.marks
          val remarks=data.remarks
          val assignment= Assignment(id,user,name,description,marks,remarks)
          val res = service.update(assignment)
          res.map{
            r => if(r==1) Redirect("/interns") else Ok("Bye")
          }
        })
  }

  def deleteAssignment(id:Int)=Action.async{ implicit request =>
    val result = service.delete(id)
    result.map{
      res => if (res ==1) Redirect("/interns") else Ok("bye")
    }
  }



}
