package com.example.demo.service;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

	@KafkaListener(topics = "fastcampus", groupId = "spring")
	public void consume(String message) {
		System.out.println("Consumed message: " + message);
	}

}
