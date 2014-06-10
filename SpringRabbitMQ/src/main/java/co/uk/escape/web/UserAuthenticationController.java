package co.uk.escape.web;



import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.amqp.core.Queue;

import co.uk.escape.RMQTemplate;
import co.uk.escape.domain.LoginRequest;
import co.uk.escape.domain.LoginRequestMessageBundle;
import co.uk.escape.domain.LoginResponse;
import co.uk.escape.domain.LoginResponseMessageBundle;



import org.springframework.hateoas.LinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.rabbitmq.client.Channel;

@RestController
@RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserAuthenticationController {

	@Autowired @RMQTemplate(RMQTemplate.Type.LOGIN_USER)
	RabbitTemplate rabbitTemplateInfo;
	
	
	// TODO: Security intercepter that extracts and prepares security data for authentication
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest, @RequestHeader HttpHeaders headers ) {
	
		System.out.println("loginUser: enter REST: " + loginRequest);
		
		MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
	   		public Message postProcessMessage(Message message) throws AmqpException {
//	   			Date timestamp = new Date(); // TODO: Change to UTC date/time and use Joda-time (generally considered better than Java date)
//	   			message.getMessageProperties().setAppId(appId); // TODO: Investigate NOTE: If this is set the message is not sent and no error is thrown.
//	   			message.getMessageProperties().setUserId(userId); // TODO: Investigate NOTE: If this is set the message is not sent and no error is thrown.
//	   			message.getMessageProperties().setTimestamp(timestamp);  // TODO: Investigate NOTE: If this is set the message is not sent and no error is thrown.
	   			message.getMessageProperties().setHeader("message-type", "login-request");
	   			return message;  
	   		} 
		};
		
		// TODO: Message is bundled with security data and sent of to the 'authorization' queue.		
		// Transform message payload into message payload
		LoginRequestMessageBundle loginRequestMessageBundle = bundleMessage(loginRequest, headers);	

		
		Object obj = rabbitTemplateInfo.convertSendAndReceive(loginRequestMessageBundle, messagePostProcessor);
	
		// TODO: Returns an object that contains the authorization status of the request
		// TODO: If authorization fails, return JSON giving details of error 401
		// TODO: If access denied, return JSON giving details of error 403
		// TODO: If authorization successful return data to client
				
		LoginResponseMessageBundle loginResponseMessageBundle = (LoginResponseMessageBundle)obj;		
		LoginResponse loginResponse = loginResponseMessageBundle.getPayload();
		
		System.out.println("loginUser: exit REST "+ loginResponse);

		
		 //HttpHeaders responseHeaders = new HttpHeaders();
		 //responseHeaders.setLocation(linkTo(methodOn(UserRegistrationController.class)).toUri());
		
		
		HttpStatus httpStatus;
		Map<String, String> res = new HashMap<String, String>();
		if (loginResponse.getAuthorised()) {
			httpStatus = HttpStatus.OK;
			res.put("status", httpStatus.toString());
		} else {			
			httpStatus = HttpStatus.UNAUTHORIZED;
			res.put("status", httpStatus.toString());
			res.put("message", httpStatus.getReasonPhrase());
			res.put("developersmessage", "User did not provide the correct credentials to login.");
			res.put("moreinfo", "http://localhost/moreinfo?code="+httpStatus.toString());	
		}
		
	
		loginResponse.setResponse(res);
				
		loginResponse.add(linkTo(UserAuthenticationController.class).withSelfRel());
		loginResponse.add(linkTo(UserRegistrationController.class).withRel("register"));
		loginResponse.add(linkTo(UserInfoController.class).withRel("userinfo"));
		
		
		ResponseEntity<LoginResponse> response = new ResponseEntity<LoginResponse>(loginResponse, httpStatus);
		

		return response;
	}

	// Bundle message
	private LoginRequestMessageBundle bundleMessage(LoginRequest object, HttpHeaders headers) {
		return new LoginRequestMessageBundle(object, headers);
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
