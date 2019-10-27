package org.anized.umf.persistence;

import java.math.BigInteger;
import java.util.Optional;

import org.anized.umf.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, BigInteger> {
    Optional<User> findBySurnameAndFirstName(final String surname, final String firstName);
}