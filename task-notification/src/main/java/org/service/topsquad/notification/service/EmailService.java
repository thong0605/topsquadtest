package org.service.topsquad.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.service.topsquad.notification.model.TaskInfosEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @KafkaListener(topics = "task_notification_topic", groupId = "email")
    public void consume(String event){
        LOGGER.info(String.format("Task notification event in email service => %s", event));
        try {
            TaskInfosEmail emailBody = new ObjectMapper().readValue(event, TaskInfosEmail.class);

            // send an email to the customer
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom("topsquad_admin@gmail.com");
            mimeMessageHelper.setTo(emailBody.getAssigneeEmailAddress());
            mimeMessageHelper.setSubject("Task Assign Test");

            String content = "<h1>Task Assignment</h1>\n" +
                    "<p>Hi " + emailBody.getAssigneeEmailAddress() + "</p>\n" +
                    "<a href=\"" + emailBody.getTaskUrl() + "\">"+ emailBody.getTaskName() + "</a>";
            mimeMessageHelper.setText(content, true);
            mimeMessageHelper.setSentDate(new Date());
            mailSender.send(mimeMessage);

            LOGGER.info(String.format("Email sent to user : %s", emailBody.getAssigneeEmailAddress()));
        } catch (JsonProcessingException e) {
            LOGGER.error(String.format("Email sent failed : %s", e));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
