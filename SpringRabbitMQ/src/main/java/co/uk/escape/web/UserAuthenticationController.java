package co.uk.escape.web;


import java.util.Date;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.amqp.core.Queue;

import co.uk.escape.RMQTemplate;
import co.uk.escape.domain.LoginRequest;
import co.uk.escape.domain.LoginRequestMessageBundle;
import co.uk.escape.domain.LoginResponse;
import co.uk.escape.domain.LoginResponseMessageBundle;

import org.springframework.http.MediaType;

import com.rabbitmq.client.Channel;

@RestController
@RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserAuthenticationController {

	@Autowired @RMQTemplate(RMQTemplate.Type.LOGIN_USER)
	RabbitTemplate rabbitTemplateInfo;
	
	
	// TODO: Security intercepter that extracts and prepares security data for authentication
	@RequestMapping(method = RequestMethod.POST)
	public LoginResponse loginUser(@RequestBody LoginRequest loginRequest) {
	
		System.out.println("loginUser: enter REST: " + loginRequest);
		
		// TODO: Message is bundled with security data and sent of to the 'authorization' queue.
		final String appId 	= "CIL12345"; // This could be the appliction's API key
		final String userId = ""; 
		
		MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
	   		public Message postProcessMessage(Message message) throws AmqpException {
	   			Date timestamp = new Date(); // TODO: Change to UTC date/time and use Joda-time (generally considered better than Java date)
//	   			message.getMessageProperties().setAppId(appId); // TODO: Investigate NOTE: If this is set the message is not sent and no error is thrown.
//	   			message.getMessageProperties().setUserId(userId); // TODO: Investigate NOTE: If this is set the message is not sent and no error is thrown.
//	   			message.getMessageProperties().setTimestamp(timestamp);  // TODO: Investigate NOTE: If this is set the message is not sent and no error is thrown.
	   			message.getMessageProperties().setHeader("message-type", "login-request");
	   			return message;  
	   		} 
		};
		
		
		// Transform message payload into message payload
		LoginRequestMessageBundle loginRequestMessageBundle = bundleMessage(loginRequest);	
		
		
		Object obj = rabbitTemplateInfo.convertSendAndReceive(loginRequestMessageBundle, messagePostProcessor);
	
		// TODO: Returns an object that contains the authorization status of the request
		// TODO: If authorization fails, return JSON giving details of error 401
		// TODO: If access denied, return JSON giving details of error 403
		// TODO: If authorization successful return data to client
				
		LoginResponseMessageBundle loginResponseMessageBundle = (LoginResponseMessageBundle)obj;		
		LoginResponse loginResponse = loginResponseMessageBundle.getPayload();
		
		System.out.println("loginUser: exit REST "+ loginResponse);
		return loginResponse;
	}

	// Bundle message
	private LoginRequestMessageBundle bundleMessage(LoginRequest object) {
		return new LoginRequestMessageBundle(object);
	}
	
	
	
	
	
//	@ExceptionHandler(DuplicateKeyException.class)
//	ResponseEntity<String> duplicateKey(Exception e) {
//		return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
//	}
//	
//	@ExceptionHandler(EmptyResultDataAccessException.class)
//	ResponseEntity<String> handleNotFounds(Exception e) {
//		return new ResponseEntity<>(e.getMessage(), HttpStatus.GONE);
//	}
}
