package de.marek.project1.kafka;

import java.util.function.BiConsumer;

public interface EventConsumer<V> extends BiConsumer<String, V> {
}
