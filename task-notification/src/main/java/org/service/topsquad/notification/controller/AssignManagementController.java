package org.service.topsquad.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/task")
public class AssignManagementController {

    // TEST
    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/sendEmail")
    public String sendEmail() {
        sendmail();
        return "Done";
    }

    private void sendmail() {
        String from = "thong0605@yahoo.com";
        String to = "minhthong0605@gmail.com";

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject("This is a plain text email");
        message.setText("Hello guys! This is a plain text email.");

        mailSender.send(message);
    }
}
