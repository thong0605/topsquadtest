package org.service.topsquad.controller;

import org.service.topsquad.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/admin")
public class AdminUserController {
    @Autowired
    public KafkaTemplate<String, Object> kafkaTemplateObject;
    private static final String TASK_SERVICE_URL = "http://localhost:8081/api/v1";

    // Display all tasks
    @GetMapping("users")
    public String userPage(Model model) {
        StringBuilder httpUrl = new StringBuilder(TASK_SERVICE_URL);
        RestTemplate restTemplate = new RestTemplate();
        Object returnOb = restTemplate.getForObject(httpUrl.append("/admin")
        .append("/users").toString(), Object.class);

        model.addAttribute("usesr", returnOb);
        return "admin/users";
    }

    // Create User View
    @PostMapping("deactivate")
    public String deactivate(Model model) {
        model.addAttribute("user", new UserModel());
        return "user/register";
    }
}