package controllers

import event.EventConsolidatorImpl
import json.{EventJSONConverter, MessageJSONConverter}
import play.api._
import play.api.mvc._
import message.FileMessageSource

object EventController extends Controller with FileMessageSource {
  val getEvents = Action { request =>
    val messages = getMessages.map(MessageJSONConverter.fromJSON(_))
                              .filter(_.isDefined)
                              .map(_.get)
    val events = EventConsolidatorImpl.buildEvents(messages)
    Ok(EventJSONConverter.toJSON(events))
  }
}
