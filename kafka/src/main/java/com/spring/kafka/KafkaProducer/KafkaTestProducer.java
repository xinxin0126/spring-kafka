package com.spring.kafka.KafkaProducer;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("/kafka")
public class KafkaTestProducer {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(KafkaTestProducer.class);

    private KafkaTemplate kafkaTemplate;

    @Autowired
    private AdminClient adminClient;

    public KafkaTestProducer(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @SneakyThrows
    @RequestMapping("/send_test")
    public void test(@RequestParam("param") String param) {
//        for (int i = 0 ; i < 10; i ++ ) {
//            kafkaTemplate.send("app_new",2,"send_test_value"+i);
//        }

//        for (int i = 0; i < 100; i++) {
//            //发送带有时间戳的消息
//            kafkaTemplate.send("topic.quick.test", 0, System.currentTimeMillis(), i, "send message with timestamp");
//
//            //使用ProducerRecord发送消息
//            ProducerRecord record = new ProducerRecord("topic.quick.test", 1, i, "use ProducerRecord to send message");
//            kafkaTemplate.send(record);
//
//            //使用Message发送消息
//            Map map = new HashMap();
//            map.put(KafkaHeaders.TOPIC, "topic.quick.test");
//            map.put(KafkaHeaders.PARTITION_ID, 2);
//            map.put(KafkaHeaders.MESSAGE_KEY, 0);
//            GenericMessage message = new GenericMessage("use Message to send message", new MessageHeaders(map));
//            kafkaTemplate.send(message);
//        }
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("id",1);
//        jsonObject.put("wordName","测试");

        kafkaTemplate.send("Topic-Type-Test",0,param);
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
        DescribeTopicsResult result = adminClient.describeTopics(Arrays.asList("test"));
        result.all().get().forEach((k, v) -> System.out.println("k: " + k + " ,v: " + v.toString() + "\n"));
    }


}
