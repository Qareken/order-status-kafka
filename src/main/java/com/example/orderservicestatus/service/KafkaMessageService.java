package com.example.orderservicestatus.service;



import com.example.orderservicestatus.model.StatusEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KafkaMessageService {

    private final KafkaTemplate<String, StatusEvent> kafkaTemplate;
    @Value("${app.kafka.kafkaMessageTopicStatus}")
    private String topicName;
    private final List<StatusEvent> messages = new ArrayList<>();
    public void  add(StatusEvent message){
        kafkaTemplate.send(topicName, message);
        messages.add(message);

    }
    public Optional<StatusEvent> getById(String status){
        return messages.stream().filter(it->it.getStatus().equals(status)).findFirst();
    }
}
