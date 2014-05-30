package co.uk.escape.web;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import co.uk.escape.domain.LoginRequest;
import co.uk.escape.domain.LoginResponse;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserAuthenticationController {

	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@RequestMapping(method = RequestMethod.POST)
	public LoginResponse loginUser(@RequestBody LoginRequest loginRequest) {
		
		System.out.println("loginUser: enter REST: " + loginRequest);
		
		Object obj = rabbitTemplate.convertSendAndReceive(loginRequest);
		System.out.println("UUID " + rabbitTemplate.getUUID());
		
		LoginResponse loginResponse = (LoginResponse)obj;
		
		System.out.println("loginUser: exit REST "+ loginResponse);
		return loginResponse;
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