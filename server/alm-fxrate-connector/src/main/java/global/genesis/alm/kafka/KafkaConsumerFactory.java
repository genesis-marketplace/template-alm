package global.genesis.alm.kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

import static org.apache.kafka.clients.CommonClientConfigs.SECURITY_PROTOCOL_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;


public class KafkaConsumerFactory {

    final Properties properties;

    public KafkaConsumerFactory(final String bootstrapServers, final String consumerGroupId) {

        properties = new Properties();
        properties.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(GROUP_ID_CONFIG, consumerGroupId);
        properties.put(SECURITY_PROTOCOL_CONFIG, SecurityProtocol.SSL.name);
    }

    public KafkaConsumer<String, String> generateKafkaConsumer() {
        return new KafkaConsumer<>(properties);
    }

}
