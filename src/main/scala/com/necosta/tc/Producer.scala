package com.necosta.tc

import cats.effect.{Async, Resource}
import fs2.kafka.{KafkaProducer, ProducerSettings}

object Producer {
  def connection[F[_]: Async](broker: String): Resource[F, KafkaProducer[F, String, String]] = {

    val producerSettings = ProducerSettings[F, String, String].withBootstrapServers(broker)

    KafkaProducer.resource(producerSettings)
  }
}
