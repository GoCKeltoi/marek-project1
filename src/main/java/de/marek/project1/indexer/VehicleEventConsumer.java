package de.marek.project1.indexer;


import static com.codahale.metrics.MetricRegistry.name;

import static de.marek.project1.indexer.VehicleEsDocAssembler.buildESDocument;

import de.marek.project1.kafka.EventConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;
import com.google.gson.Gson;

import de.marek.project1.elasticsearch.DocumentIndexer;
import de.marek.project1.indexer.vehicle.Vehicle;


class VehicleEventConsumer implements EventConsumer<ConsumerRecord<String, String>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleEventConsumer.class);

    private final DocumentIndexer<VehicleESDoc> documentIndexer;
    private final Gson gson;

    private final MetricRegistry mr;

    VehicleEventConsumer(
            DocumentIndexer<VehicleESDoc> documentIndexer,
            Gson gson,
            MetricRegistry mr
    ) {
        this.documentIndexer = documentIndexer;
        this.gson = gson;
        this.mr = mr;
    }

    @Override
    public void accept(final String index, ConsumerRecord<String, String> record) {
        LOGGER.debug("process event: {}", record);
        try {

            if (isDeleteEvent(record)) {
                documentIndexer.deleteDocument(index, record.key());
            } else {

                final Vehicle vehicle = gson.fromJson(record.value(), Vehicle.class);

                VehicleESDoc doc = buildESDocument(vehicle);
                documentIndexer.indexDocument(index, doc.getVehicleId(), doc);
            }

        } catch (Exception e) {
            mr.counter(name("outbound", "es", "error")).inc();
            LOGGER.error("error to process a message: {}", record, e);
        }
    }

    private static boolean isDeleteEvent(ConsumerRecord<String, String> record){
        return null == record.value() || record.value().length() <=0 || "null".equals(record.value());
    }
}
