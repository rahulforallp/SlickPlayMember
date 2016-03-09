package model

import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import scala.concurrent.{Await, Future}


@Singleton()
class MemberRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends MemberTable with HasDatabaseConfigProvider[JdbcProfile] {

  def createTable()={
    println("creating")
    db.run{memberTableQuery.schema.create}
    println("created")
  }

  def insert(mem: Member):Future[Int] = {
    db.run {
      memberTableQuery += mem
    }
  }

  def getMember(username:String,password:String): Future[Option[Member]] = {
    db.run{
      memberTableQuery.filter(a =>a.username === username && a.password===password).result.headOption
    }
  }

}

  trait MemberTable  { self: HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  protected class MemberTable(tag: Tag) extends Table[Member](tag, "member") {
    val username: Rep[String] = column[String]("username", O.SqlType("VARCHAR(200)"),O.PrimaryKey)
    val password: Rep[String] = column[String]("password", O.SqlType("VARCHAR(200)"))
    val usertype: Rep[String] = column[String]("usertype",O.SqlType("VARCHAR(200)"))
    val name: Rep[String] = column[String]("name",O.SqlType("VARCHAR(200)"))

    def * = (username, password, usertype, name) <> (Member.tupled, Member.unapply)
  }

  lazy  val memberTableQuery = TableQuery[MemberTable]

}