package com.example.demo.vo;

import lombok.Data;

@Data
public class WatchingAdLog {

	String userId;

	String productId; // pg-0001

	String adId; // ad-101

	String adType; // banner, clip, popup

	String WatchingTime; // 1000

	String watchingDt; // 202301010000

}
