package service

import event.Event

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait EnrichmentService {
  def enrich(event: Event): Future[Event]
}

trait EnrichmentServiceImpl extends EnrichmentService {
  override def enrich(event: Event) = Future {
    Event(event.id, event.description ++ " with sugar on top")
  }
}