package org.service.topsquad.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.service.topsquad.constants.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

//    @Value(value = "${spring.kafka.bootstrap-servers}")
//    private String bootstrapAddress;
//
//    @Bean
//    public KafkaAdmin kafkaAdmin() {
//        Map<String, Object> configs = new HashMap<>();
//        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
//        return new KafkaAdmin(configs);
//    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(Constants.TOPIC_CREATE)
                .replicas(1).partitions(1)
                .build();
    }
    @Bean
    public NewTopic topicUpdate() {
        return TopicBuilder.name(Constants.TOPIC_UPDATE)
                .replicas(1).partitions(1)
                .build();
    }

    @Bean
    public NewTopic topicSendEmail() {
        return TopicBuilder.name("task_notification_topic")
                .replicas(1).partitions(1)
                .build();
    }
}
