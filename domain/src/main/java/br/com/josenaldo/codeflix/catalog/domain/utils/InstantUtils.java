package br.com.josenaldo.codeflix.catalog.domain.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Provides utility methods for working with {@link Instant} objects.
 * <p>
 * This class offers specialized operations for managing time-related values in the domain, ensuring
 * consistent precision when generating or manipulating timestamps.
 * <p>
 * The {@code InstantUtils} class cannot be instantiated as it is intended solely as a utility
 * class.
 *
 * <p>
 * The primary purpose includes:
 * <ul>
 *   <li>Generating the current {@link Instant} truncated to a microsecond precision.</li>
 * </ul>
 * This truncation ensures that all {@link Instant} instances adhere to a consistent level of precision,
 * which is particularly useful for scenarios where timestamps are used in domain models or persistence.
 *
 * @author Josenaldo de Oliveira Matos Filho
 */
public final class InstantUtils {

    /**
     * Utility class providing methods for working with {@link Instant} objects.
     * <p>
     * This private constructor ensures that the {@code InstantUtils} class cannot be instantiated,
     * enforcing its design as a utility class containing static methods only.
     * <p>
     * The class is designed to offer precision-focused operations for handling time values,
     * specifically targeting microsecond-level precision where necessary.
     *
     * @author Josenaldo de Oliveira Matos Filho
     */
    private InstantUtils() {
    }

    /**
     * Returns the current instant truncated to microsecond precision.
     * <p>
     * This method ensures that the returned {@link Instant} has no more precision than
     * microseconds, discarding any nanoseconds. This is particularly useful for systems
     * requiring consistent timestamp precision, such as when storing or comparing time values.
     *
     * @return The current {@link Instant} truncated to microsecond precision.
     */
    public static Instant now() {
        return Instant.now().truncatedTo(ChronoUnit.MICROS);
    }
}
