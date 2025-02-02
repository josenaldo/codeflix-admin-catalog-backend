package br.com.josenaldo.codeflix.domain.pagination;

/**
 * Represents a range defined by a start and an end value.
 * <p>
 * This record is typically used to specify a range of indices, such as those used for paginating
 * results. The {@code start} value generally represents the beginning index, while the {@code end}
 * value represents the ending index, I.E., the range is inclusive of both the start and end
 * values.
 * <p>
 * Example:
 * <pre>
 *     Range range = Range.of(0, 9);
 *     int start = range.start(); // 0
 *     int end = range.end();     // 9
 *     int size = end - start + 1; // 10
 *     for (int i = start; i <= end; i++) {
 *         // Do something with the index i
 *     }
 * </pre>
 *
 * @param start the starting index of the range.
 * @param end   the ending index of the range.
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public record Range(int start, int end) {

    /**
     * Creates a new {@code Range} instance with the specified start and end values.
     *
     * @param start the starting index of the range.
     * @param end   the ending index of the range.
     * @return A new {@code Range} object initialized with the given start and end values.
     */
    public static Range of(int start, int end) {
        return new Range(start, end);
    }
}
