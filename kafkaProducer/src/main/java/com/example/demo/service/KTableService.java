package com.example.demo.service;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KTableService {


	@Autowired
	public void buildPipeline(StreamsBuilder sb) {

		KTable<String, String> leftTable = sb.stream("leftTopic", Consumed.with(Serdes.String(), Serdes.String())).toTable();
		KTable<String, String> rightTable = sb.stream("rightTopic", Consumed.with(Serdes.String(), Serdes.String())).toTable();

		ValueJoiner<String, String, String> stringStringStringValueJoiner =
			(leftValue, rightValue) -> leftValue + rightValue;

		KTable<String, String> joinedTable = leftTable.join(rightTable,
			stringStringStringValueJoiner);
		joinedTable.toStream().to("joinedMsg");

		// 마지막 키를 가지고 다른 파티션에서 그 마지막에 해당하는 키로 값을 보낼때 조인해서 보내줌.
		// left 에 마지막 보낸 키가 a:123 이고, right 에서 마지막 보낸키가 a:456 이면, joinedMsg 토픽에 a:123456 이런식으로 보내줌.
		// 하지만 left 에 마지막 보낸 키가 a:123 이고, right가 b:123 이면 joinedMsg 토픽에는 아무것도 안보내줌.
	}

}
