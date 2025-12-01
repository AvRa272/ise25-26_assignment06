package de.seuhd.campuscoffee.domain.model;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.NonNull;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Domain record that stores the User metadata.
 * This is an immutable value object - use the builder or toBuilder() to create modified copies.
 * Records provide automatic implementations of equals(), hashCode(), toString(), and accessors.
 * <p>
 * We validate the fields in the API layer based on the DTOs, so no validation annotations are needed here.
 *
 * @param id                the unique identifier; null when the User has not been created yet
 * @param createdAt         timestamp set on User creation
 * @param updatedAt         timestamp set on User creation and update
 * @param firstName         the first name of the User
 * @param lastName          the last name of the User
 * @param loginName         the login name of the User
 * @param emailAddress      email address of the User
 */
@Builder(toBuilder = true)
public record User (
        //TODO: Implement user domain object
        @Nullable Long id,
        @Nullable LocalDateTime createdAt,
        @Nullable LocalDateTime updatedAt,

        @NonNull String loginName,
        @NonNull String firstName,
        @NonNull String lastName,
        @NonNull String emailAddress
) implements Serializable { // serializable to allow cloning (see TestFixtures class).
    @Serial
    private static final long serialVersionUID = 1L;
}
