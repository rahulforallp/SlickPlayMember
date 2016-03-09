package model.connection

import slick.driver.PostgresDriver

trait PostgresComponent extends DBComponent {

  val driver = PostgresDriver

  import driver.api._

  val db: Database = PostgresDB.connectionPool

}

object PostgresDB {

  import slick.driver.PostgresDriver.api._

  val connectionPool = Database.forConfig("database")

}