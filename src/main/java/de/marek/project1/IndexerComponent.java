package de.marek.project1;

import javax.inject.Singleton;

import com.codahale.metrics.health.HealthCheckRegistry;

import dagger.Component;

import de.marek.project1.elasticsearch.ElasticSearchModule;
import de.marek.project1.indexer.FullIndexBuilder;
import de.marek.project1.indexer.IndexerModule;
import de.marek.project1.kafka.KafkaModule;
import de.marek.project1.monitoring.HealthModule;
import de.marek.project1.monitoring.MetricsModule;
import de.marek.project1.server.TomcatAppModule;
import de.marek.project1.web.ServletsModule;
import de.marek.project1.indexer.ContinuousIndexer;

@Singleton
@Component(
        modules = {
        TomcatAppModule.class,
        ServletsModule.class,
        MetricsModule.class,
        HealthModule.class,
        IndexerModule.class,
        ElasticSearchModule.class,
        KafkaModule.class
})
interface IndexerComponent {
    App webserver();

    HealthCheckRegistry healthCheckRegistry();

    FullIndexBuilder fullIndexBuilder();
    ContinuousIndexer continuousIndexer();

}
