package com.necosta.tc

case class KafkaRecord(key: String, value: String) {
  override def toString() = {
    s"key:$key|value:$value"
  }
}
