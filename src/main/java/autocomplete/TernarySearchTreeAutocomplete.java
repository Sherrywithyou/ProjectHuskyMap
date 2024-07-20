package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Ternary search tree (TST) implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class TernarySearchTreeAutocomplete implements Autocomplete {
    /**
     * The overall root of the tree: the first character of the first autocompletion term added to this tree.
     */
    private Node overallRoot;

    /**
     * Constructs an empty instance.
     */
    public TernarySearchTreeAutocomplete() {
        overallRoot = null;
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        for (CharSequence term : terms) {
            overallRoot = add(overallRoot, term, 0);
        }
    }
// Referenced from line 146 to line 157 (put method) from TST.java
    private Node add(Node x, CharSequence term, int d) {
        char c = term.charAt(d);
        if (x == null) {
            x = new Node(c);
        }
        if (c < x.data) {
            x.left = add(x.left, term, d);
        } else if (c > x.data) {
            x.right = add(x.right, term, d);
        } else if (d < term.length() - 1) {
            x.mid = add(x.mid, term, d + 1);
        } else {
            x.isTerm = true;
        }
        return x;
    }
    // Referenced from line 207 to line 217 (keysWithPrefix method) from TST.JAVA
    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        List<CharSequence> matches = new ArrayList<>();
        Node x = get(overallRoot, prefix, 0);
        if (x == null) {
            return matches;
        }
        if (x.isTerm) {
            matches.add(prefix);
        }
        collect(x.mid, new StringBuilder(prefix), matches);
        return matches;
    }

    // Referenced from line 119 to line 127 (get method) from TST.JAVA
    private Node get(Node x, CharSequence prefix, int d) {
        if (x == null) {
            return null;
        }
        char c = prefix.charAt(d);
        if (c < x.data) {
            return get(x.left, prefix, d);
        } else if (c > x.data) {
            return get(x.right, prefix, d);
        } else if (d < prefix.length() - 1) {
            return get(x.mid, prefix, d + 1);
        } else {
            return x;
        }
    }

    // Referenced from line 220 to line 227 (collect method) from TST.JAVA
    private void collect(Node x, StringBuilder prefix, List<CharSequence> matches) {
        if (x == null) {
            return;
        }
        collect(x.left, prefix, matches);
        if (x.isTerm) {
            matches.add(prefix.toString() + x.data);
        }
        collect(x.mid, prefix.append(x.data), matches);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.right, prefix, matches);
    }


    /**
     * A search tree node representing a single character in an autocompletion term.
     */
    private static class Node {
        private final char data;
        private boolean isTerm;
        private Node left;
        private Node mid;
        private Node right;

        public Node(char data) {
            this.data = data;
            this.isTerm = false;
            this.left = null;
            this.mid = null;
            this.right = null;
        }
    }
}

