package com.finanscepte.user.repository;

import com.finanscepte.user.model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

@Profile("mongo")
public interface UserMongoRepository extends MongoRepository<User, String> {
}
