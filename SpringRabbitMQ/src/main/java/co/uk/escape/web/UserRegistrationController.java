package co.uk.escape.web;

import java.util.Date;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.uk.escape.RMQTemplate;
import co.uk.escape.domain.RegisteredUser;
import co.uk.escape.domain.RegistrationRequest;
import co.uk.escape.domain.RegistrationRequestMessageBundle;


import co.uk.escape.domain.RegistrationResponse;
import co.uk.escape.domain.RegistrationResponseMessageBundle;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRegistrationController {

	@Autowired @RMQTemplate(RMQTemplate.Type.REGISTER_USER)
	RabbitTemplate rabbitTemplateUser;
	
	// TODO: Security intercepter that extracts and prepares security data for authentication
	@RequestMapping(method = RequestMethod.POST)
	public RegistrationResponse registerUser(@RequestBody RegistrationRequest registrationRequest, @RequestHeader HttpHeaders headers){
		System.out.println("in the controller: registerUser()");
			
		MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
	   		public Message postProcessMessage(Message message) throws AmqpException {
	   			message.getMessageProperties().setHeader("message-type", "registration-request");
	   			return message;  
	   		} 
		};
		
		
		// Transform message payload into message payload
		RegistrationRequestMessageBundle registrationRequestMessageBundle = bundleMessage(registrationRequest, headers, "POST");	
			
		// TODO: Message is bundled with security data and sent off to the 'authorization' queue.
		
		RegistrationResponseMessageBundle registrationResponseMessageBundle = (RegistrationResponseMessageBundle)rabbitTemplateUser.convertSendAndReceive(registrationRequestMessageBundle, messagePostProcessor);		
		RegistrationResponse registrationResponse = registrationResponseMessageBundle.getPayload();
		
		// TODO: Returns an object that contains the authorization status of the request
		// TODO: If authorization fails, return JSON giving details of error 401
		// TODO: If access denied, return JSON giving details of error 403
		// TODO: If authorization successful return data to client
		
		System.out.println("obj returned from registerUser(): " + registrationResponse);		
		return registrationResponse;
	}

	// Bundle message
	private RegistrationRequestMessageBundle bundleMessage(RegistrationRequest registrationRequest, HttpHeaders headers, String method) {
		RegistrationRequestMessageBundle registrationRequestMessageBundle =  new RegistrationRequestMessageBundle(registrationRequest);
		registrationRequestMessageBundle.setHeaders(headers);
		registrationRequestMessageBundle.setMethod(method);
		return registrationRequestMessageBundle;
	}


}
