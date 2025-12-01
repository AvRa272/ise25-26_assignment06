package de.seuhd.campuscoffee.tests.system;

import de.seuhd.campuscoffee.domain.model.User;
import de.seuhd.campuscoffee.domain.tests.TestFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import de.seuhd.campuscoffee.api.dtos.UserDto;

import java.util.List;
import java.util.Objects;

import static de.seuhd.campuscoffee.tests.SystemTestUtils.Requests.posRequests;
import static de.seuhd.campuscoffee.tests.SystemTestUtils.Requests.userRequests;
import static org.assertj.core.api.Assertions.assertThat;

public class UsersSystemTests extends AbstractSysTest {

    //TODO: Uncomment once user endpoint is implemented

    @Test
    void createUser() {
        User userToCreate = TestFixtures.getUserListForInsertion().getFirst();
        User createdUser = userDtoMapper.toDomain(userRequests.create(List.of(userDtoMapper.fromDomain(userToCreate))).getFirst());

        assertEqualsIgnoringIdAndTimestamps(createdUser, userToCreate);
    }

    //TODO: Add at least two additional tests for user operations

    // get user by ID
    @Test
    void getUserById() {
        List<User> userList = TestFixtures.createUsers(userService);
        User createdUser = userList.getFirst();

        User retrievedUser = userDtoMapper.toDomain(userRequests.retrieveById(createdUser.id()));

        assertEqualsIgnoringTimestamps(createdUser, retrievedUser);
    }

    // get User by login name
    @Test
    void filterUserByLoginName() {
        List<User> userList = TestFixtures.createUsers(userService);
        User createdUser = userList.getFirst();

        User retrievedUser = userDtoMapper.toDomain(userRequests.retrieveByFilter("login_name", createdUser.loginName()));

        assertEqualsIgnoringTimestamps(createdUser, retrievedUser);
    }

    // get all users
    @Test
    void getAllCreatedUsers() {
        List<User> userList = TestFixtures.createUsers(userService);
        List<User> retrievedUsers = userRequests.retrieveAll()
                .stream()
                .map(userDtoMapper::toDomain)
                .toList();

        assertEqualsIgnoringTimestamps(userList, retrievedUsers);
    }

    // update User
    @Test
    void UpdateUser() {
        List<User> userList = TestFixtures.createUsers(userService);
        User userToUpdate = TestFixtures.getUserList().getFirst();

        userToUpdate = userToUpdate.toBuilder()
                .loginName(userToUpdate.loginName() + "updated")
                .emailAddress("updated@email.com")
                .build();

        User updatedUser = userDtoMapper.toDomain(userRequests.update(List.of(userDtoMapper.fromDomain(userToUpdate))).getFirst());

        assertEqualsIgnoringTimestamps(userToUpdate, updatedUser);

        // verify changes persist
        User retrievedUser = userDtoMapper.toDomain(userRequests.retrieveById(updatedUser.id()));
        assertEqualsIgnoringTimestamps(userToUpdate, retrievedUser);

    }

    // delete User
    @Test
    void deleteUser() {
        List<User> userList = TestFixtures.createUsers(userService);
        User userToDelete = userList.getFirst();
        Objects.requireNonNull(userToDelete.id());

        List<Integer> statusCodes = userRequests.deleteAndReturnStatusCodes(List.of(userToDelete.id(), userToDelete.id()));

        assertThat(statusCodes)
                .containsExactly(HttpStatus.NO_CONTENT.value(), HttpStatus.NOT_FOUND.value());

        List<Long> remainingUserIds = userRequests.retrieveAll()
                .stream()
                .map(UserDto::id)
                .toList();

        assertThat(remainingUserIds).doesNotContain(userToDelete.id());
    }
}