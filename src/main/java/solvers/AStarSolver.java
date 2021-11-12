package solvers;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Stack;

import heuristics.Heuristic;
import model.EightPuzzle;

public final class AStarSolver {
    private final record AStarNode(EightPuzzle state, AStarNode parent, int g, double h) implements Comparable<AStarNode> {
        public AStarNode(EightPuzzle state, int g, double h) {
            this(state, null, g, h);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;

            if (o == null)
                return false;

            if (getClass() != o.getClass())
                return false;
            AStarNode node = (AStarNode) o;
            // Intentionally: DON'T check parent, g, or h to let the frontier.contains calls work as expected.
            return state.equals(node.state);

        }

        @Override
        public int compareTo(AStarNode o) {
            return Double.compare(g + h, o.g + o.h);
        }
    }

    private int searchDepth;
    private int nodesExpanded;

    private AStarSolver() {}

    private static EightPuzzle[] constructSolution(AStarNode node) {
        // If we took two steps, then we have three states, etc..
        // ie, the number of states we have is the cost + 1.
        var result = new EightPuzzle[node.g() + 1];

        for (int i = node.g(); i >= 0; i--) {
            result[i] = node.state();
            node = node.parent();
        }

        return result;
    }

    public EightPuzzle[] solve(EightPuzzle initialState, Heuristic heuristic) throws UnresolvableBoardException {
        searchDepth   = 0;
        nodesExpanded = 0;
        double h = heuristic.evaluate(initialState);

        var frontier = new PriorityQueue<AStarNode>();
        var frontierHashSet = new HashSet<AStarNode>();
        var initialNode = new AStarNode(initialState, 0, h);
        frontier.add(initialNode);
        frontierHashSet.add(initialNode);
        var explored = new HashSet<EightPuzzle>();
        nodesExpanded++;
        while (!frontier.isEmpty()) {
            var node = frontier.remove();
            frontierHashSet.remove(node);
            var state = node.state();
            explored.add(state);

            if (state.isGoalState()) {
                return constructSolution(node);
            }

            for (var neighbour : state.getNeighbours()) {
                var newNode = new AStarNode(neighbour, node, node.g() + 1, heuristic.evaluate(neighbour));
                var frontierContainsNewNode = frontierHashSet.contains(newNode);
                if (!frontierContainsNewNode && !explored.contains(neighbour)) {
                    frontier.add(newNode);
                    frontierHashSet.add(newNode);
                    nodesExpanded++;
                }
                else if (frontierContainsNewNode) {
                    var isRemoved = frontier.removeIf(n -> n.state().equals(newNode.state()) && n.g() + n.h() > newNode.g() + newNode.h());
                    if (isRemoved) {
                        frontier.add(newNode);
                        // There is no need to touch frontierHashSet in this code path.
                        // We don't use the cost of hash set nodes.
                    }
                }
                searchDepth = Integer.max(newNode.g(), searchDepth);
            }
        }

        throw new UnresolvableBoardException();
    }
    public int getNumberNodesExpanded() {
        return nodesExpanded;
    }

    public int getSearchDepth() {
        return searchDepth;
    }
}
