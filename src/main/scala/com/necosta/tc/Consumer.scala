package com.necosta.tc

import cats.effect.{Async, Resource}
import fs2.kafka.{AutoOffsetReset, ConsumerSettings, KafkaConsumer}

object Consumer {

  val GroupId = "group1"

  def connection[F[_]: Async](broker: String): Resource[F, KafkaConsumer[F, String, String]] = {
    val consumerSettings =
      ConsumerSettings[F, String, String]
        .withAutoOffsetReset(AutoOffsetReset.Earliest)
        .withBootstrapServers(broker)
        .withGroupId(GroupId)

    KafkaConsumer
      .resource(consumerSettings)
  }
}
