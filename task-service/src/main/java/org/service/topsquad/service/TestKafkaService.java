package org.service.topsquad.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.service.topsquad.model.TaskEntity;
import org.service.topsquad.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TestKafkaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestKafkaService.class);
    @Autowired
    TaskRepository repository;

    @KafkaListener(topics = "thongtestproducer", groupId = "group_id")
    public void addTask(String body) {
        try {
            TaskEntity task = new ObjectMapper().readValue(body, TaskEntity.class);
            repository.save(task);
            LOGGER.info(String.format("Task created." + new Date()));
        } catch (JsonProcessingException e) {
            LOGGER.error(String.format("Error task created : %s", e));
        }
    }
}
