package json

import event.Event
import message.Message
import play.api.libs.json._

trait JSONConverter {
  def messageFromJSON(message: String): Option[Message]
  def eventToJSON(events: Seq[Event]): String
}

trait PlayJSONConverter extends JSONConverter {

  implicit val messageJsonFormat: Format[Message] = Json.format[Message]
  implicit val eventJsonFormat:   Format[Event] =   Json.format[Event]

  override def messageFromJSON(message: String): Option[Message] = Json.parse(message).validate[Message] match {
    case s: JsSuccess[Message] => Some(s.get)
    case e: JsError => None
  }

  override def eventToJSON(events: Seq[Event]): String = {
    Json.stringify(Json.toJson(events))
  }
}
