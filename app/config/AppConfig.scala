package config

import scala.concurrent.Future

import scalaz._
import Scalaz._

/**
 * Application configuration
 *
 * @param messageChunkSize number of messages returned by MessageSource
 */
case class AppConfig(messageChunkSize: Int)

object AppConfig {

  // ReaderTFuture monad combines Reader's ability to read from some configuration once,
  // and Future's ability to express latency.
  type ReaderTFuture[A, B] = ReaderT[Future, A, B]

  // The corresponding companion object describes how to construct from a kleisli function
  object ReaderTFuture extends KleisliFunctions with KleisliInstances {
    def apply[A, B](f: A => Future[B]): ReaderTFuture[A, B] = kleisli(f)
  }

}