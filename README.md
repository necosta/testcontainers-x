# Test Containers

Playground test containers project using Kafka and FS2

## How-to

* Launch Kafka: `docker-compose up`
* Send messages to Kafka: `sbt "runMain com.necosta.tc.ProducerStandalone"`
* Read messages from Kafka: `sbt "runMain com.necosta.tc.ConsumerStandalone"`

## License

See [LICENSE](LICENSE)
