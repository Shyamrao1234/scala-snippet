package kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import java.util.Properties

object Producer extends App {

  val properties = new Properties()
  val topic = "test-topic"

  properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

  val kafkaProducer = new KafkaProducer[String, String](properties)

  for {i <- 1 to 10} {
    val record = new ProducerRecord[String, String](topic ,s"key-${i}", s"value-${i}")
    kafkaProducer.send(record)
  }

  kafkaProducer.close()


}


