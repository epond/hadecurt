package event

import message.{KnownEvent, UnknownEvent, Message}

trait EventConsolidator {

  /**
   * These are the kinds of event that the system knows how to build.
   */
  val eventBuilders: List[EventBuilder]

  /**
   * Takes a list of messages and translates them into events according to the list of EventBuilders.
   *
   * @param messages raw messages of all types
   * @return events as a result of combining related messages
   */
  def buildEvents(messages: List[Message]): List[Event] = {
    val applyEventBuilder: (EventBuilder, (List[Event], List[Message])) => (List[Event], List[Message]) = {
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

    eventBuilders.foldRight[(List[Event], List[Message])]((Nil, messages))(applyEventBuilder)._1
  }
}

trait EventConsolidatorImpl extends EventConsolidator with VideoEventBuilders
