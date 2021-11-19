package com.necosta.tc

import cats.effect.{ExitCode, IO, IOApp}
import fs2.kafka._

import scala.concurrent.duration._

object Workflow extends IOApp {

  val Broker = "localhost:9092"
  val TopicName = "topicName"

  override def run(args: List[String]): IO[ExitCode] = {
    def processRecord(record: ConsumerRecord[String, String]): IO[(String, String)] =
      IO.pure(record.key -> record.value)

    val consumerSettings =
      ConsumerSettings[IO, String, String]
        .withAutoOffsetReset(AutoOffsetReset.Earliest)
        .withBootstrapServers(Broker)
        .withGroupId(Consumer.GroupId)

    val producerSettings =
      ProducerSettings[IO, String, String]
        .withBootstrapServers(Broker)

    val stream =
      KafkaConsumer
        .stream(consumerSettings)
        .subscribeTo(TopicName)
        .records
        .mapAsync(25) { committable =>
          processRecord(committable.record)
            .map { case (key, value) =>
              val record = ProducerRecord(TopicName, key, value)
              ProducerRecords.one(record, committable.offset)
            }
        }
        .through(KafkaProducer.pipe(producerSettings))
        .map(_.passthrough)
        .through(commitBatchWithin(100, 5.seconds))

    stream.compile.drain.as(ExitCode.Success)
  }
}
