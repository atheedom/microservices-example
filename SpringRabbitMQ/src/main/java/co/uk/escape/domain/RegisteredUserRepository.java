package co.uk.escape.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface  RegisteredUserRepository extends MongoRepository<RegisteredUser, String>{

}
