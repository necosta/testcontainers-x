# Test Containers

Playground test containers project using Kafka and FS2

## How-to

* Run integration tests: `sbt it:test`
* Launch Kafka: `docker-compose up`
* Send messages to Kafka: `sbt "runMain com.necosta.tc.ProducerStandalone"`
* Read messages from Kafka: `sbt "runMain com.necosta.tc.ConsumerStandalone"`
* Shut down: `docker-compose down`

## License

See [LICENSE](LICENSE)
