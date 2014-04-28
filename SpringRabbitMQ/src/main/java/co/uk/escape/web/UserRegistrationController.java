package co.uk.escape.web;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.uk.escape.domain.RegisteredUser;
import co.uk.escape.domain.RegistrationRequest;
import co.uk.escape.domain.UserInfoRequest;

import org.springframework.http.MediaType;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRegistrationController {

	@Autowired @RMQTemplate(RMQTemplate.Type.CREATE_USER)
	RabbitTemplate rabbitTemplateUser;
	
	@Autowired @RMQTemplate(RMQTemplate.Type.GET_USER_INFO)
	RabbitTemplate rabbitTemplateInfo;
	
	@RequestMapping(method = RequestMethod.POST)
	public RegisteredUser registerUser(@RequestBody RegistrationRequest newUserRegistrationRequest){
		System.out.println("in the controller: registerUser()");
		
		RegisteredUser registeredUser = (RegisteredUser)rabbitTemplateUser.convertSendAndReceive(newUserRegistrationRequest);		
		
		System.out.println("obj returned from registerUser(): " + registeredUser);		
		return registeredUser;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{emailAddress}")
	public RegisteredUser getUserInfo(@PathVariable String emailAddress){
		System.out.println("in the controller: getUserInfo()");
		
		UserInfoRequest userInfoRequest = new UserInfoRequest(emailAddress);
		
		RegisteredUser registeredUser = (RegisteredUser)rabbitTemplateInfo.convertSendAndReceive(userInfoRequest);		
		
		System.out.println("obj returned from getUserInfo(): " + registeredUser);		
		return registeredUser;
	}
	
	
	@ExceptionHandler(DuplicateKeyException.class)
	ResponseEntity<String> duplicateKey(Exception e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(EmptyResultDataAccessException.class)
	ResponseEntity<String> handleNotFounds(Exception e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.GONE);
	}
}
