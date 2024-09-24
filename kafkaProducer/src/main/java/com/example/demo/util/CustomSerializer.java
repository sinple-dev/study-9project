package com.example.demo.util;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.example.demo.vo.PurchaseLogOneProduct;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomSerializer implements Serializer<PurchaseLogOneProduct> {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public byte[] serialize(String topic, PurchaseLogOneProduct data) {
		try {
			if (data == null){
				return null;
			}
			return objectMapper.writeValueAsBytes(data);
		} catch (Exception e) {
			throw new SecurityException("Exception Occured");
		}
	}

	@Override
	public void close() {
	}

}
