//package com.spring.kafka.streams;
//
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.serialization.Serde;
//import org.apache.kafka.common.serialization.Serdes;
//import org.apache.kafka.common.utils.Bytes;
//import org.apache.kafka.streams.KafkaStreams;
//import org.apache.kafka.streams.StreamsBuilder;
//import org.apache.kafka.streams.StreamsConfig;
//import org.apache.kafka.streams.kstream.*;
//import org.apache.kafka.streams.kstream.internals.*;
//import org.apache.kafka.streams.kstream.internals.WindowedSerializer;
//import org.apache.kafka.streams.state.WindowStore;
//
//import java.util.Properties;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.TimeUnit;
//
///**
// * @ClassName TempAggregationSumDemo
// * @Description    聚合 Aggregation--统计总和
// * @Author pengzhansong
// * @Date 2020/9/23
// **/
//public class TempAggregationSumDemo {
//
//    private static final int TEMPERATURE_WINDOW_SIZE = 30;
//
//    public static void main(String[] args) throws Exception {
//
//        Properties props = new Properties();
//        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-key-sum");
//        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091");
//        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
//        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
//
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
//        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
//
//        StreamsBuilder builder = new StreamsBuilder();
//
//        KStream<String, String> source = builder.stream("iot-key");
//        //KStream是一个由键值对构成的抽象记录流，每个键值对是一个独立的单元，即使相同的Key也不会覆盖，类似数据库的插入操作
//        KTable<Windowed<String>, Long> sumWindowed = source
//                .groupByKey()
//                .windowedBy(TimeWindows.of(TimeUnit.SECONDS.toMillis(TEMPERATURE_WINDOW_SIZE)))
//                .aggregate(
//                        new Initializer<Long>() {
//                            @Override
//                            public Long apply() {
//                                return 0L;
//                            }
//                        },
//                        new Aggregator<String, String, Long>() {
//                            @Override
//                            public Long apply(String aggKey, String newValue, Long aggValue) {
//                                System.out.println("aggKey:" + aggKey+ ",  newValue:"  +  newValue +", aggKey:" + aggValue );
//                                Long newValueLong = Long.valueOf(newValue);
//                                long newSum = aggValue.longValue() + newValueLong.longValue();
//                                return Long.valueOf(newSum);
//                            }
//                        },
//                        Materialized.<String, Long, WindowStore<Bytes, byte[]>>as("time-windowed-aggregated-temp-stream-store")
//                                .withValueSerde(Serdes.Long())
//                );
//
//        WindowedSerializer<String> windowedSerializer = new WindowedSerializer<String>() {
//            @Override
//            public byte[] serialize(String topic, Windowed<String> data) {
//                return new byte[0];
//            }
//
//            @Override
//            public byte[] serializeBaseKey(String s, Windowed<String> windowed) {
//                return new byte[0];
//            }
//        };
//
//        TimeWindowedDeserializer<String> windowedDeserializer = new TimeWindowedDeserializer<>(Serdes.String().deserializer(), TEMPERATURE_WINDOW_SIZE);
//        Serde<Windowed<String>> windowedSerde = Serdes.serdeFrom(windowedSerializer, windowedDeserializer);;
//
//        sumWindowed.toStream().to("iot-key-sum", Produced.with(windowedSerde, Serdes.Long()));
//        final KafkaStreams streams = new KafkaStreams(builder.build(), props);
//        final CountDownLatch latch = new CountDownLatch(1);
//
//        // attach shutdown handler to catch control-c
//        Runtime.getRuntime().addShutdownHook(new Thread("streams-key-shutdown-hook") {
//            @Override
//            public void run() {
//                streams.close();
//                latch.countDown();
//            }
//        });
//
//        try {
//            streams.start();
//            latch.await();
//        } catch (Throwable e) {
//            System.exit(1);
//        }
//        System.exit(0);
//    }
//}
