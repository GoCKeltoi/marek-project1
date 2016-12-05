package de.marek.project1.web;

import javax.inject.Singleton;

import com.codahale.metrics.MetricRegistry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;

import de.marek.project1.indexer.FullIndexBuilder;
import de.marek.project1.Route;

@Module
public class ServletsModule {

    @Provides(type = Provides.Type.SET)
    Route providePing() {
        return new Route("/internal/ping", new PingServlet());
    }

    @Provides(type = Provides.Type.SET)
    Route provideReleaseInfo() {
        return new Route("/internal/release-info", new ReleaseInfoServlet() {});
    }

    @Provides(type = Provides.Type.SET)
    Route provideRecreateIndex(FullIndexBuilder reindexer, MetricRegistry mr) {
        return new Route("/internal/reindex", new ReIndexServlet(reindexer, mr));
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
//                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
    }
}
