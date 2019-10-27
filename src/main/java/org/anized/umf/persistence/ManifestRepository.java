package org.anized.umf.persistence;

import java.math.BigInteger;
import java.util.Optional;

import org.anized.umf.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ManifestRepository extends MongoRepository<Person, BigInteger> {
    Optional<Person> findBySurnameAndFirstName(final String surname, final String firstName);
}