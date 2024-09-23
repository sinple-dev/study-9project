package com.example.demo.service;

import java.time.Duration;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StreamService {

	private static final Serde<String> STRING_SERDE = Serdes.String();

	@Autowired
	public void buildPipeline(StreamsBuilder sb) {

		// KStream<String, String> kStream = sb.stream("fastcampus", Consumed.with(STRING_SERDE, STRING_SERDE));
		// // fastcampus 라는 토픽을 읽어서, 5글자 이상인것만 필터링해서 targetTopic 으로 보내겠다.
		// kStream.print(Printed.toSysOut());
		// kStream.filter((k, v) -> v.length() > 5).to("targetTopic");

		KStream<String, String> leftStream = sb.stream("leftTopic", Consumed.with(STRING_SERDE, STRING_SERDE))
			.selectKey((k, v) -> v.split(",")[0]);
		KStream<String, String> rightStream = sb.stream("rightTopic", Consumed.with(STRING_SERDE, STRING_SERDE))
			.selectKey((k, v) -> v.split(",")[0]);

		leftStream.print(Printed.toSysOut());
		rightStream.print(Printed.toSysOut());

		// Duration.ofMinutes(1) 설정한 시간내에 두개에 토픽에서 데이터가 들어와야 joinedmsg토픽으로 보내줌.
		ValueJoiner<String, String, String> stringStringStringValueJoiner =
			(leftValue, rightValue) -> leftValue + rightValue;

		KStream<String, String> joinedStream = leftStream.join(rightStream,
			stringStringStringValueJoiner,
			JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofMinutes(1)));

		joinedStream.print(Printed.toSysOut());
		joinedStream.to("joinedMsg");

	}



}
