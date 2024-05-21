package com.guru.notificationservice.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfiguration {

    @Bean
    public Queue viewProfile() {
        return new Queue("view_profile_queue");
    }

    @Bean
    public Queue jobInvitation() {
        return new Queue("job_invitations_queue");
    }

    @Bean
    public Queue bidPlacement() {
        return new Queue("bid_placement_queue");
    }
    public Queue messageSent() {return new Queue("new_message_sent");}

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

}
