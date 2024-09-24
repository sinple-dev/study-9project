package com.example.demo.vo;

import lombok.Data;

@Data
public class PurchaseLogOneProduct {

	String orderId; // od-0001

	String userId;

	String productId; // pg-0001

	String purchasedDt; // 202301010000

	Long price; // 10000
}
