package com.necosta.tc

import cats.effect._
import cats.implicits._
import fs2.kafka._
import org.apache.kafka.clients.admin.{NewPartitions, NewTopic}
import org.apache.kafka.common.acl._
import org.apache.kafka.common.resource.{
  PatternType,
  ResourcePattern,
  ResourceType
}

object KafkaAdmin extends Utils {

  private def kafkaAdminClientResource[F[_]: Async](bootstrapServers: String): Resource[F, KafkaAdminClient[F]] =
    KafkaAdminClient.resource[F](AdminClientSettings(bootstrapServers))

  def topicOperations[F[_]: Async]: F[Unit] =
    kafkaAdminClientResource[F](Broker).use { client =>
      for {
        topicNames <- client.listTopics.names
        _ <- client.describeTopics(topicNames.toList)
        _ <- client.createTopic(new NewTopic("new-topic", 1, 1.toShort))
        _ <- client.createTopics(new NewTopic("newer-topic", 1, 1.toShort) :: Nil)
        _ <- client.createPartitions(Map("new-topic" -> NewPartitions.increaseTo(4)))
        _ <- client.deleteTopic("new-topic")
        _ <- client.deleteTopics("newer-topic" :: Nil)
      } yield ()
    }

  def aclOperations[F[_]: Async]: F[Unit] =
    kafkaAdminClientResource[F](Broker).use { client =>
      for {
        describedAcls <- client.describeAcls(AclBindingFilter.ANY)
        aclEntry = new AccessControlEntry(
          "User:ANONYMOUS",
          "*",
          AclOperation.DESCRIBE,
          AclPermissionType.ALLOW
        )
        pattern = new ResourcePattern(ResourceType.TOPIC, "topic1", PatternType.LITERAL)
        acl = new AclBinding(pattern, aclEntry)
        _ <- client.createAcls(List(acl))
        _ <- client.deleteAcls(List(AclBindingFilter.ANY))
      } yield ()
    }

}
