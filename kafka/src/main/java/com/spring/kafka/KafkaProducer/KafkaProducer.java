package com.spring.kafka.KafkaProducer;

import lombok.SneakyThrows;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("/kafka")
public class KafkaProducer {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(KafkaProducer.class);

    private KafkaTemplate kafkaTemplate;

    @Autowired
    private AdminClient adminClient;

    public KafkaProducer(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @SneakyThrows
    @RequestMapping("/send_test")
    public void test() {
//        for (int i = 0 ; i < 10; i ++ ) {
//            kafkaTemplate.send("app_new",2,"send_test_value"+i);
//        }

        for (int i = 0; i < 10; i++) {
            //发送带有时间戳的消息
            kafkaTemplate.send("topic.quick.demo", 0, System.currentTimeMillis(), i, "send message with timestamp");

            //使用ProducerRecord发送消息
            ProducerRecord record = new ProducerRecord("topic.quick.demo", 1, i, "use ProducerRecord to send message");
            kafkaTemplate.send(record);

            //使用Message发送消息
            Map map = new HashMap();
            map.put(KafkaHeaders.TOPIC, "topic.quick.demo");
            map.put(KafkaHeaders.PARTITION_ID, 2);
            map.put(KafkaHeaders.MESSAGE_KEY, 0);
            GenericMessage message = new GenericMessage("use Message to send message", new MessageHeaders(map));
            kafkaTemplate.send(message);
        }
    }

    /**
     * 创建topic
     */
    @SneakyThrows
    @RequestMapping("/creatTopic")
    public void creatTopic() {

        NewTopic topic = new NewTopic("topic.quick.initial2", 8, (short) 1);
        adminClient.createTopics(Arrays.asList(topic));
        Thread.sleep(1000);

    }

    /**
     * 查询Topic信息
     */
    @RequestMapping("/selectTopic")
    public void testSelectTopicInfo() throws ExecutionException, InterruptedException {
        DescribeTopicsResult result = adminClient.describeTopics(Arrays.asList("topic.quick.initial"));
        result.all().get().forEach((k, v) -> System.out.println("k: " + k + " ,v: " + v.toString() + "\n"));
    }


}
