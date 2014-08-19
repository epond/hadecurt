# Hadecurt

Hadecurt is an application that reads a sequence of messages from some kind of source and performs some processing on them in order to display them as a sequence of events. This acts as a window into a data warehouse.

A message source provides a set of generic json messages.
Initially this can be sourced from a file, but eventually it could come from a query to psql or Amazon Redshift.
 * MessageSource trait produces individual messages as JSON Strings.
 * DummyMessageSource has hardcoded messages to aid development.
 * FileMessageSource gets messages from file.

Parse messages with play json inception.
 * MessageJSONConverter parses message JSON string into Message instances

Define event builders that each know how to combine multiple messages into a single event
(methods: groupByDiscriminator, buildEvent).
 * Provide the discriminator function, f, for the standard groupBy function: def groupBy[K](f: (A) ⇒ K): Map[K, List[A]]
 * Build an appropriate Event from a sequence of Message.

Convert a sequence of messages into a sequence of events with EventConsolidator, which is caked with a list
of event builders.

Enrich event with info from two services returning futures.

Configure with ReaderT / Kleisli

Display assembled, enriched events with angular, ui-router, ng-table