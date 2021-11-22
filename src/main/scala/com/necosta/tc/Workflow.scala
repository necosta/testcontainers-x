package com.necosta.tc

import cats.effect.{ExitCode, IO, IOApp}
import fs2.kafka._

import scala.concurrent.duration._

object Workflow extends IOApp with Utils {

  override def run(args: List[String]): IO[ExitCode] = {

    val consumerSettings =
      ConsumerSettings[IO, String, String]
        .withIsolationLevel(IsolationLevel.ReadCommitted)
        .withAutoOffsetReset(AutoOffsetReset.Earliest) // Start consuming from offset 0
        .withBootstrapServers(Broker)
        .withGroupId(ConsumerGroupId)

    val producerSettings =
      ProducerSettings[IO, String, String](
        keySerializer = Serializer[IO, String],
        valueSerializer = Serializer[IO, String]
      ).withBootstrapServers(Broker)

    val stream =
      KafkaConsumer
        .stream(consumerSettings)
        .subscribeTo(TopicName)
        .records
        .mapAsync(25) { committable =>
          processRecord(committable.record)
            .map(r => {
              val record = ProducerRecord(TopicName, r.key, r.value)
              ProducerRecords.one(record, committable.offset)
            })
        }
        .through(KafkaProducer.pipe(producerSettings))
        .map(_.passthrough)
        .through(commitBatchWithin(100, 5.seconds))

    stream.compile.drain.as(ExitCode.Success)
  }

  private def processRecord(record: ConsumerRecord[String, String]): IO[KafkaRecord] =
    IO.pure(KafkaRecord(record.key, record.value))
}
