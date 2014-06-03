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

import co.uk.escape.RMQTemplate;
import co.uk.escape.domain.LoginRequest;
import co.uk.escape.domain.LoginResponse;
import co.uk.escape.domain.RegisteredUser;
import co.uk.escape.domain.RegistrationRequest;
import co.uk.escape.domain.UserInfoRequest;

import org.springframework.http.MediaType;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRegistrationController {

	@Autowired @RMQTemplate(RMQTemplate.Type.REGISTER_USER)
	RabbitTemplate rabbitTemplateUser;
	
	// TODO: Security intercepter that extracts and prepares security data for authentication
	@RequestMapping(method = RequestMethod.POST)
	public RegisteredUser registerUser(@RequestBody RegistrationRequest newUserRegistrationRequest){
		System.out.println("in the controller: registerUser()");
		
		// TODO: Message is bundled with security data and sent of to the 'authorization' queue.
		RegisteredUser registeredUser = (RegisteredUser)rabbitTemplateUser.convertSendAndReceive(newUserRegistrationRequest);		
		
		// TODO: Returns an object that contains the authorization status of the request
		// TODO: If authorization fails, return JSON giving details of error 401
		// TODO: If access denied, return JSON giving details of error 403
		// TODO: If authorization successful return data to client
		
		System.out.println("obj returned from registerUser(): " + registeredUser);		
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
