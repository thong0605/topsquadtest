package org.service.topsquad.controller;

import org.apache.commons.lang3.StringUtils;
import org.service.topsquad.constants.Constants;
import org.service.topsquad.model.Account;
import org.service.topsquad.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/user-manage")
public class UserManagementController {
    @Autowired
    public KafkaTemplate<String, Object> kafkaTemplateObject;
    private static final String TASK_SERVICE_URL = "http://localhost:8081/api/v1";

    // Display all tasks
    @GetMapping("{userName}")
    public String userPage(@PathVariable String userName, Model model) {
        StringBuilder httpUrl = new StringBuilder(TASK_SERVICE_URL);
        RestTemplate restTemplate = new RestTemplate();
        Object returnOb = restTemplate.getForObject(httpUrl.append("/users")
        .append("/").append(userName).toString(), Object.class);

        model.addAttribute("user", returnOb);
        return "user/index";
    }

    // Create User View
    @GetMapping("registerForm")
    public String registerForm(Model model) {
        model.addAttribute("user", new UserModel());
        return "user/register";
    }
    // User Create Form
    @PostMapping("register")
    public String registerSubmit(@ModelAttribute UserModel userModel) {
        kafkaTemplateObject.send(Constants.TOPIC_USER_REGISTER, userModel);
        return "user/login";
    }

    // Change password view
    @GetMapping("changePassword")
    public String changePasswordForm(Model model) {
        model.addAttribute("user", new UserModel());
        return "user/changePassword";
    }
    // Change password view
    @PostMapping("changePassword")
    public String changePasswordSubmit(@ModelAttribute Account model) {
        // Request validations
        if (StringUtils.isAllBlank(model.getUserName(), model.getPassword(), model.getNewPassword()))
            return "error/400";

        // TODO use request-reply kafka produce message
        kafkaTemplateObject.send(Constants.TOPIC_USER_UPDATE, model);
        return "redirect:/user-manage/logout";
    }
    // TODO login, logout
}