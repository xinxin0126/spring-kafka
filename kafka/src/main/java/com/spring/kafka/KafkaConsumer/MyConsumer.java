package com.spring.kafka.KafkaConsumer;

import com.spring.kafka.utils.MQDict;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.Collections;
import java.util.Properties;

/**
 * @ClassName MyConsumer
 * @Description   消费者手动确认消息/偏移量 demo
 * @Author pengzhansong
 * @Date 2020/9/18
 **/
@Slf4j
public class MyConsumer {

    public static void main(String[] args){

        Properties properties = new Properties();
        properties.put("bootstrap.servers","localhost:9091");
        properties.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id","DemoConsumerGroup");

        //默认值为latest，当消费者读取的分区没有偏移量或偏移量无效时，消费者将从最新的记录开始读
        //当一个消费group第一次订阅主题时，符合这种情况，在Consumer启动之前，Producer生产的数据不会被读取
        //置为earliest，表示从分区起始位置读取消息
        properties.put("auto.offset.reset","earliest");

        //设置手动提交消息偏移
        properties.put("enable.auto.commit","false");

        //一次拉取的最大消息条数
        properties.put("max.poll.records",10);


        org.apache.kafka.clients.consumer.KafkaConsumer<String,String> consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<String, String>(properties);

        consumer.subscribe(Collections.singletonList("test"));

        int count = 0;
        try {
            while (true) {

                ConsumerRecords<String, String> records = consumer.poll(MQDict.CONSUMER_POLL_TIME_OUT);
                for (ConsumerRecord<String, String> record : records) {
                    if ("key10".equals(record.key())) {
                        System.out.println("业务逻辑"+"revice: key ===" + record.key() + " value ====" + record.value() + " topic ===" + record.topic());
                        //提交偏移量
                        consumer.commitSync();

                        consumer.close();
                        break;
                    }

                    log.info("revice: key ===" + record.key() + " value ====" + record.value() + " topic ===" + record.topic());
                }
                log.info("==================");
                log.info("==================");
                log.info("==================");
                log.info("==================");
                log.info("==================");

//                count++;
//                if (count == 5){
//                    log.info("commitSync  提交偏移量：" + count);
//                    consumer.commitSync();
//                    consumer.close();
//                    break;
//                }

                System.out.println(count);
            }
        } catch (Exception e) {
            System.err.println("异常:"+e);

        } finally {
            consumer.close();
        }
    }
}
