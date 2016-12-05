package de.marek.project1.kafka;

import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TopicConnectionFactory {
    private static final Logger logger = LoggerFactory.getLogger(TopicConnectionFactory.class);

    private final String bootstrapServers;
    private final String topic;
    public TopicConnectionFactory(String bootstrapServers, String topic) {
        this.bootstrapServers = bootstrapServers;
        this.topic = topic;
    }

    public TopicConnection fromBeginningClient(String kafkaGroupId) {
        return client(kafkaGroupId, "earliest");
    }

    public TopicConnection fromEndClient(String kafkaGroupId) {
        return client(kafkaGroupId, "latest");
    }

    public TopicConnection resumingClient(String kafkaGroupId) {
        return client(kafkaGroupId, "none");
    }

    private TopicConnection client(String kafkaGroupId, String autoOffsetReset) {
        logger.info("Creating new Kafka client with group id: {}", kafkaGroupId);
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("max.poll.records", "1024");
        props.put("group.id", kafkaGroupId);
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        props.put("enable.auto.commit", "false");
        props.put("auto.offset.reset", autoOffsetReset);

        KafkaConsumer client = new KafkaConsumer<>(props);
        client.subscribe(Collections.singletonList(topic));

        return new TopicConnectionImpl<>(kafkaGroupId, client);
    }

}
