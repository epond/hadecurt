package event

import message.Message

/**
 * Builds an event to represent an individual view of a video.
 * A video view event consists of exactly one message with a message type of 'videoView'.
 */
object VideoViewEventBuilder extends EventBuilder {

  override def groupByDiscriminator = ???

  override def buildEvent(messages: Seq[Message]) = ???

}
