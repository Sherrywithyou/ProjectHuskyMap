package graphs.shortestpaths;

import graphs.Edge;
import graphs.Graph;

import java.util.*;

/**
 * Topological sorting implementation of the {@link ShortestPathSolver} interface for <b>directed acyclic graphs</b>.
 *
 * @param <V> the type of vertices.
 * @see ShortestPathSolver
 */
public class ToposortDAGSolver<V> implements ShortestPathSolver<V> {
    private final Map<V, Edge<V>> edgeTo;
    private final Map<V, Double> distTo;
    /**
     * Constructs a new instance by executing the toposort-DAG-shortest-paths algorithm on the graph from the start.
     *
     * @param graph the input graph.
     * @param start the start vertex.
     */
    public ToposortDAGSolver(Graph<V> graph, V start) {
        edgeTo = new HashMap<>();
        distTo = new HashMap<>();
        List<V> order = new ArrayList<>();
        Set<V> visited = new HashSet<>();
        dfsPostOrder(graph, start, visited, order);
        Collections.reverse(order);
        distTo.put(start, 0.0);
        for (V vertex : order) {
            if (!distTo.containsKey(vertex)) continue;
            for (Edge<V> edge : graph.neighbors(vertex)) {
                relax(edge);
            }
        }
    }




    /**
     * Recursively adds nodes from the graph to the result in DFS postorder from the start vertex.
     *
     * @param graph   the input graph.
     * @param vertex   the start vertex.
     * @param visited the set of visited vertices.
     * @param result  the destination for adding nodes.
     */
    private void dfsPostOrder(Graph<V> graph, V vertex, Set<V> visited, List<V> result) {
        visited.add(vertex);
        for (Edge<V> edge : graph.neighbors(vertex)) {
            if (!visited.contains(edge.to)) {
                dfsPostOrder(graph, edge.to, visited, result);
            }
        }
        result.add(vertex);
    }


    private void relax(Edge<V> edge) {
        V from = edge.from;
        V to = edge.to;
        double newDist = distTo.get(from) + edge.weight;
        if (newDist < distTo.getOrDefault(to, Double.POSITIVE_INFINITY)) {
            distTo.put(to, newDist);
            edgeTo.put(to, edge);
        }
    }

    @Override
    public List<V> solution(V goal) {
        List<V> path = new ArrayList<>();
        V curr = goal;
        path.add(curr);
        while (edgeTo.get(curr) != null) {
            curr = edgeTo.get(curr).from;
            path.add(curr);
        }
        Collections.reverse(path);
        return path;
    }
}