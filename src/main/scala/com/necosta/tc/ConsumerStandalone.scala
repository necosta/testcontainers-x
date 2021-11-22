package com.necosta.tc

import cats.effect.{ExitCode, IO, IOApp}
import fs2.kafka._

object ConsumerStandalone extends IOApp with Utils {

  override def run(args: List[String]): IO[ExitCode] = {
    val consumerSettings =
      ConsumerSettings[IO, String, String]
        .withAutoOffsetReset(AutoOffsetReset.Earliest)
        .withBootstrapServers(Workflow.Broker)
        .withGroupId(ConsumerGroupId)

    KafkaConsumer
      .stream(consumerSettings)
      .subscribeTo(Workflow.TopicName)
      .partitionedRecords
      .map { _.evalMap { committable =>
          IO(println(s"Processing record: ${committable.record}"))
        }
      }
      .parJoinUnbounded
      .compile
      .drain
      .as(ExitCode.Success)
  }
}
