package com.example.demo.service;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;

import com.example.demo.vo.EffectOrNot;
import com.example.demo.vo.PurchaseLog;
import com.example.demo.vo.PurchaseLogOneProduct;
import com.example.demo.vo.WatchingAdLog;

@Service
public class AdEvaluationService {

	@Autowired
	Producer producer;

	@Autowired
	public void buildPipeline(StreamsBuilder sb) {

		JsonSerializer<EffectOrNot> effectSerializer = new JsonSerializer<>();
		JsonSerializer<PurchaseLog> purchaseLogSerializer = new JsonSerializer<>();
		JsonSerializer<WatchingAdLog> watchingAdLogSerializer = new JsonSerializer<>();
		JsonSerializer<PurchaseLogOneProduct> purchaseLogOneProductSerializer = new JsonSerializer<>();

		JsonDeserializer<EffectOrNot> effectDeserializer = new JsonDeserializer<>(EffectOrNot.class);
		JsonDeserializer<PurchaseLog> purchaseLogDeserializer = new JsonDeserializer<>(PurchaseLog.class);
		JsonDeserializer<WatchingAdLog> watchingAdLogDeserializer = new JsonDeserializer<>(WatchingAdLog.class);
		JsonDeserializer<PurchaseLogOneProduct> purchaseLogOneProductDeserializer = new JsonDeserializer<>(
			PurchaseLogOneProduct.class);

		Serde<EffectOrNot> effectSerde = Serdes.serdeFrom(effectSerializer, effectDeserializer);
		Serde<PurchaseLog> purchaseLogSerde = Serdes.serdeFrom(purchaseLogSerializer, purchaseLogDeserializer);
		Serde<WatchingAdLog> watchingAdLogSerde = Serdes.serdeFrom(watchingAdLogSerializer, watchingAdLogDeserializer);
		Serde<PurchaseLogOneProduct> purchaseLogOneProductSerde = Serdes.serdeFrom(purchaseLogOneProductSerializer,
			purchaseLogOneProductDeserializer);

		KTable<String, WatchingAdLog> adTable = sb.stream("AdLog", Consumed.with(Serdes.String(), watchingAdLogSerde))
			.selectKey((k, v) -> v.getUserId() + "_" + v.getProductId())
			.toTable(Materialized.<String, WatchingAdLog, KeyValueStore<Bytes, byte[]>>as("adStore")
				.withKeySerde(Serdes.String())
				.withValueSerde(watchingAdLogSerde));

		// 100만원보다 작은걸로 필터링
		KStream<String, PurchaseLog> purchaseStream = sb.stream("OrderLog",
				Consumed.with(Serdes.String(), purchaseLogSerde))
			.filter((k, v) -> v.getPrice() < 1000000);

		purchaseStream.foreach((k, v) -> {
			for (String prodId : v.getProductId()) {
				PurchaseLogOneProduct purchaseLogOneProduct = new PurchaseLogOneProduct();
				purchaseLogOneProduct.setUserId(v.getUserId());
				purchaseLogOneProduct.setProductId(prodId);
				purchaseLogOneProduct.setOrderId(v.getOrderId());
				purchaseLogOneProduct.setPrice(v.getPrice());
				purchaseLogOneProduct.setPurchasedDt(v.getPurchasedDt());

				producer.sendJoinedMsg("oneProduct", purchaseLogOneProduct);

			}
		});

		KTable<String, PurchaseLogOneProduct> purchaseLogOneProductKTable = sb.stream("oneProduct",
				Consumed.with(Serdes.String(), purchaseLogOneProductSerde))
			.selectKey((k, v) -> v.getUserId() + "_" + v.getProductId())
			.toTable(Materialized.<String, PurchaseLogOneProduct, KeyValueStore<Bytes, byte[]>>as(
					"purchaseLogOneProductStore")
				.withKeySerde(Serdes.String())
				.withValueSerde(purchaseLogOneProductSerde));

		ValueJoiner<WatchingAdLog, PurchaseLogOneProduct, EffectOrNot> stringStringStringValueJoiner = (leftValue, rightValue) -> {
			EffectOrNot effectOrNot = new EffectOrNot();
			effectOrNot.setUserId(leftValue.getUserId());
			effectOrNot.setAdId(leftValue.getAdId());
			return effectOrNot;
		};

		adTable.join(purchaseLogOneProductKTable, stringStringStringValueJoiner)
			.toStream().to("AdEvaluationComplete", Produced.with(Serdes.String(), effectSerde));

	}

}
