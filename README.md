# Hadecurt

Hadecurt is an application that reads a sequence of messages from some kind of source and performs some processing on
them in order to display them as a sequence of events. This could be used as a window into a data warehouse, but its
intention is so I can play around with some functional concepts such as monadic composition.

A message source provides messages in chunks, the size of which is set in application configuration.
Initially messages can be sourced from a file, but eventually they could come from a query to psql or Amazon Redshift.

 * `MessageSource` trait produces message chunk as a list of message.
 * `HardcodedMessageSource` has hardcoded messages to aid development.
 * `FileMessageSource` gets messages from file.

Perform JSON conversion with play json inception.

 * `JSONConverter` produces event JSON from event instances.

Define event builders that each know how to combine multiple messages into a single event
(methods: `groupByDiscriminator`, `buildEvent`).

 * Provide the discriminator function, `f`, for the standard `groupBy` function:
 `def groupBy[K](f: (A) ⇒ K): Map[K,List[A]]`
 * Build an appropriate `Event` from a sequence of `Message`.

Convert a sequence of messages into a sequence of events with `EventConsolidator`, which is caked with a list
of event builders.

Enrich event with info from service that returns a future.

Configure with `ReaderT` / `Kleisli`

Display assembled, enriched events with angular, ui-router, ng-table