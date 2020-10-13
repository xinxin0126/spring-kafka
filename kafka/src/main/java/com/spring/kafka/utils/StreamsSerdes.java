//package com.spring.kafka.utils;
//
//import com.spring.kafka.config.streams.JsonDeserializer;
//import com.spring.kafka.config.streams.JsonSerializer;
//import com.spring.kafka.config.streams.WrapperSerde;
//import com.spring.kafka.pojo.Test1;
//import com.spring.kafka.pojo.Test2;
//import com.spring.kafka.pojo.Test3;
//import org.apache.kafka.common.serialization.Serde;
//
//
///**
// * @ClassName StreamsSerdes
// * @Description
// * @Author pengzhansong
// * @Date 2020/9/22
// **/
//public class StreamsSerdes {
//
//    public static Serde<Test1> PurchaseSerde() {
//        return new Test1();
//    }
//
////    public static Serde<Test2> PurchasePatternSerde() {
////        return new Test2();
////    }
////
////    public static Serde<Test3> RewardAccumulatorSerde() {
////        return  new Test3();
////    }
//
//    public static final class PurchaseSerde extends WrapperSerde<Test1> {
//        public PurchaseSerde() {
//            super(new JsonSerializer<Test1>(), new JsonDeserializer<Test1>(Test1.class));
//        }
//    }
//
////    public static final class PurchasePatternsSerde extends WrapperSerde<Test2> {
////        public PurchasePatternsSerde() {
////            super(new JsonSerializer<Test2>(),
////                    new JsonDeserializer<Test2>(Test2.class));
////        }
////    }
////
////    public static final class RewardAccumulatorSerde extends WrapperSerde<Test3> {
////        public RewardAccumulatorSerde() {
////            super(new JsonSerializer<Test3>(),
////                    new JsonDeserializer<Test3>(Test3.class));
////        }
////    }
//
//
//}
