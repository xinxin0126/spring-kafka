package com.spring.kafka.KafkaConsumer;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @ClassName MyListener
 * @Description  消费者分区消费
 * @Author pengzhansong
 * @Date 2020/9/17
 **/
//@Component
@Slf4j
public class MyListener {
    private static final String TPOIC = "topic.quick.demo";


    @KafkaListener(id = "id0" ,containerFactory = "batchFactory",groupId = "test-group-id", topicPartitions = {@TopicPartition(topic = TPOIC, partitions = {"0"})})
    public void listenPartition0(List<ConsumerRecord<?, ?>> records) {
        log.info("Id0 Listener, Thread ID: " + Thread.currentThread().getId());
        log.info("Id0 records size " + records.size());

        for (ConsumerRecord<?, ?> record : records) {
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            log.info("Received: " + record);
            if (kafkaMessage.isPresent()) {
                Object message = record.value();
                String topic = record.topic();

                log.info("p0 Received message : {},topic:{},record.partition:{}", message,topic,record.partition());
            }
        }
    }

    @KafkaListener(id = "id1" ,containerFactory = "batchFactory",groupId = "test-group-id", topicPartitions = {@TopicPartition(topic = TPOIC, partitions = {"1"})})
    public void listenPartition1(List<ConsumerRecord<?, ?>> records) {
        log.info("Id1 Listener, Thread ID: " + Thread.currentThread().getId());
        log.info("Id1 records size " + records.size());

        for (ConsumerRecord<?, ?> record : records) {
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            log.info("Received: " + record);
            if (kafkaMessage.isPresent()) {
                Object message = record.value();
                String topic = record.topic();
                log.info("p1 Received message : {},topic:{},record.partition:{}", message,topic,record.partition());
            }
        }
    }

    @KafkaListener(id = "id2" , containerFactory = "batchFactory",groupId = "test-group-id",topicPartitions = {@TopicPartition(topic = TPOIC, partitions = {"2"})})
    public void listenPartition2(List<ConsumerRecord<?, ?>> records) {
        log.info("Id2 Listener, Thread ID: " + Thread.currentThread().getId());
        log.info("Id2 records size " + records.size());



        for (ConsumerRecord<?, ?> record : records) {
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            log.info("Received: " + record);
            if (kafkaMessage.isPresent()) {
                Object message = record.value();
                String topic = record.topic();
                log.info("p2 Received message : {},topic:{},record.partition:{}", message,topic,record.partition());
            }
        }
    }
}
