package controllers

import config.AppConfig
import config.AppConfig.ReaderTFuture
import event.{EventConsolidator, Event, EventConsolidatorImpl}
import json._
import play.api._
import play.api.mvc._
import message._
import service.{EnrichmentServiceImpl, EnrichmentService}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
// needed for implicit scalaz.Functor[scala.concurrent.Future]
import scalaz.contrib.std._

trait EventController {

  // Explicitly typed self references let us cake these dependencies differently in production and test
  this: Controller with MessageSource with EventConsolidator with EnrichmentService with JSONConverter =>

  val getEvents = Action.async { request =>

    // Application configuration is hardcoded here for now
    val appConfig = AppConfig(5)

    // resultProcessor can be seen as a function that takes application configuration and returns
    // the required result of our computation which has type Future[Result]
    val resultProcessor: ReaderTFuture[AppConfig, Result] = for {

      // Read messages from json message source
      messageJson <- getMessages
      messages = messageJson.map(messageFromJSON(_))
                            .collect{case Some(e) => e}

      // Convert messages into events
      rawEvents = buildEvents(messages)

      // Enrich events individually and sequence into a single Future
      enrichedEvents <- ReaderTFuture.sequence(rawEvents.map(enrich(_)))

      // Convert events to json
      eventsJson = eventToJSON(enrichedEvents)

    } yield Ok(eventsJson)

    // Feed application configuration into our reader that computes the result
    resultProcessor(appConfig)

  }
}

// This EventController is caked to use production dependencies
object EventController extends Controller with EventController with HardcodedMessageSource with EventConsolidatorImpl with EnrichmentServiceImpl with PlayJSONConverter