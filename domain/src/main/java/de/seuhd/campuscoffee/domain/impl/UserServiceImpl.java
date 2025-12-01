package de.seuhd.campuscoffee.domain.impl;

import de.seuhd.campuscoffee.domain.exceptions.DuplicationException;
import de.seuhd.campuscoffee.domain.model.Pos;
import de.seuhd.campuscoffee.domain.ports.UserDataService;
import de.seuhd.campuscoffee.domain.ports.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import de.seuhd.campuscoffee.domain.model.User;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    // TODO: Implement user service
    private final UserDataService userDataService;
    //private final UserService userService;

    @Override
    public @NonNull  List<User> getAll(){
        log.debug("Retrieving all users");
        return userDataService.getAll();
    }

    @Override
    public void clear(){
        log.debug("Clearing all users");
        userDataService.clear();

    }

    @Override
    public @NonNull User getById(@NonNull Long id){
        log.debug("Retrieving user by id: {}", id);
        return userDataService.getById(id);
    }

    @Override
    public @NonNull User getByName(@NonNull String name){
        log.debug("Retrieving user by name: {}", name);
        return userDataService.getByLoginName(name);
    }


    @Override
    public @NonNull User upsert(@NonNull User user) {
        if (user.id() == null) {
            // create a new User
            log.info("Creating new User: {}", user.loginName());
        } else {
            // update an existing User
            log.info("Updating User with ID: {}", user.id());
            // User ID must be set
            Objects.requireNonNull(user.id());
            // User must exist in the database before the update
            userDataService.getById(user.id());
        }
        return performUpsert(user);
    }

    @Override
    public void delete(@NonNull Long id){
        log.debug("Trying to delete user with ID: {}", id);
        userDataService.delete(id);
        log.debug("Deleted User with ID: {}", id);
    }

    /**
     * Performs the actual upsert operation with consistent error handling and logging.
     * Database constraint enforces name uniqueness - data layer will throw DuplicateEntityException if violated.
     * JPA lifecycle callbacks (@PrePersist/@PreUpdate) set timestamps automatically.
     *
     * @param user the User to upsert
     * @return the persisted User with updated ID and timestamps
     * @throws DuplicationException if a User with the same name already exists
     */
    private @NonNull User performUpsert(@NonNull User user) {
        try {
            User upsertedUser = userDataService.upsert(user);
            log.info("Successfully upserted User with ID: {}", upsertedUser.id());
            return upsertedUser;
        } catch (DuplicationException e) {
            log.error("Error upserting User '{}': {}", user.loginName(), e.getMessage());
            throw e;
        }
    }
}
