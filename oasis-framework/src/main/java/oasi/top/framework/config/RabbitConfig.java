package oasi.top.framework.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String QueueName = "oasis-queue";

    @Bean
    public Queue queue() {
        return new Queue(QueueName);
    }
}
