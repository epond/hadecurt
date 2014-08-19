package event

import message.{KnownEvent, UnknownEvent, Message}

trait EventConsolidator {

  /**
   * These are the kinds of event that the system knows how to build.
   */
  val eventBuilders: Seq[EventBuilder]

  /**
   * Takes a sequence of messages and translates them into events according to the list of EventBuilders.
   *
   * @param messages raw messages of all types
   * @return events as a result of combining related messages
   */
  def buildEvents(messages: Seq[Message]): Seq[Event] = {
    val applyEventBuilder: (EventBuilder, (Seq[Event], Seq[Message])) => (Seq[Event], Seq[Message]) = {
      case (builder, (events, messages)) => {
        val groupedMessages = messages.groupBy(builder.groupByDiscriminator)
        val messageGroups = groupedMessages.filter{
          case (KnownEvent(_), _) => true
          case (UnknownEvent, _)  => false
        }
        val newEvents = messageGroups
          .map{case (eventId, eventMsgs) => builder.buildEvent(eventMsgs)}
          .collect{case Some(e) => e}
          .toList
          .sortBy(_.id)
        val remainingMessages = (groupedMessages get UnknownEvent) match {
          case Some(m) => m
          case None => Nil
        }
        (newEvents ++ events, remainingMessages)
      }
    }

    eventBuilders.foldRight[(Seq[Event], Seq[Message])]((Nil, messages))(applyEventBuilder)._1
  }
}

object EventConsolidatorImpl extends EventConsolidator with VideoEventBuilders
