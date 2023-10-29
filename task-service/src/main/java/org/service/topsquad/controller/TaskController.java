package org.service.topsquad.controller;

import java.util.List;

import org.service.topsquad.exceptionhandler.ResourceNotFoundException;
import org.service.topsquad.exceptionhandler.ResourceValidateException;
import org.service.topsquad.model.Account;
import org.service.topsquad.model.Response;
import org.service.topsquad.model.TaskAssignModel;
import org.service.topsquad.model.TaskEntity;
import org.service.topsquad.model.TaskInfosEmail;
import org.service.topsquad.model.TaskModel;
import org.service.topsquad.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1")
public class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService service;

    @Autowired
    private KafkaTemplate<String, TaskInfosEmail> kafkaTemplate;

    @KafkaListener(topics = "test_request_topic")
    @SendTo
    public Object listen(Account request) {
        request.setNewPassword(request.getUserName() + request.getPassword());
        return request;
    }

    @KafkaListener(topics = "task_create_topic",
            groupId = "${spring.kafka.consumer.group-id}")
    public void consumeCreate(TaskModel model){
        LOGGER.info(String.format("Received msg from Task Creation"));
        try {
            service.create(model);
        } catch (ResourceValidateException e) {
            LOGGER.info(String.format("Error from Task Creation"));
        }
    }
    @KafkaListener(topics = "task_update_topic",
            groupId = "${spring.kafka.consumer.group-id}")
    public void consumeUpdate(TaskModel model){
        LOGGER.info(String.format("Received msg from Task Update"));
        LOGGER.info(String.format("Update Task: " + model.getTicketNumber()));
        try {
            service.update(model);
        } catch (ResourceNotFoundException e) {
            LOGGER.info(String.format("Error from Task Update"));
        }
    }

    // OK
    @GetMapping("/tasks")
    public List<TaskEntity> findAll() {
        return service.findAll();
    }

    @GetMapping("/tasks/{ticketNumber}")
    public ResponseEntity<TaskEntity> getByTicketNumber(@PathVariable(value = "ticketNumber") final String ticketNumber) {
        try {
            TaskEntity task = service.findByTicketNumber(ticketNumber);
            return ResponseEntity.status(HttpStatus.OK).body(task);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // OKAY
    @PostMapping("/tasks")
    public ResponseEntity<TaskEntity> create(@RequestBody final TaskModel model) {
        try {
            TaskEntity entity = service.create(model);
            return ResponseEntity.status(HttpStatus.OK).body(entity);
        } catch (ResourceValidateException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // OKAY
    @PatchMapping("/tasks")
    public ResponseEntity<TaskEntity> update(@RequestBody final TaskModel model)
            throws ResourceNotFoundException {
        TaskEntity newTask = service.update(model);
        return ResponseEntity.status(HttpStatus.OK).body(newTask);
    }
    @PatchMapping("/tasks/assign")
    public ResponseEntity<Response> assign(@RequestBody final TaskAssignModel assignModel) {
        try {
            service.assignTask(assignModel.getTicketNumber(), assignModel.getUserName());

            TaskInfosEmail mail = new TaskInfosEmail();
            mail.setTaskName(assignModel.getTicketNumber());
            mail.setAssigneeEmailAddress(assignModel.getUserName());
            mail.setTaskUrl(assignModel.getTaskUrl());
            kafkaTemplate.send("task_notification_topic", mail);

            return ResponseEntity.status(HttpStatus.OK).body(new Response("Task assigned"));
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/admin/tasks/{ticketNumber}")
    public ResponseEntity<Response> delete(@PathVariable(value = "ticketNumber") final String ticketNumber)
            throws ResourceNotFoundException {
        String msg = service.deleteByTicketNumber(ticketNumber) ? "Deleted" : "Not Deleted";
        return ResponseEntity.ok().body(new Response(msg));
    }

    @DeleteMapping("/admin/tasks")
    public ResponseEntity<Response> deleteAll() {
    return service.deleteAll()
        ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Response("Tasks deleted."))
        : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
