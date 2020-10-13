//package com.spring.kafka.streams;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.serialization.Serde;
//import org.apache.kafka.common.serialization.Serdes;
//import org.apache.kafka.streams.KafkaStreams;
//import org.apache.kafka.streams.StreamsBuilder;
//import org.apache.kafka.streams.StreamsConfig;
//import org.apache.kafka.streams.kstream.*;
//import org.apache.kafka.streams.kstream.internals.WindowedDeserializer;
//import org.apache.kafka.streams.kstream.internals.WindowedSerializer;
//
//import java.util.Properties;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.TimeUnit;
//
///**
// * @ClassName TemperatureDemo
// * @Description
// * @Author pengzhansong
// * @Date 2020/9/23
// **/
//public class TemperatureDemo {
//
//    // threshold used for filtering max temperature values
//    private static final int TEMPERATURE_THRESHOLD = 20;
//    // window size within which the filtering is applied
//    private static final int TEMPERATURE_WINDOW_SIZE = 5;
//
//    public static void main(String[] args) throws Exception {
//
//        Properties props = new Properties();
//        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-temperature");
//        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091");
//        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
//        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
//
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
//
//        StreamsBuilder builder = new StreamsBuilder();
//
//        //从topic iot-temperature读取设备发送的传感器信息
//        KStream<String, String> source = builder.stream("topic-TemperatureDemo");
//
//        KStream<Windowed<String>, String> max = source
//                // temperature values are sent without a key (null), so in order
//                // to group and reduce them, a key is needed ("temp" has been chosen)
//                .selectKey(new KeyValueMapper<String, String, String>() {
//                    @Override
//                    public String apply(String key, String value) {
//                        return "temp";
//                    }
//                })
//                .groupByKey()
//                .windowedBy(TimeWindows.of(TimeUnit.SECONDS.toMillis(TEMPERATURE_WINDOW_SIZE)))
//                .reduce(new Reducer<String>() {
//                    @Override
//                    public String apply(String value1, String value2) {
//                        System.out.println("value1=" + value1+ ", value2=" + value2);
//                        JSONObject json = JSON.parseObject(value1);
//                        Integer temperature = json.getInteger("temp");
//                        if (temperature > Integer.parseInt(value2)) {
//                            return temperature.toString();
//                        }
//                        else {
//                            return value2;
//                        }
//                    }
//                })
//                .toStream()
//                //过滤条件就是温度大于20
//                .filter(new Predicate<Windowed<String>, String>() {
//                    @Override
//                    public boolean test(Windowed<String> key, String value) {
//                        System.out.println("key=" + key+ ", value=" + value);
//                        JSONObject json = JSON.parseObject(value);
//                        Integer temperature = json.getInteger("temp");
//                        return temperature > TEMPERATURE_THRESHOLD;
//                    }
//                });
//
//        WindowedSerializer<String> windowedSerializer = new WindowedSerializer<>(Serdes.String().serializer());
//        WindowedDeserializer<String> windowedDeserializer = new WindowedDeserializer<>(Serdes.String().deserializer(), TEMPERATURE_WINDOW_SIZE);
//        Serde<Windowed<String>> windowedSerde = Serdes.serdeFrom(windowedSerializer, windowedDeserializer);
//
//        // need to override key serde to Windowed<String> type
//        max.to("topic-TemperatureDemo-max", Produced.with(windowedSerde, Serdes.String()));
//
//        KafkaStreams streams = new KafkaStreams(builder.build(), props);
//        CountDownLatch latch = new CountDownLatch(1);
//
//        // attach shutdown handler to catch control-c
//        Runtime.getRuntime().addShutdownHook(new Thread("streams-temperature-shutdown-hook") {
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
//
