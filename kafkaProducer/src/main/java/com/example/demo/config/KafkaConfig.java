package com.example.demo.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaConfig {

	@Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
	public KafkaStreamsConfiguration kStreamsConfigs() {
		Map<String, Object> myConfig = new HashMap<>();
		// ip 여러개 넣을수 있음.
		myConfig.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092, localhost:9093, localhost:9094");
		myConfig.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		myConfig.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		// 몇개의 쓰레드를 돌릴것이냐.
		// 파티션에 개수도 의미가 있음.
		// 노드를 3개를 만들었으니까, 파티션도 3개로 하자.
		// kafka 를 3개 만들었으니까 쓰레드도 3개로
		myConfig.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 3);
		return new KafkaStreamsConfiguration(myConfig);
	}

	@Bean
	public KafkaTemplate<String, Object> kafkaTempLate() {
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean
	public ProducerFactory<String, Object> producerFactory() {
		Map<String, Object> myConfig = new HashMap<>();
		myConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "");
		myConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		myConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, com.example.demo.util.CustomSerializer.class);
		return new DefaultKafkaProducerFactory<>(myConfig);
	}




	// @Bean
	// public ConsumerFactory<String, Object> consumerFactory() {
	// 	Map<String, Object> myConfig = new HashMap<>();
	// 	myConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "");
	// 	myConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	// 	myConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	// 	return new DefaultKafkaConsumerFactory<>(myConfig);
	// }
	//
	// @Bean
	// public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
	// 	ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
	// 	factory.setConsumerFactory(consumerFactory());
	// 	return factory;
	// }

}
