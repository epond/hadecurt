package service

import event.Event

trait EnrichmentService {
  def enrich(event: Event): Event
}

trait EnrichmentServiceImpl extends EnrichmentService {
  override def enrich(event: Event): Event = {
    // TODO: Return a future instead of blocking
    Event(event.id, event.description ++ " with sugar on top")
  }
}