package event

import message.{UnknownEvent, MessageGroupKey, KnownEvent, Message}
import org.specs2.mutable.Specification

class EventConsolidatorSpec extends Specification {

  "An EventConsolidator with just a single SingleMessageEventBuilder" should {
    "given a sequence of 3 messages across 3 event ids then build a sequence of 3 events" in {
      object SimpleEventConsolidator extends EventConsolidator {
        override val eventBuilders: Seq[EventBuilder] = List(
          SingleMessageEventBuilder
        )
      }

      val message1 = Message("videoView", "1", "a")
      val message2 = Message("videoLike", "2", "b")
      val message3 = Message("videoView", "3", "c")

      val events = SimpleEventConsolidator.buildEvents(List(message1, message2, message3))
      events.size must beEqualTo(3)
      events(0).id must beEqualTo("1")
      events(1).id must beEqualTo("2")
      events(2).id must beEqualTo("3")
    }

    "given a sequence of 3 messages across 2 event ids then build a sequence of 2 events" in {
      object SimpleEventConsolidator extends EventConsolidator {
        override val eventBuilders: Seq[EventBuilder] = List(
          SingleMessageEventBuilder
        )
      }

      val message1 = Message("videoView", "1", "a")
      val message2 = Message("videoLike", "1", "b")
      val message3 = Message("videoView", "2", "c")
      val events = SimpleEventConsolidator.buildEvents(List(message1, message2, message3))

      events.size must beEqualTo(2)
      events(0).id must beEqualTo("1")
      events(0).description must beEqualTo("a")
      events(1).id must beEqualTo("2")
      events(1).description must beEqualTo("c")
    }
  }

  "An EventConsolidator with EventBuilders for two different message types" should {
    "given a variety of messages then build single-message events for the known message types" in {
      object MyEventConsolidator extends EventConsolidator {
        override val eventBuilders: Seq[EventBuilder] = List(
          ByMessageTypeEventBuilder("t1"), ByMessageTypeEventBuilder("t2")
        )
      }

      val message1 = Message("t1", "1", "a")
      val message2 = Message("t2", "2", "b")
      val message3 = Message("t3", "3", "c")
      val events = MyEventConsolidator.buildEvents(List(message1, message2, message3))

      events.size must beEqualTo(2)
      events(0).id must beEqualTo("1")
      events(1).id must beEqualTo("2")
    }

    "given a variety of messages then build multi-message events for the known message types" in {
      object MyEventConsolidator extends EventConsolidator {
        override val eventBuilders: Seq[EventBuilder] = List(
          ByMessageTypeEventBuilder("t1"), ByMessageTypeEventBuilder("t2")
        )
      }

      val events = MyEventConsolidator.buildEvents(List(
        Message("t1", "1", "a"), Message("t1", "1", "b"), // first event
        Message("t2", "2", "c"), Message("t2", "2", "d"), // second event
        Message("t2", "3", "e"), Message("t2", "3", "f"), // third event
        Message("t3", "3", "g"), Message("t3", "4", "h")  // these must be ignored
      ))

      events.size must beEqualTo(3)
      events(0).id must beEqualTo("1")
      events(0).description must beEqualTo("ab")
      events(1).id must beEqualTo("2")
      events(1).description must beEqualTo("cd")
      events(2).id must beEqualTo("3")
      events(2).description must beEqualTo("ef")
    }
  }

  object SingleMessageEventBuilder extends EventBuilder {
    override def groupByDiscriminator = m => KnownEvent(m.eventId)
    override def buildEvent(messages: Seq[Message]) = messages match {
      case m :: _ => Some(Event(m.eventId, m.payload))
      case Nil => None
    }
  }

  case class ByMessageTypeEventBuilder(messageType: String) extends EventBuilder {
    override def groupByDiscriminator = {
      case Message(`messageType`, eventId, _) => KnownEvent(eventId)
      case _ => UnknownEvent
    }
    override def buildEvent(messages: Seq[Message]) = messages match {
      case m :: _ => {
        val description = messages.foldRight("")((m, acc) => m.payload ++ acc)
        Some(Event(m.eventId, description))
      }
      case Nil => None
    }
  }
}
