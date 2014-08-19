package event

import message.{UnknownEvent, KnownEvent, Message}
import org.specs2.mutable.Specification

class VideoViewEventBuilderSpec extends Specification {
  "A VideoViewEventBuilder" should {
    "Convert a 'videoView' message into a video view event" in {
      val msg = Message("videoView", "1", "a")
      VideoViewEventBuilder.groupByDiscriminator(msg) must beEqualTo(KnownEvent("1"))
      VideoViewEventBuilder.buildEvent(List(msg)) must beEqualTo(Some(Event("1", "a")))
    }
    "Use the first message in a group to build the event" in {
      val msg1 = Message("videoView", "1", "a")
      val msg2 = Message("videoView", "1", "b")
      VideoViewEventBuilder.buildEvent(List(msg1, msg2)) must beEqualTo(Some(Event("1", "a")))
    }
    "Do nothing with a message of unknown type" in {
      val msg = Message("somethingElse", "1", "a")
      VideoViewEventBuilder.groupByDiscriminator(msg) must beEqualTo(UnknownEvent)
      VideoViewEventBuilder.buildEvent(List(msg)) must beEqualTo(None)
    }
  }
}
