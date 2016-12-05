package de.marek.project1.indexer;

import javax.inject.Named;
import javax.inject.Singleton;

import de.marek.project1.elasticsearch.DocumentIndexer;
import de.marek.project1.elasticsearch.DocumentIndexerImpl;
import de.marek.project1.kafka.TopicConnectionFactory;
import de.marek.project1.util.MonitoringProxy;
import org.elasticsearch.client.Client;

import com.codahale.metrics.MetricRegistry;
import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;

import de.marek.project1.elasticsearch.EsAliasResolver;


@Module
public class IndexerModule {

    @Provides
    @Singleton
    VehicleEventConsumer provideVehicleEventConsumer(
            @Named("vehicleIndexer") DocumentIndexer<VehicleESDoc> docIndexer,
            Gson gson,
            MetricRegistry mr
    ) {
        return new VehicleEventConsumer(docIndexer, gson, mr);
    }

    @Provides
    @Singleton
    @Named("vehicleIndexer")
    DocumentIndexer<VehicleESDoc> provideVehicleEsIndexer(
            Client esclient,
            Gson gson,
            MetricRegistry mr
    ) {
        return MonitoringProxy.<DocumentIndexerImpl>builder()
                .metricRegistry(mr)
                .namespace("outbound.ElasticSearch")
                .clazz(DocumentIndexerImpl.class)
                .src(new DocumentIndexerImpl("vehicle", esclient, gson))
                .build();
    }

    @Provides
    @Singleton
    FullIndexBuilder provideFullIndexBuilder(
            EsAliasResolver aliasResolver,
            Client client,
            TopicConnectionFactory tcf
    ) {
        return new FullIndexBuilderImpl(aliasResolver, client, tcf);
    }

    @Provides
    @Singleton
    ContinuousIndexer provideContinuousIndexer(
            EsAliasResolver aliasResolver,
            TopicConnectionFactory tcf,
            VehicleEventConsumer vec,
            MetricRegistry mr
    ) {
        return new ContinuousIndexer(aliasResolver, tcf, vec, mr);
    }



}
