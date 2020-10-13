package com.spring.kafka.KafkaProducer;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.jackson.JsonObjectDeserializer;

import java.util.Properties;

/**
 * @ClassName MyProdcer
 * @Description
 * @Author pengzhansong
 * @Date 2020/9/18
 **/
@Slf4j
public class MyProdcer {

    public static void main(String[] args) throws InterruptedException {
        //消息实体
        ProducerRecord<String , String> record = null;

        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091");
        //重试，0为不启用重试机制
        props.put(ProducerConfig.RETRIES_CONFIG, 1);
        //控制批处理大小，单位为字节
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        //批量发送，延迟为1毫秒，启用该功能能有效减少生产者发送消息次数，从而提高并发量
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        //生产者可以使用的总内存字节来缓冲等待发送到服务器的记录
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 1024000);
        //键的序列化方式
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        //值的序列化方式
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);



        KafkaProducer<String, String> producer = new org.apache.kafka.clients.producer.KafkaProducer<>(props);

        for (int i = 1; i <= 20; i++) {
//            record = new ProducerRecord<String, String>(MQDict.PRODUCER_TOPIC,0, "key"+i,"value"+i);

//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("tep",i);
//            jsonObject.put("hunidity",i + 3);

            record = new ProducerRecord<String, String>("Topic-Type-Test",0, "key","value"+1);

            //发送消息
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (null != e){
                        log.info("send error" + e.getMessage());
                    }else {
                        System.out.println(String.format("offset:%s,partition:%s",recordMetadata.offset(),recordMetadata.partition()));
                    }
                }
            });
        }
        producer.close();
    }

}
