package controllers

import event.EventConsolidatorImpl
import json.{EventJSONConverter, MessageJSONConverter}
import play.api._
import play.api.mvc._
import message._

object EventController extends Controller with DummyMessageSource {
  val getEvents = Action { request =>
    val messages = getMessages.map(MessageJSONConverter.fromJSON(_))
                              .collect{case Some(e) => e}
    val events = EventConsolidatorImpl.buildEvents(messages)
    Ok(EventJSONConverter.toJSON(events))
  }
}
