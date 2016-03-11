package model

import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import scala.concurrent.{Await, Future}


@Singleton()
class AwardRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends AwardTable with HasDatabaseConfigProvider[JdbcProfile] {

  def createTable={
    println("creating")
    db.run{awardTableQuery.schema.create}
    println("created")

  }

  def insert(award: Award):Future[Int] = {
    db.run {
      awardTableQuery += award
    }
  }

  def update(award:Award):Future[Int]={
    db.run {awardTableQuery.filter(_.id === award.id).update(award)}
  }

  def delete(id:Int):Future[Int]={
    db.run {awardTableQuery.filter(_.id === id).delete}
  }

  def getAward(username:String):Future[List[Award]] = {

    db.run{
      awardTableQuery.filter(_.username===username).to[List].result
    }
  }

  def getAwardById(id:Int):Future[List[Award]] = {

    db.run{
      awardTableQuery.filter(_.id===id).to[List].result
    }
  }
}

trait AwardTable  { self: HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  protected class AwardTable(tag: Tag) extends Table[Award](tag, "award") {
    val id: Rep[Int] = column[Int]("id", O.AutoInc,O.PrimaryKey)
    val username: Rep[String] = column[String]("username", O.SqlType("VARCHAR(200)"))
    val serialNo: Rep[String] = column[String]("serlno",O.SqlType("VARCHAR(25)"))
    val name: Rep[String] = column[String]("name",O.SqlType("VARCHAR(200)"))
    val description: Rep[String] = column[String]("description",O.SqlType("VARCHAR(200)"))
    val year: Rep[String] = column[String]("year",O.SqlType("VARCHAR(5)"))

    def * = (id,username,serialNo,name,description,year) <> (Award.tupled, Award.unapply)
  }

  lazy  val awardTableQuery = TableQuery[AwardTable]
}