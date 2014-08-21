package service

import config.AppConfig
import config.AppConfig._
import event.Event

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait EnrichmentService {
  def enrich(event: Event): ReaderTFuture[AppConfig, Event]
}

trait EnrichmentServiceImpl extends EnrichmentService {
  override def enrich(event: Event) = ReaderTFuture[AppConfig, Event] { config =>
    Future(Event(event.id, event.description ++ " with sugar on top"))
  }
}