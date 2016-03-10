package model

import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import scala.concurrent.{Await, Future}

/**
  * Created by knoldus on 10/3/16.
  */
@Singleton()
class LanguageRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends LanguageTable with HasDatabaseConfigProvider[JdbcProfile] {

  def createTable()={

    db.run{languageTableQuery.schema.create}

  }

  def insert(lang: Language):Future[Int] = {
    db.run {
      languageTableQuery += lang
    }
  }

  def update(lang: Language):Future[Int]={
    db.run {languageTableQuery.filter(_.id === lang.id).update(lang)}
  }

  def delete(lang: Language):Future[Int]={
    db.run {languageTableQuery.filter(_.id === lang.id).delete}
  }

  def getLanguage(username:String):Future[List[Language]] = {

    db.run{
      languageTableQuery.filter(_.username===username).to[List].result
    }
  }
}


trait LanguageTable  { self: HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  protected class LanguageTable(tag: Tag) extends Table[Language](tag, "language") {
    val id: Rep[Int] = column[Int]("id", O.AutoInc,O.PrimaryKey)
    val username: Rep[String] = column[String]("username", O.SqlType("VARCHAR(200)"))
    val name: Rep[String] = column[String]("name", O.SqlType("VARCHAR(200)"))
    val frequency: Rep[String] = column[String]("frequency",O.SqlType("VARCHAR(200)"))

    def * = (id,username, name,frequency) <> (Language.tupled, Language.unapply)
  }

  lazy  val languageTableQuery = TableQuery[LanguageTable]

}