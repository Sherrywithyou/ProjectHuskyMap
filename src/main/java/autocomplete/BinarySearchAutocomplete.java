package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Binary search implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class BinarySearchAutocomplete implements Autocomplete {
    /**
     * {@link List} of added autocompletion terms.
     */
    private final List<CharSequence> elements;

    /**
     * Constructs an empty instance.
     */
    public BinarySearchAutocomplete() {
        elements = new ArrayList<>();
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        // Add all terms to the elements list
        elements.addAll(terms);
        // Sort the elements list
        Collections.sort(elements, CharSequence::compare);
    }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        // Find the insertion point using binary search
        int start = Collections.binarySearch(elements, prefix, CharSequence::compare);
        if (start < 0) {
            start = -(start + 1);
        }

        List<CharSequence> matches = new ArrayList<>();

        // Collect all matching terms
        for (int i = start; i < elements.size(); i++) {
            if (Autocomplete.isPrefixOf(prefix, elements.get(i))) {
                matches.add(elements.get(i));
            } else {
                break;
            }
        }

        return matches;
    }

}
