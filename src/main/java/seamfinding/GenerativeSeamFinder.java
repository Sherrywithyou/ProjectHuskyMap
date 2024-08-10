package seamfinding;

import graphs.Edge;
import graphs.Graph;
import graphs.shortestpaths.ShortestPathSolver;
import seamfinding.energy.EnergyFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Generative adjacency list graph single-source {@link ShortestPathSolver} implementation of the {@link SeamFinder}
 * interface.
 *
 * @see Graph
 * @see ShortestPathSolver
 * @see SeamFinder
 */
public class GenerativeSeamFinder implements SeamFinder {
    /**
     * The constructor for the {@link ShortestPathSolver} implementation.
     */
    private final ShortestPathSolver.Constructor<Node> sps;

    /**
     * Constructs an instance with the given {@link ShortestPathSolver} implementation.
     *
     * @param sps the {@link ShortestPathSolver} implementation.
     */
    public GenerativeSeamFinder(ShortestPathSolver.Constructor<Node> sps) {
        this.sps = sps;
    }

    @Override
    // Borrowed from AdjacencyListSeamFinder
    // Reason: The logic for creating the graph, running the shortest path solver,
    // and extracting the seam is universal and applicable to both implementations.
    public List<Integer> findHorizontal(Picture picture, EnergyFunction f) {
        PixelGraph graph = new PixelGraph(picture, f);
        List<Node> seam = sps.run(graph, graph.source).solution(graph.sink);
        seam = seam.subList(1, seam.size() - 1); // Skip the source and sink nodes
        List<Integer> result = new ArrayList<>(seam.size());
        for (Node node : seam) {
            // All remaining nodes must be Pixels
            PixelGraph.Pixel pixel = (PixelGraph.Pixel) node;
            result.add(pixel.y);
        }
        return result;
    }

    /**
     * Generative adjacency list graph of {@link Pixel} vertices and {@link EnergyFunction}-weighted edges. Rather than
     * materialize all vertices and edges upfront in the constructor, generates vertices and edges as needed when
     * {@link #neighbors(Node)} is called by a client.
     *
     * @see Pixel
     * @see EnergyFunction
     */
    // Borrowed the overall structure of PixelGraph, including the use of Picture, EnergyFunction, source, and sink nodes
    // The basic graph representation, with a source and sink node, remains the same in both implementations.
    // This structure is fundamental to the seam-finding process and is necessary for both classes.
    private static class PixelGraph implements Graph<Node> {
        /**
         * The {@link Picture} for {@link #neighbors(Node)}.
         */
        private final Picture picture;
        /**
         * The {@link EnergyFunction} for {@link #neighbors(Node)}.
         */
        private final EnergyFunction f;
        /**
         * Source {@link Node} for the adjacency list graph.
         */
        // Borrowed from AdjacencyListSeamFinder
        // Both classes define source and sink nodes that represent the start and end points of the graph traversal.
        // The source node connects to the first column of pixels, and the sink node connects to the last column.
        private final Node source = new Node() {
            @Override
            public List<Edge<Node>> neighbors(Picture picture, EnergyFunction f) {
                List<Edge<Node>> result = new ArrayList<>(picture.height());
                for (int j = 0; j < picture.height(); j++) {
                    Pixel to = new Pixel(0, j);
                    result.add(new Edge<>(this, to, f.apply(picture, 0, j)));
                }
                return result;
            }
        };
        /**
         * Sink {@link Node} for the adjacency list graph.
         */
        private final Node sink = new Node() {
            @Override
            public List<Edge<Node>> neighbors(Picture picture, EnergyFunction f) {
                return List.of(); // Sink has no neighbors
            }
        };

        /**
         * Constructs a generative adjacency list graph. All work is deferred to implementations of
         * {@link Node#neighbors(Picture, EnergyFunction)}.
         *
         * @param picture the input picture.
         * @param f       the input energy function.
         */
        // Different from AdjacencyListSeamFinder
        // In GenerativeSeamFinder, neighbors are not precomputed and stored.
        // The graph is generated on-demand when the neighbors() method is called.
        private PixelGraph(Picture picture, EnergyFunction f) {
            this.picture = picture;
            this.f = f;
            // Neighbors are not stored
        }

        @Override
        public List<Edge<Node>> neighbors(Node node) {
            return node.neighbors(picture, f);
        }

        /**
         * A pixel in the {@link PixelGraph} representation of the {@link Picture} with {@link EnergyFunction}-weighted
         * edges to neighbors.
         *
         * @see PixelGraph
         * @see Picture
         * @see EnergyFunction
         */
        public class Pixel implements Node {
            private final int x;
            private final int y;

            /**
             * Constructs a pixel representing the (<i>x</i>, <i>y</i>) indices in the picture.
             *
             * @param x horizontal index into the picture.
             * @param y vertical index into the picture.
             */
            public Pixel(int x, int y) {
                this.x = x;
                this.y = y;
            }

            @Override
            // Different from AdjacencyListSeamFinder
            // Reason: In GenerativeSeamFinder, neighbors are dynamically generated
            // each time the method is called, instead of being precomputed.
            public List<Edge<Node>> neighbors(Picture picture, EnergyFunction f) {
                List<Edge<Node>> result = new ArrayList<>();
                if (x < picture.width() - 1) {
                    for (int z = y - 1; z <= y + 1; z++) {
                        if (0 <= z && z < picture.height()) {
                            Pixel to = new Pixel(x + 1, z);
                            result.add(new Edge<>(this, to, f.apply(picture, x + 1, z)));
                        }
                    }
                } else {
                    result.add(new Edge<>(this, sink, 0));
                }
                return result;
            }

            @Override
            // Identical to AdjacencyListSeamFinder
            // Reason: these methods are to define how objects of the Pixel class should behave when they are compared,
            // printed, or used in hash-based collections
            public String toString() {
                return "(" + x + ", " + y + ")";
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                } else if (!(o instanceof Pixel)) {
                    return false;
                }
                Pixel other = (Pixel) o;
                return this.x == other.x && this.y == other.y;
            }

            @Override
            public int hashCode() {
                return Objects.hash(x, y);
            }
        }
    }
}
