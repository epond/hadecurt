package controllers

import event.EventConsolidatorImpl
import json.{EventJSONConverter, MessageJSONConverter}
import play.api._
import play.api.mvc._
import message._
import service.{EnrichmentServiceImpl, EnrichmentService}

trait EventController {
  // Explicitly typed self references let us cake these dependencies differently in production and test
  this: Controller with MessageSource with EnrichmentService =>

  val getEvents = Action { request =>

    // Read messages from json message source
    val messages = getMessages.map(MessageJSONConverter.fromJSON(_))
                              .collect{case Some(e) => e}

    // Convert messages into events
    val events = EventConsolidatorImpl.buildEvents(messages)

    // Enrich events
    val enrichedEvents = events.map(enrich(_))

    // Output events as json
    Ok(EventJSONConverter.toJSON(enrichedEvents))

  }
}

// This EventController is caked to use production dependencies
object EventController extends Controller with EventController with EnrichmentServiceImpl with DummyMessageSource