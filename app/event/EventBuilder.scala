package event

import message.{MessageGroupKey, Message}

trait EventBuilder {
  /**
   * The discriminator function when performing a groupBy on a List of Message.
   * A key of KnownEvent groups messages for a single event whose type is relevant for this builder.
   * A key of UnknownEvent groups remaining messages that are not relevant to this builder.
   */
  def groupByDiscriminator: Message => MessageGroupKey

  /**
   * Builds an event from a sequence of related messages.
   *
   * @param messages the messages that comprise this event
   * @return The event if it could be built, otherwise None
   */
  def buildEvent(messages: List[Message]): Option[Event]
}
