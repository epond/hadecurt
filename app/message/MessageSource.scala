package message

trait MessageSource {
  /**
   * produces individual messages as JSON Strings
   */
  def getMessages: Seq[String]
}

trait FileMessageSource extends MessageSource {
  private def allMessages: String = "" // read from file

  private def breakUp(messages: String): Seq[String] = Nil // split at top level of JSON

  override def getMessages: Seq[String] = breakUp(allMessages)
}

trait DummyMessageSource extends MessageSource {
  override def getMessages: Seq[String] = List(
    """{"messageType": "videoView", "eventId": "1", "payload": "a"}""",
    """{"messageType": "videoView", "eventId": "2", "payload": "b"}"""
  )
}