package co.uk.escape;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class UserRegistrationConfiguration {
	
	final static String queueName = "user-registration";
	
	@Bean
	Queue replyQueue() {
		return new Queue(queueName+"-reply", false);
	}
	
	
	@Bean
	DirectExchange exchange() {
		return new DirectExchange("user-registrations-exchange");
	}
		
	@Bean
	RabbitTemplate template(DirectExchange exchange, Queue replyQueue, ConnectionFactory connectionFactory){
		Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonConverter);
		rabbitTemplate.setExchange(exchange.getName());
		rabbitTemplate.setRoutingKey("user");
		rabbitTemplate.setReplyQueue(replyQueue);
		return rabbitTemplate;
	}
	
    @Bean
    public SimpleMessageListenerContainer replyListenerContainer(RabbitTemplate template, Queue replyQueue, ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(replyQueue);
        container.setMessageListener(template);
        return container;
    }

}
