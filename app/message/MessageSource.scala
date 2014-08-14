package message

trait MessageSource {
  /**
   * produces individual messages as JSON Strings
   */
  def getMessages: Seq[String]
}

trait FileMessageSource extends MessageSource {
  private def allMessages: String = ??? // read from file

  private def breakUp(messages: String): Seq[String] = ??? // split at top level of JSON

  override def getMessages: Seq[String] = breakUp(allMessages)
}