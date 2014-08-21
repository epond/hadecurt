package config

import scala.concurrent.{ExecutionContext, Promise, Future}
import ExecutionContext.Implicits.global

import scalaz._
import Scalaz._
// needed for implicit scalaz.Functor[scala.concurrent.Future]
import scalaz.contrib.std._

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

    // We can't use Future.sequence on a ReaderTFuture so let's define our own version of sequence
    // Just like Erik Meijer does in https://class.coursera.org/reactive-001/lecture/55
    def sequence[A, T](fs: List[ReaderTFuture[A, T]]): ReaderTFuture[A, List[T]] = {
      val successful = Promise[List[T]]()
      successful.success(Nil)
      // The seed value of the fold must be a future with an empty list, wrapped in a ReaderTFuture
      val seed = ReaderTFuture[A, List[T]] { config => successful.future }
      fs.foldRight(seed) {
        (f, acc) => for {x <- f; xs <- acc} yield x :: xs
      }
    }
  }

}