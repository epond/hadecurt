package message

sealed trait MessageGroupKey
case class KnownEvent(eventId: String) extends MessageGroupKey
case object UnknownEvent extends MessageGroupKey