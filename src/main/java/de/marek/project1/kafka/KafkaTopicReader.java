package de.marek.project1.kafka;

import static com.codahale.metrics.MetricRegistry.name;

import java.time.Duration;
import java.util.function.Predicate;

import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;

import de.marek.project1.config.Config;

public class KafkaTopicReader<ConsumerRecord> {

    private static final Logger logger = LoggerFactory.getLogger(KafkaTopicReader.class);

    private final TopicConnectionFactory tcf;
    private final MetricRegistry mr;
    private final Duration pollTimeout;

    public KafkaTopicReader(TopicConnectionFactory tcf, MetricRegistry mr) {
        this.tcf = tcf;
        this.mr = mr;
        this.pollTimeout = Duration.ofSeconds(Config.get("pollTimeout", 20));
    }

    public void consume(
            String groupId,
            Predicate<String> continueCondition,
            EventConsumer sink
            ) {

        logger.info("Starting consumer with group id: {}", groupId);
        try (final TopicConnection client = tcf.resumingClient(groupId)) {

            // process as long as the group id is actual
            while (continueCondition.test(groupId)) {
                ConsumerRecords records = client.poll(pollTimeout.toMillis());
                logger.debug("got {} event(s)", records.count());
                if(!records.isEmpty()){
                    // pour all messages into sink
                    // TODO: retry if accept fails
                    records.forEach(r -> sink.accept(groupId,r));
                    // Count index events
                    mr.meter(name("inbound", "kafka", groupId)).mark(records.count());
                    try {
                        client.commitSync();
                    } catch (CommitFailedException e) {
                        logger.warn("unable to commit consumer offset", e);
                    }
                }
            }
        } finally {
            logger.info("Consumer for group id {} stopped", groupId);
        }
    }

}
