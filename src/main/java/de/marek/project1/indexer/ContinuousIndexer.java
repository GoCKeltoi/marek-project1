package de.marek.project1.indexer;

import java.time.Duration;

import de.marek.project1.config.Config;
import de.marek.project1.elasticsearch.EsAliasResolver;
import de.marek.project1.elasticsearch.EsAliasSamePredicate;
import de.marek.project1.kafka.KafkaTopicReader;
import de.marek.project1.kafka.TopicConnectionFactory;
import de.marek.project1.util.Resolver;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;

public class ContinuousIndexer {

    private static final Logger logger = LoggerFactory.getLogger(ContinuousIndexer.class);

    private final EsAliasResolver aliasResolver;
    private final TopicConnectionFactory tcf;
    private final VehicleEventConsumer vec;
    private final MetricRegistry mr;

    public ContinuousIndexer(EsAliasResolver aliasResolver, TopicConnectionFactory tcf, VehicleEventConsumer vec, MetricRegistry mr) {
        this.aliasResolver = aliasResolver;
        this.tcf = tcf;
        this.vec = vec;
        this.mr = mr;
    }

    public void start() {
        while (true) {
            try {
                final String index = Resolver.withRetry(aliasResolver, Duration.ofSeconds(Config.get("aliasResolverRetry", 60)));
                Thread.currentThread().setName("realtime-indexer-" + index);
                final EsAliasSamePredicate aliasSame = new EsAliasSamePredicate(aliasResolver);

                final KafkaTopicReader<ConsumerRecord> topic = new KafkaTopicReader<>(tcf, mr);

                topic.consume(index, aliasSame, vec);
            } catch (WakeupException ex) {
                break;
            }
        }

    }

}
