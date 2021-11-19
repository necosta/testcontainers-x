package com.necosta.tc

import cats.effect.{ExitCode, IO, IOApp}
import fs2.kafka._

object ProducerStandalone extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val events = fs2.Stream.emits(
      List(
        ("8f68ba9c-241e-44c3-b552-841612f5ec4e", "This is the first message."),
        ("fd79c027-a6fa-4f00-86d4-3a9ec90115e6", "A second message."),
        ("e47b17fb-72c9-4cf2-b6db-226f259b6c6a", "And yet a third message.")
      )
    )

    val producerSettings = ProducerSettings[IO, String, String]
      .withBootstrapServers(Workflow.Broker)

    KafkaProducer
      .stream(producerSettings)
      .flatMap { producer =>
        events
          .map(v => ProducerRecords.one(ProducerRecord(Workflow.TopicName, v._1, v._2)))
          .evalMap(producer.produce)
      }
      .compile
      .drain
      .as(ExitCode.Success)
  }
}
