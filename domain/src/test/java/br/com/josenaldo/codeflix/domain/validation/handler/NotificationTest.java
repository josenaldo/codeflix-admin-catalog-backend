package br.com.josenaldo.codeflix.domain.validation.handler;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.josenaldo.codeflix.domain.exceptions.DomainException;
import br.com.josenaldo.codeflix.domain.validation.Error;
import br.com.josenaldo.codeflix.domain.validation.ValidationHandler;
import java.util.List;
import org.junit.jupiter.api.Test;

class NotificationTest {

    /**
     * Tests that creating a Notification with the static create() method returns an empty
     * notification.
     */
    @Test
    public void givenCreateMethod_whenCalled_thenNotificationHasNoErrors() {
        // Arrange - Given

        // Act - When
        Notification notification = Notification.create();

        // Assert - Then
        assertThat(notification).isNotNull();
        assertThat(notification.hasErrors()).isFalse();
        assertThat(notification.getErrors()).isEmpty();
    }

    /**
     * Tests that creating a Notification with a given Error returns a notification containing that
     * error.
     */
    @Test
    public void givenCreateWithErrorMethod_whenCalled_thenNotificationHasThatError() {
        // Arrange - Given
        Error error = new Error("Test error");

        // Act - When
        Notification notification = Notification.create(error);

        // Assert - Then
        assertThat(notification).isNotNull();
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors()).containsExactly(error);
    }

    /**
     * Tests that creating a Notification with a Throwable returns a notification containing an
     * error with the throwable's message.
     */
    @Test
    public void givenCreateWithThrowableMethod_whenCalled_thenNotificationContainsThrowableMessage() {
        // Arrange - Given
        Throwable throwable = new RuntimeException("Throwable error");

        // Act - When
        Notification notification = Notification.create(throwable);

        // Assert - Then
        assertThat(notification).isNotNull();
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors().getFirst().message()).isEqualTo("Throwable error");
    }

    /**
     * Tests that appending an Error to a Notification adds the error to the list.
     */
    @Test
    public void givenNotification_whenAppendError_thenErrorIsAdded() {
        // Arrange - Given
        Notification notification = Notification.create();
        Error error = new Error("Another error");

        // Act - When
        notification.append(error);

        // Assert - Then
        assertThat(notification.getErrors()).containsExactly(error);
        assertThat(notification.hasErrors()).isTrue();
    }

    /**
     * Tests that appending errors from another ValidationHandler adds those errors.
     */
    @Test
    public void givenNotification_whenAppendValidationHandler_thenReturnsNullAndAddsErrors() {
        // Arrange - Given
        Notification notification = Notification.create();
        Notification anotherNotification = Notification.create(new Error("Error from handler"));

        // Act - When
        Notification result = notification.append(anotherNotification);

        // Assert - Then
        assertThat(result).isNotNull();
        assertThat(result.getErrors()).contains(new Error("Error from handler"));
    }

    /**
     * Tests that validate() executes the given validation logic when no exception is thrown,
     * leaving the notification unchanged.
     */
    @Test
    public void givenValidValidation_whenCallingValidate_thenNoErrorsAreAdded() {
        // Arrange - Given
        Notification notification = Notification.create();
        ValidationHandler.Validation validation = () -> {
            // No exception thrown
        };

        // Act - When
        notification.validate(validation);

        // Assert - Then
        assertThat(notification.hasErrors()).isFalse();
        assertThat(notification.getErrors()).isEmpty();
    }

    /**
     * Tests that validate() catches a DomainException and adds its errors to the notification.
     */
    @Test
    public void givenDomainException_whenCallingValidate_thenDomainErrorsAreAdded() {
        // Arrange - Given
        Notification notification = Notification.create();
        Error error1 = new Error("Domain error 1");
        Error error2 = new Error("Domain error 2");
        ValidationHandler.Validation validation = () -> {
            throw DomainException.with(List.of(error1, error2));
        };

        // Act - When
        notification.validate(validation);

        // Assert - Then
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors()).containsExactlyInAnyOrder(error1, error2);
    }

    /**
     * Tests that validate() catches a generic Exception and adds its message as an error.
     */
    @Test
    public void givenGenericException_whenCallingValidate_thenErrorWithExceptionMessageIsAdded() {
        // Arrange - Given
        Notification notification = Notification.create();
        ValidationHandler.Validation validation = () -> {
            throw new RuntimeException("Generic error");
        };

        // Act - When
        notification.validate(validation);

        // Assert - Then
        assertThat(notification.hasErrors()).isTrue();
        // Verifies that the first error's message matches the exception message.
        assertThat(notification.getErrors().getFirst().message()).isEqualTo("Generic error");
    }

    /**
     * Tests that hasErrors() returns false for a new Notification and true after errors are added.
     */
    @Test
    public void givenNotification_whenNoError_thenHasErrorsReturnsFalse_andAfterAppending_returnsTrue() {
        // Arrange - Given
        Notification notification = Notification.create();

        // Act - When
        boolean initiallyHasErrors = notification.hasErrors();
        notification.append(new Error("Error exists"));

        // Assert - Then
        assertThat(initiallyHasErrors).isFalse();
        assertThat(notification.hasErrors()).isTrue();
    }
}
