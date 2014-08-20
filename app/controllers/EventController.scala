package controllers

import event.EventConsolidatorImpl
import json.{EventJSONConverter, MessageJSONConverter}
import play.api._
import play.api.mvc._
import message._
import service.{EnrichmentServiceImpl, EnrichmentService}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait EventController {
  // Explicitly typed self references let us cake these dependencies differently in production and test
  this: Controller with MessageSource with EnrichmentService =>

  val getEvents = Action.async { request =>

    for {

      // Read messages from json message source
      messageJson <- getMessages
      messages = messageJson.map(MessageJSONConverter.fromJSON(_))
                            .collect{case Some(e) => e}

      // Convert messages into events
      rawEvents = EventConsolidatorImpl.buildEvents(messages)

      // Enrich events individually
      enrichedEvents <- Future.sequence(rawEvents.map(enrich(_)))

      // Output events as json
      eventsJson = EventJSONConverter.toJSON(enrichedEvents)

    } yield Ok(eventsJson)

  }
}

// This EventController is caked to use production dependencies
object EventController extends Controller with EventController with EnrichmentServiceImpl with DummyMessageSource