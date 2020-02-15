package oasi.top.framework.service;

import oasi.top.framework.config.RabbitConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MsgService {
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send(Object msg) {
        amqpTemplate.convertAndSend(RabbitConfig.QueueName, msg);
    }

    @RabbitListener(queues = RabbitConfig.QueueName)
    public void receiver(Object msg) {
        System.out.println("receive the msg " + msg);
    }
}
