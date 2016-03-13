package model

import javax.inject.{Inject, Singleton}
import org.postgresql.util.PSQLException
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import scala.concurrent.{ Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

/**
  * Created by knoldus on 10/3/16.
  */
@Singleton()
class ProgrammingLanguageRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends ProgrammingLanguageTable with HasDatabaseConfigProvider[JdbcProfile] {

  def createTable()={

    db.run{ pLanguageTableQuery.schema.create }

  }

  def insert(lang: ProgrammingLanguage):Future[Int] =  {

      db.run((pLanguageTableQuery += lang).asTry).map(result =>
      result match {
        case Success(res) => res
        case Failure(e:PSQLException) => 0
        case Failure(e) => 0
      })


  }

  def update(lang: ProgrammingLanguage):Future[Int]={
    db.run {pLanguageTableQuery.filter(_.id === lang.id).map(x => (x.name,x.frequency)).update((lang.name,lang.frequency))}
  }

  def delete(id:Int):Future[Int]={
    db.run {pLanguageTableQuery.filter(_.id === id).delete}
  }

  def getLanguage(username:String):Future[List[ProgrammingLanguage]] = {

    db.run{
      pLanguageTableQuery.filter(_.username===username).to[List].result
    }
  }

  def getLanguageById(id:Int):Future[Option[ProgrammingLanguage]] ={
    db.run{
      pLanguageTableQuery.filter(_.id===id).result.headOption
    }
  }
}

trait ProgrammingLanguageTable  { self: HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  protected class ProgrammingLanguageTable(tag: Tag) extends Table[ProgrammingLanguage](tag, "programminglanguage") {
    val id: Rep[Int] = column[Int]("id",O.PrimaryKey)
    val username: Rep[String] = column[String]("username", O.SqlType("VARCHAR(200)"))
    val name: Rep[String] = column[String]("name", O.SqlType("VARCHAR(200)"))
    val frequency: Rep[String] = column[String]("frequency",O.SqlType("VARCHAR(200)"))

    def * = (id,username, name,frequency) <> (ProgrammingLanguage.tupled, ProgrammingLanguage.unapply)
  }

  lazy  val pLanguageTableQuery = TableQuery[ProgrammingLanguageTable]

}
