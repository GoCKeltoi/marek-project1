package de.marek.project1.kafka;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import de.marek.project1.config.Config;

@Module
public class KafkaModule {
// mobile.ad.publish.service.kafka.topic
    // kafka.databroker.mirror.host
    @Provides @Singleton
    TopicConnectionFactory provideTopicConnectionFactory() {
        return new TopicConnectionFactory(Config.mustExist("kafka.databroker.mirror.host"),Config.mustExist("mobile.ad.publish.service.kafka.topic") );
    }

}
