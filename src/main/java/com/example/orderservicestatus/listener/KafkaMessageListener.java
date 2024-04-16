package com.example.orderservicestatus.listener;



import com.example.orderservicestatus.model.OrderEvent;
import com.example.orderservicestatus.model.StatusEvent;
import com.example.orderservicestatus.service.KafkaMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaMessageListener {
    private final KafkaMessageService kafkaMessageService;


    @KafkaListener(topics = "${app.kafka.kafkaMessageTopic}",
            groupId = "${app.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaMessageConcurrentKafkaListenerContainerFactory")
    public void listen(@Payload OrderEvent kafkaMessage, @Header(value= KafkaHeaders.RECEIVED_KEY, required = false)UUID key,
//                       @Header(KafkaHeaders.TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partitions,
                       @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timeStamp) {
        log.info("Received message: {}",kafkaMessage);
        log.info("Key: {}; Partition: {}; Topic: {}; TimeStamp: {}", key, partitions, timeStamp);
        log.info("kafkaMessage {} : product Name; {} : quantity", kafkaMessage.getProduct(), kafkaMessage.getQuantity());

        kafkaMessageService.add(new StatusEvent("Created", Instant.now()));
    }
}
