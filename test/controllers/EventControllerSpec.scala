package controllers

import message.DummyMessageSource
import org.specs2.mutable.Specification
import play.api.mvc.Controller
import service.EnrichmentServiceImpl

class EventControllerSpec extends Specification {

  object TestEventController extends Controller with EventController with EnrichmentServiceImpl with DummyMessageSource

  "An EventController" should {
    "Read messages from Json, convert them to Events, enrich events, then output Event JSON" in {
      pending
    }
  }
}
