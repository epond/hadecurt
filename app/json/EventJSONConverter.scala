package json

import event.Event

object EventJSONConverter {
  def toJSON(event: Event): String = ???

  def toJSON(events: Seq[Event]): String = ???
}
