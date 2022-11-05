package ru.practicum.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.exception.exceptionClass.ExceptionConflict;
import ru.practicum.exception.exceptionClass.ExceptionNotFound;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        log.info("createUser: {}", user);
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            log.debug("User with email={} already exists", user.getEmail());
            throw new ExceptionConflict(String.format("User with email=%s already exists", user.getEmail()));
        }
    }

    public User getById(long id) {
        log.info("get user by id: {}", id);
        return userRepository.findById(id).orElseThrow(
                () -> new ExceptionNotFound(String.format("user with Id=%d is missing", id)));
    }

    public List<User> getAllByListId(Integer from, Integer size, List<Long> listId) {
        log.info("getAllByListId with from: {}, size: {}, listId: {}", from, size, listId);
        Pageable pageable = PageRequest.of(
                from / size,
                size);
        if (listId == null) {
            return userRepository.findAll(pageable).getContent();
        }
        return userRepository.findAllByIdIn(listId, pageable);
    }

    public void deleteUser(Long id) {
        log.info("deleteUser with id: {}", id);
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            log.debug("The user with id={} does not exist", id);
            throw new ExceptionNotFound(String.format("The user with id=%d does not exist", id));
        }
    }

}
