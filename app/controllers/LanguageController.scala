package controllers

import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import model.{AwardRepo}
import play.api.i18n.Messages.Implicits._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Play.current

/**
  * Created by knoldus on 10/3/16.
  */
class LanguageController @Inject()(service:AwardRepo) extends Controller{ {

}
