package com.spring.kafka.KafkaConsumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author pengzhansong
 */
@Component
@Slf4j
public class KafkaConsumer {

    private final KafkaTemplate kafkaTemplate;

    @Autowired
    public KafkaConsumer(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "app_new"
//            ,groupId = "test-group-id"
//            ,concurrency = "6"
    )
    public void consume(ConsumerRecord<?, ?> consumer) {
//        log.info("kakfa分区1监听到消息");
        System.out.println("topic名称:" + consumer.topic() + ",key:" + consumer.key() + ",value:" + consumer.value() +",分区位置:" + consumer.partition()
                + ", 下标" + consumer.offset());
    }

    @KafkaListener(topics = "app_new"
            ,groupId = "test-group-id2",containerFactory = "batchFactory"
    )
    public void consume1(List<ConsumerRecord<?, ?>> consumer) {
//        log.info("kakfa分区2监听到消息");

        List<String> messages = new ArrayList<>();
        for (ConsumerRecord<?, ?> record : consumer) {
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            // 获取消息
            kafkaMessage.ifPresent(o -> messages.add(o.toString()));
        }

        System.out.println("kakfa分区2监听到消息：" + messages);
    }
}
