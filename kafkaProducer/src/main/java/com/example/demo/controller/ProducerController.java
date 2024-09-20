package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.Producer;

@RestController
public class ProducerController {

	Producer producer;


	@Autowired
	ProducerController(Producer producer) {
		this.producer = producer;
	}


	@PostMapping("/message")
	public String PublishMessage(@RequestParam String msg) {
		return "Message Published";
	}

}
