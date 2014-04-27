package co.uk.escape;

import java.util.UUID;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
	
//	@Bean
//	public Queue replyQueue() {
//		return new AnonymousQueue();
//	}
	
	
	@Bean
	DirectExchange exchange() {
		return new DirectExchange("user-registrations-exchange");
	}
//	
//	@Bean
//	Binding binding(Queue queue, DirectExchange exchange) {
//		return BindingBuilder.bind(queue).to(exchange).with(queueName);
//	}
	
	
	@Bean
	RabbitTemplate template(DirectExchange exchange, Queue replyQueue, ConnectionFactory connectionFactory){
		Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonConverter);
		rabbitTemplate.setExchange(exchange.getName());
		rabbitTemplate.setRoutingKey("test");
		//rabbitTemplate.setCorrelationKey(UUID.randomUUID().toString());
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
