package autocomplete;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the {@link BinarySearchAutocomplete} class.
 *
 * @see BinarySearchAutocomplete
 */
public class BinarySearchAutocompleteTests extends AutocompleteTests {
    @Override
    public Autocomplete createAutocomplete() {
        return new BinarySearchAutocomplete();
    }
    @Test
    void compareSimple() {
        List<CharSequence> terms = List.of(
                "alpha", "delta", "do", "cats", "dodgy", "pilot", "dog"
        );
        CharSequence prefix = "do";
        List<CharSequence> expected = List.of("do", "dodgy", "dog");

        Autocomplete testing = createAutocomplete();
        testing.addAll(terms);
        List<CharSequence> actual = testing.allMatches(prefix);
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
        assertTrue(actual.containsAll(expected));
    }
}
