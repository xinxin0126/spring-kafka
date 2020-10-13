//package com.spring.kafka.streams;
//
//import com.spring.kafka.pojo.Test1;
//import com.spring.kafka.pojo.Test2;
//import com.spring.kafka.pojo.Test3;
//import com.spring.kafka.utils.StreamsSerdes;
//import org.apache.kafka.common.serialization.Serde;
//import org.apache.kafka.common.serialization.Serdes;
//import org.apache.kafka.streams.*;
//import org.apache.kafka.streams.kstream.*;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.*;
//
///**
// * @ClassName StreamsDemo
// * @Description
// * @Author pengzhansong
// * @Date 2020/9/21
// **/
//@RestController
//@RequestMapping("/streams")
//public class StreamsDemo {
//
//
//    /**
//     * 简单实现kafka streams 流程
//     * @param args
//     */
//    public static void main(String[] args) {
//        String input = "kafka.test.streams.input";   //输入 topic
//        String output = "kafka.test.streams.output";  //输出 topic
//
//        Properties properties = new Properties();
//
//        // 应用的ID在集群内必须是唯一的
//        properties.put(StreamsConfig.APPLICATION_ID_CONFIG,"test");
//
//        //服务器地址可以是单个服务器和端口，也可以是由逗号分隔的多个服务器和端口
//        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9091");
//
//        //使用Serdes类创建序列化/反序列化所需的Serde实例  Serdes类为以下类型提供默认的实现：String、Byte array、Long、Integer和Double。
//        Serde<String> stringSerde = Serdes.String();
//
//        Serde<Test1> test1Serde = StreamsSerdes.PurchaseSerde();
//
////        Serde<Test2> test2Serde = StreamsSerdes.PurchasePatternSerde();
////
////        Serde<Test3> test3Serde = StreamsSerdes.RewardAccumulatorSerde();
//
//
//        // 每个流应用程序都实现并执行至少一个拓扑。
//        StreamsBuilder builder = new StreamsBuilder();
//
//
//        //第一个 处理器
//        KStream<String, Test1> purchaseKStream = builder
//                // 从事务topic读取消息，使用自定义序列化/反序列化
//                .stream("Test1", Consumed.with(stringSerde, test1Serde))
////                // 使用KStream.mapValues方法隐藏每个信用卡信息
//                .mapValues(p -> Test1.builder().wordName("宋鹏展").id(30).build());
//
//        purchaseKStream.to("app_new", Produced.with(stringSerde, test1Serde));
//
//
//
////        //第二个 处理器
////        KStream<String, Test2> patternKStream = purchaseKStream
////                // 通过自定义PurchasePattern类抽取zip code
////                .mapValues(purchase -> Test2.builder().build());
////        // 把结果发送到另外一个负责分析模式的topic
////        patternKStream.to("Test2", Produced.with(stringSerde, test2Serde));
////
////
////        //第三个 处理器
////        KStream<String, Test3> rewardsKStream = purchaseKStream
////                // 通过自定义RewardAccumulator类计算奖励积分
////                .mapValues(purchase -> Test3.builder().build());
////        // 把结果发送到另外一个负责处理积分的topic
////        rewardsKStream.to("Test3", Produced.with(stringSerde, test3Serde));
//
//
//        // 直接把数据发送到另外一个负责保存数据的topic
////        purchaseKStream.to("TestAll", Produced.with(stringSerde, test1Serde));
////
//        //  过滤  数据
////        KStream<String, Test1> filteredKStream = purchaseKStream
////                // 使用KStream.filter方法过滤小额消费
////                .filter((key, purchase) -> purchase.getId() > 5);
////        // 把数据发送到另外一个负责保存数据的topic
////        filteredKStream.to("purchases", Produced.with(stringSerde, test1Serde));
////
////
////        //KStream.branch方法创建分支流
////        KStream<String, Test1>[] kstreamByDept = purchaseKStream.branch(
////                (key, purchase) -> purchase.getWordName().equalsIgnoreCase("coffee"),
////                (key, purchase) -> purchase.getWordName().equalsIgnoreCase("electronics"));
////        // 把数据发送到相应的topics
////        kstreamByDept[0].to("coffee", Produced.with(stringSerde, test1Serde));
////        kstreamByDept[1].to("electronics", Produced.with(stringSerde, test1Serde));
////
////
////        // KStream.selectKey方法可以为数据生成新的key值
////        // 在过滤方法后链接selectKey方法生成新的KStream<Long, Purchase>实例
////        KStream<Long, Test1> selectKeyKStream = purchaseKStream
////                .filter((key, purchase) ->purchase.getId()  > 5.00)
////                // 使用新的key值
////                .selectKey((key, purchase) -> (long)purchase.getId());
////        // 把数据发送到另外一个负责保存数据的topic，注意key值是Long类型
////        selectKeyKStream.to("purchases", Produced.with(Serdes.Long(), test1Serde));
////
////
////        // 写入 关系型数据库
////        // 过滤指定商店的消费数据
////        purchaseKStream.filter((key, purchase) -> purchase.getWordName().equals("ceshi"));
////
////        //   可以通过foreach 的方式 写入数据库
////                // 使用KStream.foreach方法对每一个数据执行操作，这里使用SecurityDBService保存数据
////                // .foreach((key, purchase) -> SecurityDBService.saveRecord(purchase.getPurchaseDate(),
//////                        purchase.getEmployeeId(), purchase.getItemPurchased()));
////
////
////        KStream<String, String> simpleFirstStream = builder
////                .stream("topic.quick.demo", Consumed.with(stringSerde, stringSerde));
//        // 使用KStream.mapValues 将输入数据流以 abc: 拆分获取下标为 1 字符串
////        KStream<String, String> upperSplitStream = simpleFirstStream.mapValues(line -> line.split(":")[3]);
//
////        KStream<String, String> upperCasedStream = simpleFirstStream.mapValues(line -> line.toLowerCase());
//
//        // 把转换结果输出到另一个topic
////        upperCasedStream.to("Test1", Produced.with(stringSerde, stringSerde));
//
//        //创建和启动KStream
//        KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), properties);
//        kafkaStreams.start();
////
////
//////        patternKStream.print(Printed.<String, Test2>toSysOut().withLabel("Test2"));
//////        rewardsKStream.print(Printed.<String, Test3>toSysOut().withLabel("Test3"));
////        purchaseKStream.print(Printed.<String, Test1>toSysOut().withLabel("Test1"));
//
//    }
//
//
//
//
//}
