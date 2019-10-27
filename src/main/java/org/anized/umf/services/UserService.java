package org.anized.umf.services;

import org.anized.common.Try;
import org.anized.umf.model.User;
import org.anized.umf.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static Logger LOGGER =
            LoggerFactory.getLogger(UserService.class);
    private UserRepository storage;

    @Autowired
    public UserService(final UserRepository storage) {
       this.storage = storage;
    }

    public Try<Boolean> create(final User user) {
        final User canon = User.builder(user)
                .setFirstName(user.getFirstName().trim().toLowerCase())
                .setSurname(user.getSurname().trim().toLowerCase())
                .build();
        return Try.apply(() -> {
            if (read(canon).isPresent()) {
                throw new IllegalStateException("User already exists: " + canon);
            } else {
                storage.save(canon);
                return true;
            }
        });
    }


    public Optional<User> read(final User user) {
        return findByName(user.getSurname(), user.getFirstName());
    }

    public Try<User> update(final User user) {
        return Try.apply(() -> {
            if (findUserById(user.getId()).isPresent()) {
                storage.save(user);
                return user;
            } else {
                throw new IllegalStateException("User does not exist: " + user);
            }
        });
    }

    public Try<User> delete(final BigInteger userId) {
        final Optional<User> maybeUser = storage.findById(userId);
        LOGGER.debug("deleting user id={}: {}", userId,
                maybeUser.map(User::toString).orElse("<not found>"));
        return Try.apply(() -> {
            if (!maybeUser.isPresent()) {
                throw new IllegalStateException("User does not exist for id=" + userId);
            } else {
                final User user = maybeUser.get();
                storage.delete(user);
                return user;
            }
        });
    }

    public List<User> findAll() {
        return storage.findAll();
    }

    public long count() {
        return storage.count();
    }

    private Optional<User> findByName(final String surname, final String forename) {
        return storage.findBySurnameAndFirstName(
                surname.trim().toLowerCase(), forename.trim().toLowerCase());
    }

    public Optional<User> findUserById(final BigInteger userId) {
        return storage.findById(userId);
    }

}
