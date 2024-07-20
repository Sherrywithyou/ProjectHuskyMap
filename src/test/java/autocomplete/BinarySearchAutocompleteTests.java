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

}
