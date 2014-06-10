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
import co.uk.escape.domain.LoginRequest;
import co.uk.escape.domain.RegisteredUser;
import co.uk.escape.domain.RegistrationRequest;
import co.uk.escape.domain.RegistrationRequestMessageBundle;


import co.uk.escape.domain.RegistrationResponse;
import co.uk.escape.domain.RegistrationResponseMessageBundle;
import co.uk.escape.domain.UserInfoRequest;
import co.uk.escape.domain.UserInfoRequestMessageBundle;
import co.uk.escape.domain.UserInfoResponse;
import co.uk.escape.domain.UserInfoResponseMessageBundle;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping(value = "/userinfo", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserInfoController extends ResourceSupport{

	@Autowired @RMQTemplate(RMQTemplate.Type.USER_INFO)
	RabbitTemplate rabbitTemplateUser;
	
	@RequestMapping(method = RequestMethod.GET)
	public UserInfoResponse registerUser(@RequestHeader HttpHeaders headers){
		System.out.println("in the controller: registerUser()");
			
		MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
	   		public Message postProcessMessage(Message message) throws AmqpException {
	   			message.getMessageProperties().setHeader("message-type", "user-info-request");
	   			return message;  
	   		} 
		};
		
		
		// Transform message payload into message payload
		UserInfoRequestMessageBundle userInfoRequestMessageBundle = bundleMessage(new UserInfoRequest(), headers, "GET");	
			
		UserInfoResponseMessageBundle userInfoResponseMessageBundle = 
				(UserInfoResponseMessageBundle)rabbitTemplateUser.convertSendAndReceive(userInfoRequestMessageBundle, messagePostProcessor);		
		UserInfoResponse userInfoResponse = userInfoResponseMessageBundle.getPayload();
		
		
		System.out.println("obj returned: " + userInfoResponse);		
		return userInfoResponse;
	}

	// Bundle message
	private UserInfoRequestMessageBundle bundleMessage(UserInfoRequest userInfoRequest, HttpHeaders headers, String method) {
		UserInfoRequestMessageBundle userInfoRequestMessageBundle =  new UserInfoRequestMessageBundle(userInfoRequest, headers, method);
		return userInfoRequestMessageBundle;
	}


}
