package de.marek.project1;

import dagger.Component;
import de.marek.project1.server.TomcatAppModule;
import de.marek.project1.web.ServletsModule;

import javax.inject.Singleton;


@Singleton
@Component(
        modules = {
                TomcatAppModule.class,
                ServletsModule.class
        })
interface IndexerComponent {
    App webserver();

    // ContinuousIndexer continuousIndexer();

}
