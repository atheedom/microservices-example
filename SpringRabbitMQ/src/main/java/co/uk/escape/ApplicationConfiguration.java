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

import static co.uk.escape.RMQExchange.Type.*;
import static co.uk.escape.RMQQueue.Type.*;
import static co.uk.escape.RMQTemplate.Type.*;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class ApplicationConfiguration {
	
	// QUEUES //	
	@Bean @RMQQueue(LOGIN_REQUEST)
	Queue loginRequestQueue() {
		return new Queue("LoginRequestQueue", true);
	}
	
	@Bean @RMQQueue(LOGIN_RESPONSE)
	Queue loginResponseQueue() {
		return new Queue("LoginResponseQueue", true);
	}
	
	@Bean @RMQQueue(REGISTRATION_REQUEST)
	Queue registrationRequestQueue() {
		return new Queue("RegistrationRequestQueue", true);
	}
	
	@Bean @RMQQueue(REGISTRATION_RESPONSE)
	Queue registrationResponseQueue() {
		return new Queue("RegistrationResponseQueue", true);
	}	
	
	
	// EXCHANGE //
	@Bean @RMQExchange(AUTHORISATION)
	TopicExchange authorisationExchange() {
		return new TopicExchange("AuthorisationExchange");
	}
	
	@Bean @RMQExchange(RESPONSE)
	TopicExchange responseExchange() {
		return new TopicExchange("ResponseExchange");
	}
	
	@Bean @RMQExchange(MESSAGE)
	TopicExchange messageExchange() {
		return new TopicExchange("MessageExchange");
	}

	
	// BINDINGS //		
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(loginRequestQueue()).to(authorisationExchange()).with("AuthorisationRoutingKey");
    }
    
	@Bean
	Binding replyBind(){
		return BindingBuilder.bind(loginResponseQueue()).to(responseExchange()).with("ResponseRoutingKey");
	}

	@Bean
	Binding registrationBind(){
		return BindingBuilder.bind(registrationResponseQueue()).to(responseExchange()).with("RegistrationRoutingKey");
	}
	
	
    ///////////////////////
	// Register New User //
    ///////////////////////
	@Bean @RMQTemplate(REGISTER_USER)
	RabbitTemplate template(ConnectionFactory connectionFactory,
			@RMQQueue(REGISTRATION_RESPONSE) Queue registrationResponseQueue,
			@RMQQueue(REGISTRATION_REQUEST) Queue registrationRequestQueue,
			@RMQExchange(AUTHORISATION) TopicExchange authorisationExchange){
		Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonConverter);
		rabbitTemplate.setExchange(authorisationExchange.getName());
		rabbitTemplate.setQueue(registrationRequestQueue.getName());
		rabbitTemplate.setRoutingKey("RegistrationRoutingKey");
		rabbitTemplate.setReplyQueue(registrationResponseQueue);
		return rabbitTemplate;
	}
	
    @Bean
    public SimpleMessageListenerContainer replyListenerContainer(ConnectionFactory connectionFactory,
    		@RMQTemplate(REGISTER_USER) RabbitTemplate template, 
    		@RMQQueue(REGISTRATION_RESPONSE) Queue registrationResponseQueue) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(registrationResponseQueue);
        container.setMessageListener(template);
        container.setReceiveTimeout(200000);
        return container;
    }
    
    
    ///////////////////////
	// Authenticate User //
    ///////////////////////
	@Bean @RMQTemplate(LOGIN_USER)
	RabbitTemplate loginTemplate(ConnectionFactory connectionFactory,
			@RMQQueue(LOGIN_REQUEST) Queue loginRequestQueue, 
			@RMQQueue(LOGIN_RESPONSE) Queue loginResponseQueue,
			@RMQExchange(AUTHORISATION) TopicExchange authorisationExchange){
		Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonConverter);
		rabbitTemplate.setExchange(authorisationExchange.getName());
		rabbitTemplate.setQueue(loginRequestQueue.getName());
		rabbitTemplate.setRoutingKey("AuthorisationRoutingKey");
		rabbitTemplate.setReplyQueue(loginResponseQueue);
		return rabbitTemplate;
	}
	
    @Bean
    public SimpleMessageListenerContainer replyUserInfoListenerContainer(ConnectionFactory connectionFactory, 
    			@RMQQueue(LOGIN_RESPONSE) Queue loginResponseQueue,
    			@RMQTemplate(LOGIN_USER) RabbitTemplate loginTemplate) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(loginResponseQueue);
        container.setMessageListener(loginTemplate);
        container.setReceiveTimeout(200000);
        return container;
    }
}
