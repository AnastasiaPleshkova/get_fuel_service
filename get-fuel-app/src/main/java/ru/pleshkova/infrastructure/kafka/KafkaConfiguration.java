package ru.pleshkova.infrastructure.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.pleshkova.business.model.dto.FuelMessage;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {
    private static final String FUEL_TOPIC_NAME = "fuel.records";

    @Bean
    public ProducerFactory<String, FuelMessage> producerFactory() {
        return new DefaultKafkaProducerFactory<String, FuelMessage>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, FuelMessage> fuelExporter(ProducerFactory<String, FuelMessage> producerFactory) {
        final var template = new KafkaTemplate<>(producerFactory);
        template.setDefaultTopic(FUEL_TOPIC_NAME);
        return template;
    }

    private Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:29092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
//        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, )
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

}
