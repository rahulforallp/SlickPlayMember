package model

import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import scala.concurrent.{Await, Future}



@Singleton()
class AssignmentRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends AssignmentTable with HasDatabaseConfigProvider[JdbcProfile] {

  def createTable()={
    db.run{assignmentTableQuery.schema.create}
  }

  def insert(assign: Assignment):Future[Int] = {
    db.run {
      assignmentTableQuery += assign
    }
  }

  def update(assign: Assignment):Future[Int]={
    db.run {assignmentTableQuery.filter(_.id === assign.id).update(assign)}
  }

  def delete(id: Int):Future[Int]={
    db.run {assignmentTableQuery.filter(_.id === id).delete}
  }

  def getAssignment(username:String):Future[List[Assignment]] = {

    db.run{
      assignmentTableQuery.filter(_.username===username).to[List].result
    }
  }
}


trait AssignmentTable  { self: HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  protected class AssignmentTable(tag: Tag) extends Table[Assignment](tag, "assignment") {
    val id: Rep[Int] = column[Int]("id", O.AutoInc,O.PrimaryKey)
    val username: Rep[String] = column[String]("username", O.SqlType("VARCHAR(200)"))
    val name: Rep[String] = column[String]("name",O.SqlType("VARCHAR(200)"))
    val description: Rep[String] = column[String]("description",O.SqlType("VARCHAR(200)"))
    val marks: Rep[Int] = column[Int]("marks")
    val remarks: Rep[String] = column[String]("remarks",O.SqlType("VARCHAR(200)"))
    def * = (id,username,name,description,marks,remarks) <> (Assignment.tupled, Assignment.unapply)
  }

  lazy  val assignmentTableQuery = TableQuery[AssignmentTable]
}