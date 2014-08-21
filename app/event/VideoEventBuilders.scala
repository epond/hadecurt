package event

import message.{UnknownEvent, Message, KnownEvent}

trait VideoEventBuilders {
  val eventBuilders: List[EventBuilder] = List(
    VideoViewEventBuilder
  )
}

/**
 * Builds an event to represent an individual view of a video.
 * A video view event consists of exactly one message with a message type of 'videoView'.
 */
object VideoViewEventBuilder extends EventBuilder {
  override def groupByDiscriminator = {
    case Message("videoView", eventId, _) => KnownEvent(eventId)
    case _ => UnknownEvent
  }
  override def buildEvent(messages: List[Message]) = messages match {
    case Message("videoView", id, payload) :: _ => Some(Event(id, payload))
    case _ => None
  }
}