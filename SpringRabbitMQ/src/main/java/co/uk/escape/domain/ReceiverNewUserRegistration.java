package co.uk.escape.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;



@Controller
public class ReceiverNewUserRegistration {
	
	@Autowired
	RegisteredUserRepository registeredUserRepository;
	
	public void saveNewUser(RegistrationRequest newUserRegistrationRequest) {
		RegisteredUser registeredUser = new RegisteredUser(null, 
											newUserRegistrationRequest.getEmailAddress(), 
											newUserRegistrationRequest.getPassword());

		registeredUserRepository.save(registeredUser);
        System.out.println("ReceiverNewUserRegistration <" + registeredUser + ">");
    }
	
}
