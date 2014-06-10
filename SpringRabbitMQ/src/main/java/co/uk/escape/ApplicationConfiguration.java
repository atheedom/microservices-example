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
//	@Bean @RMQQueue(LOGIN_REQUEST)
//	Queue loginRequestQueue() {
//		return new Queue("LoginRequestQueue", true);
//	}
	
	@Bean @RMQQueue(USER_INFO_RESPONSE)
	Queue userInfoResponseQueue() {
		return new Queue("UserInfoResponseQueue", true);
	}
	
	@Bean @RMQQueue(LOGIN_RESPONSE)
	Queue loginResponseQueue() {
		return new Queue("LoginResponseQueue", true);
	}
	
	@Bean @RMQQueue(USER_AUTHORISATION)
	Queue authorisationQueue() {
		return new Queue("AuthorisationQueue", true);
	}
	
//	@Bean @RMQQueue(REGISTRATION_REQUEST)
//	Queue registrationRequestQueue() {
//		return new Queue("RegistrationRequestQueue", true);
//	}
	
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
//    @Bean
//    public Binding binding() {
//        return BindingBuilder.bind(loginRequestQueue()).to(authorisationExchange()).with("AuthorisationRoutingKey");
//    }
    
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(authorisationQueue()).to(authorisationExchange()).with("AuthorisationRoutingKey");
    }
    
	@Bean
	Binding replyBind(){
		return BindingBuilder.bind(loginResponseQueue()).to(responseExchange()).with("ResponseRoutingKey");
	}

	@Bean
	Binding registrationBind(){
		return BindingBuilder.bind(registrationResponseQueue()).to(responseExchange()).with("ResponseRoutingKey");
	}
	
	

//	@Bean
//	public ConnectionFactory connectionFactory() {
//		CachingConnectionFactory factory = new CachingConnectionFactory();
//		//String addresses = config.getAddresses();
//		//factory.setAddresses(addresses);
//		factory.setHost("127.0.0.1");
//		factory.setPort(5673);
//		factory.setUsername("guest");		
//		factory.setPassword("guest");		
//		factory.setVirtualHost("/");	
//		return factory;
//		};
	
	
    ////////////////
	// User Info  //
    ////////////////
	@Bean @RMQTemplate(USER_INFO)
	RabbitTemplate userInfoTemplate(ConnectionFactory connectionFactory,
			@RMQQueue(USER_INFO_RESPONSE) Queue userInfoResponseQueue,
			@RMQQueue(USER_AUTHORISATION) Queue authorisationQueue,
			@RMQExchange(AUTHORISATION) TopicExchange authorisationExchange){
		Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();	
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonConverter);
		rabbitTemplate.setExchange(authorisationExchange.getName());
		rabbitTemplate.setQueue(authorisationQueue.getName());
		rabbitTemplate.setRoutingKey("AuthorisationRoutingKey");
		rabbitTemplate.setReplyQueue(userInfoResponseQueue);
		return rabbitTemplate;
	}
	
    @Bean
    public SimpleMessageListenerContainer userInfoListenerContainer(ConnectionFactory connectionFactory,
    		@RMQTemplate(USER_INFO) RabbitTemplate template, 
    		@RMQQueue(USER_INFO_RESPONSE) Queue userInfoResponseQueue) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(userInfoResponseQueue);
        container.setMessageListener(template);
        container.setReceiveTimeout(200000);
        return container;
    }	
	
    
    
    ///////////////////////
	// Register New User //
    ///////////////////////
	@Bean @RMQTemplate(REGISTER_USER)
	RabbitTemplate template(ConnectionFactory connectionFactory,
			@RMQQueue(REGISTRATION_RESPONSE) Queue registrationResponseQueue,
			//@RMQQueue(REGISTRATION_REQUEST) Queue registrationRequestQueue,
			@RMQQueue(USER_AUTHORISATION) Queue authorisationQueue,
			@RMQExchange(AUTHORISATION) TopicExchange authorisationExchange){
		Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
		
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonConverter);
		rabbitTemplate.setExchange(authorisationExchange.getName());
		//rabbitTemplate.setQueue(registrationRequestQueue.getName()); // TODO: determine if it matters if we have just one authorisation IN queue
		rabbitTemplate.setQueue(authorisationQueue.getName());
//		rabbitTemplate.setRoutingKey("RegistrationRoutingKey"); // TODO: determine if it matters if the routing key is AuthorisationRoutingKey
		rabbitTemplate.setRoutingKey("AuthorisationRoutingKey");
		rabbitTemplate.setReplyQueue(registrationResponseQueue);
		return rabbitTemplate;
	}
	
    @Bean
    public SimpleMessageListenerContainer userRegistrationListenerContainer(ConnectionFactory connectionFactory,
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
			//@RMQQueue(LOGIN_REQUEST) Queue loginRequestQueue, 
			@RMQQueue(LOGIN_RESPONSE) Queue loginResponseQueue,
			@RMQQueue(USER_AUTHORISATION) Queue authorisationQueue,
			@RMQExchange(AUTHORISATION) TopicExchange authorisationExchange){
		Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonConverter);
		rabbitTemplate.setExchange(authorisationExchange.getName());
		//rabbitTemplate.setQueue(loginRequestQueue.getName());
		rabbitTemplate.setQueue(authorisationQueue.getName());
		rabbitTemplate.setRoutingKey("AuthorisationRoutingKey");
		rabbitTemplate.setReplyQueue(loginResponseQueue);
		return rabbitTemplate;
	}
	
    @Bean
    public SimpleMessageListenerContainer replyUserAuthenticationListenerContainer(ConnectionFactory connectionFactory, 
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
