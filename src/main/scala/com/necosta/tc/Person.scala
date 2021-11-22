package com.necosta.tc

import cats.implicits._
import vulcan.Codec

final case class Person(name: String, age: Option[Int])