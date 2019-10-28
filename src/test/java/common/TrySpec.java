package common;

import org.anized.common.Try;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.fail;

class TrySpec {

    @Test
    @DisplayName("Try results in Failure")
    void tryFails() {
        final Try<String> result = Try.apply(() -> {
            throw new RuntimeException("boom!");
        });
        assertFalse(result.isSuccess());
        result
            .onFailure(e -> assertEquals(e.getMessage(), "boom!"))
            .onSuccess(res -> fail("operation should not have succeeded"));
    }

    @Test
    @DisplayName("Try results in Success")
    void trySucceeds() {
        final Try<String> result = Try.apply(() -> "successful");
        assertTrue(result.isSuccess());
        result.onFailure(e -> fail("operation should not have failed"));
    }
}
