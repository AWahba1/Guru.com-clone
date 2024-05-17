package com.gurutest.amqb;

import com.gurutest.commands.Command;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Headers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class RabbitMQConfig {

    //  map between command name and object command
    private static final String request_queue = "user_req";
    private final Map<String, Command> commands;
   // private final AmqpTemplate amqpTemplate;
//    //private final ExecutorService threadPool;
//
    public RabbitMQConfig( Map<String, Command> commands) {
        this.commands = commands;
        //this.amqpTemplate = amqpTemplate;
       // this.threadPool = threadPool;
    }



    @Bean(name = {request_queue})
    public Queue request_queue() {
        return new Queue(request_queue);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public ExecutorService executor() {

        return new ThreadPoolExecutor(
                10,
                20,
                60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(1000));
    }

//    @RabbitListener(queues = request_queue)
//    public void listen_2(HashMap<String, Object> payload, @Headers Map<String, Object> headers) {
//        HashMap<String, Object> map = new HashMap<>();
//        try {
//            payload.put("user_id", headers.get("user_id"));
//            payload.put("timestamp", headers.get("timestamp"));
//            Object res = commands.get((String)headers.get("command")).execute(payload);
//            map.put("data", res);
//        } catch (Exception e) {
//            map.put("error", e.getMessage());
//        } finally {
//            amqpTemplate.convertAndSend((String) headers.get("amqp_replyTo"), map, m -> {
//                m.getMessageProperties().setCorrelationId((String) headers.get("amqp_correlationId"));
//                m.getMessageProperties().setReplyTo((String) headers.get("amqp_replyTo"));
//                return m;
//            });
//        }
//    }
}
