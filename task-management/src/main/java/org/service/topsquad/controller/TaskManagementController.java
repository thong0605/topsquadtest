package org.service.topsquad.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.service.topsquad.constants.Constants;
import org.service.topsquad.model.Person;
import org.service.topsquad.model.Response;
import org.service.topsquad.model.TaskAssignModel;
import org.service.topsquad.model.TaskModel;
import org.service.topsquad.model.TaskStatus;
import org.service.topsquad.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/task-manage")
public class TaskManagementController {
    @Autowired
    private KafkaTemplate<String, TaskModel> kafkaTemplate;
    private static final String TASK_SERVICE_URL = "http://localhost:8081/api/v1";

    // Display all tasks
    @GetMapping()
    public String homePage(Model model) {
        StringBuilder httpUrl = new StringBuilder(TASK_SERVICE_URL);
        RestTemplate restTemplate = new RestTemplate();
        Object returnOb = restTemplate.getForObject(httpUrl.append("/tasks").toString(), Object.class);

        model.addAttribute("tasks", returnOb);
        return "index";
    }

    // Create Task View
    @GetMapping("createForm")
    public String createForm(Model model) {
        model.addAttribute("task", new TaskModel());
        return "task/add_task";
    }
    // Task Create Form
    @PostMapping("create")
    public String createSubmit(@ModelAttribute TaskModel taskModel) {
        kafkaTemplate.send(Constants.TOPIC_CREATE, taskModel);
        return "redirect:/task-manage";
    }

    // Create Update View
    @GetMapping("/{ticketNumber}")
    public String updateForm(@PathVariable String ticketNumber, Model model) {
        StringBuilder httpUrl = new StringBuilder(TASK_SERVICE_URL);
        RestTemplate restTemplate = new RestTemplate();
        String returnOb = restTemplate.getForObject(httpUrl.append("/tasks").append("/")
                .append(ticketNumber).toString(), String.class);
        TaskModel task = null;
        try {
            task = new ObjectMapper().readValue(returnOb, TaskModel.class);
        } catch (JsonProcessingException e) {
            return "Cannot parse the json body ";
        }

        List<String> status = new ArrayList<>();
        status.add(TaskStatus.TO_DO.name());
        status.add(TaskStatus.IN_PROGRESS.name());
        status.add(TaskStatus.IN_TESTING.name());
        status.add(TaskStatus.IN_REVIEW.name());
        status.add(TaskStatus.DONE.name());
        status.add(TaskStatus.RE_OPEN.name());

        TaskAssignModel taskAssign = new TaskAssignModel(ticketNumber);
        model.addAttribute("task", task);
        model.addAttribute("taskAssign", taskAssign);
        model.addAttribute("statusList", status);
        return "task/edit_task";
    }

    // Task Update Form
    @PostMapping("update")
    public String updateSubmit(@ModelAttribute TaskModel taskModel) {
//        Message<TaskModel> message = MessageBuilder
//                .withPayload(taskModel)
//                .setHeader(KafkaHeaders.TOPIC, Constants.TOPIC_CREATE)
//                .build();
        kafkaTemplate.send(Constants.TOPIC_UPDATE, taskModel);
        return "redirect:/task-manage";
    }

    // Delete Task Execution
    @GetMapping("delete/{ticketNumber}")
    public String delete(@PathVariable String ticketNumber) {
        StringBuilder httpUrl = new StringBuilder(TASK_SERVICE_URL);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> responseEntity = restTemplate.exchange(httpUrl.append("/tasks").append("/")
                .append(ticketNumber).toString(), HttpMethod.DELETE, null, Object.class);

        return responseEntity.getStatusCode() == HttpStatus.OK ?
                "redirect:/task-manage" : "error/500";
    }

    @GetMapping("deleteAll")
    public String deleteAll() {
        StringBuilder httpUrl = new StringBuilder(TASK_SERVICE_URL);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> responseEntity = restTemplate.exchange(httpUrl.append("/tasks").toString(),
                HttpMethod.DELETE, null, Object.class);

        return responseEntity.getStatusCode() == HttpStatus.OK ?
                "redirect:/task-manage" : "error/500";
    }
    // Delete Task Execution

    // Display tasks assigned to the user
    @GetMapping("userTest")
    public String userPage(Model model) {
        StringBuilder httpUrl = new StringBuilder(TASK_SERVICE_URL);
        RestTemplate restTemplate = new RestTemplate();
//        Object returnOb = restTemplate.getForObject(httpUrl.append("/tasks").toString(), Object.class);

        String userName = "";
        String returnUser = restTemplate.getForObject(httpUrl.append("/users").append("/")
        .append(userName).toString(), String.class);

        UserModel userModel;

        try {
            userModel = new ObjectMapper().readValue(returnUser, UserModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        model.addAttribute("tasks", userModel.getTasks());
        model.addAttribute("user", userModel);
        return "user/task";
    }
    @PostMapping("assign")
    public String assignTask(@ModelAttribute TaskAssignModel taskAssignModel) {

        StringBuilder httpUrl = new StringBuilder(TASK_SERVICE_URL);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<TaskAssignModel> httpEntity = new HttpEntity<>(taskAssignModel);

        ResponseEntity<String> responseEntity = restTemplate.exchange(httpUrl.append("/tasks")
                .append("/assign").toString(), HttpMethod.PATCH, httpEntity, String.class);

        StringBuilder redirect = new StringBuilder("redirect:/task-manage/").append(taskAssignModel.getTicketNumber());
        return responseEntity.getStatusCode() == HttpStatus.OK
                ? redirect.toString() : "error/500";
    }
}