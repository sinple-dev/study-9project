package com.example.demo.vo;

import java.util.ArrayList;

import lombok.Data;

@Data
public class PurchaseLog {

	String orderId; // od-0001

	String userId;

	ArrayList<String> productId; // pg-0001

	String purchasedDt; // 202301010000

	Long price; // 10000


}
