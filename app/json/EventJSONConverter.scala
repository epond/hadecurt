package json

import event.Event
import play.api.libs.json.{Format, Json}

object EventJSONConverter {

  implicit val eventJsonFormat: Format[Event] = Json.format[Event]

  def toJSON(events: Seq[Event]): String = {
    Json.stringify(Json.toJson(events))
  }
}
