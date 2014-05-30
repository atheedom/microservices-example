package co.uk.escape;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
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

import com.rabbitmq.client.Channel;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class UserAuthenticationConfiguration {

	@Bean
	Queue responseQueue() {
		return new Queue("response-queue", true);
	}
		
	@Bean
	Queue authorisationRequestQueue() {
		return new Queue("authorisation-request-queue", true);
	}
	
	@Bean
	TopicExchange authorisationExchange() {
		return new TopicExchange("authorisation-exchange", true, false);
	}
	
	@Bean
	TopicExchange responseExchange() {
		return new TopicExchange("response-exchange", true, false);
	}
	
//	@Bean
//	TopicExchange messageExchange() {
//		return new TopicExchange("message-exchange", true, false);
//	}
	
		
	@Bean
	Binding bindAuthenticateRequest(){
		return BindingBuilder.bind(authorisationRequestQueue()).to(authorisationExchange()).with("authenticate");
	}
	
	@Bean
	Binding bindAuthenticateResponse(){
		return BindingBuilder.bind(responseQueue()).to(responseExchange()).with("authenticate");
	}

	@Bean
	RabbitTemplate template(TopicExchange authorisationExchange, Queue responseQueue, Queue authorisationRequestQueue, ConnectionFactory connectionFactory) {
		Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonConverter);
		rabbitTemplate.setExchange(authorisationExchange.getName());
		rabbitTemplate.setQueue(authorisationRequestQueue.getName());
		rabbitTemplate.setRoutingKey("authenticate");	
		rabbitTemplate.setReplyQueue(responseQueue);
		return rabbitTemplate;
	}
	
	

	@Bean
	public SimpleMessageListenerContainer replyListenerContainer(RabbitTemplate template, Queue responseQueue, ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueues(responseQueue);
		container.setMessageListener(template);
		return container;
	}


}