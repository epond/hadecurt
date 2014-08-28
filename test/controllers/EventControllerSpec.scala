package controllers

import config.AppConfig
import config.AppConfig.ReaderTFuture
import message._
import event._
import json._
import service._
import play.api.mvc._
import play.api.test._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class EventControllerSpec extends PlaySpecification with Results {

  "An EventController" should {
    "Read messages from Json, convert them to Events, enrich events, then output Event JSON" in {
      val controller = new TestEventController()
      val result: Future[Result] = controller.getEvents().apply(FakeRequest())
      val bodyText: String = contentAsString(result)

      controller.messages.size must beEqualTo(2)
      controller.messages.contains(Message("dummy", "", "message1json")) must beTrue
      controller.messages.contains(Message("dummy", "", "message2json")) must beTrue
      controller.events.size must beEqualTo(2)
      controller.events.contains(Event("eventid", "message1json enriched")) must beTrue
      controller.events.contains(Event("eventid", "message2json enriched")) must beTrue
      bodyText must beEqualTo("dummy event json")
    }
  }
}


class TestEventController extends Controller with EventController with DummyMessageSource with SimpleEventConsolidator with DummyEnrichmentService with DummyJSONConverter

trait DummyMessageSource extends MessageSource {
  override def getMessages = ReaderTFuture[AppConfig, List[String]] { config =>
    Future(List("message1json", "message2json"))
  }
}

trait DummyJSONConverter extends JSONConverter {
  var events: Seq[Event] = Nil

  override def eventToJSON(events: Seq[Event]): String = {
    this.events = events
    "dummy event json"
  }
  override def messageFromJSON(message: String): Option[Message] = Some(Message("dummy", "", message))
}

trait SimpleEventConsolidator extends EventConsolidator {
  var messages: List[Message] = Nil

  override val eventBuilders: List[EventBuilder] = Nil
  override def buildEvents(messages: List[Message]): List[Event] = {
    this.messages = messages
    messages.map(m => Event("eventid", m.payload))
  }
}

trait DummyEnrichmentService extends EnrichmentService {
  override def enrich(event: Event) = ReaderTFuture[AppConfig, Event] { config =>
    Future(Event(event.id, event.description ++ " enriched"))
  }
}