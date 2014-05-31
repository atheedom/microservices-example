package co.uk.escape;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
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

import co.uk.escape.web.RMQTemplate;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class UserRegistrationConfiguration {
	
	//final static String queueName = "user-registration";
	
	@Bean
	Queue replyQueue() {
		return new Queue("replyqueue", false);
	}
	

	@Bean
	DirectExchange replyexchange() {
		return new DirectExchange("replyexchange");
	}
	
	
	@Bean
	Binding bind(){
		return BindingBuilder.bind(replyQueue()).to(replyexchange()).with("replyroutingkey");
	}
	
	@Bean
	Queue requestQueue() {
		return new Queue("requestqueue", false);
	}
	
//	@Bean
//	DirectExchange exchange() {
//		return new DirectExchange("user-registrations-exchange");
//	}
	
	@Bean
	DirectExchange exchangeUserInfo() {
		return new DirectExchange("requestexchange");
	}

	// Create new user
//	@Bean @RMQTemplate(RMQTemplate.Type.CREATE_USER)
//	RabbitTemplate template(DirectExchange exchange, Queue replyQueue, ConnectionFactory connectionFactory){
//		Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
//		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//		rabbitTemplate.setMessageConverter(jsonConverter);
//		rabbitTemplate.setExchange(exchange.getName());
//		rabbitTemplate.setRoutingKey("user");
//		rabbitTemplate.setReplyQueue(replyQueue);
//		return rabbitTemplate;
//	}
	
//    @Bean
//    public SimpleMessageListenerContainer replyListenerContainer(RabbitTemplate template, Queue replyQueue, ConnectionFactory connectionFactory) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueues(replyQueue);
//        container.setMessageListener(template);
//        container.setReceiveTimeout(200000);
//        return container;
//    }

	// Get user info
	@Bean @RMQTemplate(RMQTemplate.Type.GET_USER_INFO)
	RabbitTemplate templateUserInfo(DirectExchange exchangeUserInfo, Queue replyQueue, ConnectionFactory connectionFactory){
		Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonConverter);
		rabbitTemplate.setExchange(exchangeUserInfo.getName());
		rabbitTemplate.setQueue("requestqueue");
		rabbitTemplate.setRoutingKey("requestroutingkey");
		rabbitTemplate.setReplyQueue(replyQueue);
		return rabbitTemplate;
	}
	
    @Bean
    public SimpleMessageListenerContainer replyUserInfoListenerContainer(RabbitTemplate templateUserInfo, Queue replyQueue, ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(replyQueue);
        container.setMessageListener(templateUserInfo);
        container.setReceiveTimeout(200000);
        return container;
    }
}
