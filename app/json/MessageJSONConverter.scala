package json

import message.Message
import play.api.libs.json._

object MessageJSONConverter {

  implicit val messageJsonFormat: Format[Message] = Json.format[Message]

  def fromJSON(message: String): Option[Message] = Json.parse(message).validate[Message] match {
    case s: JsSuccess[Message] => Some(s.get)
    case e: JsError => None
  }
}
