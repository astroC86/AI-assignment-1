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

    private AStarSolver() {}

    private static Stack<EightPuzzle> constructSolution(AStarNode node) {
        var stack = new Stack<EightPuzzle>();
        while (node != null) {
            stack.push(node.state());
            node = node.parent();
        }

        return stack;
    }

    public static Stack<EightPuzzle> solve(EightPuzzle initialState, Heuristic heuristic) throws UnresolvableBoardException {
        double h = heuristic.evaluate(initialState);

        var frontier = new PriorityQueue<AStarNode>();
        frontier.add(new AStarNode(initialState, 0, h));
        var explored = new HashSet<EightPuzzle>();
        while (!frontier.isEmpty()) {
            var node = frontier.remove();
            var state = node.state();
            explored.add(state);

            if (state.isGoalState()) {
                return constructSolution(node);
            }

            for (var neighbour : state.getNeighbours()) {
                var newNode = new AStarNode(neighbour, node, node.g() + 1, heuristic.evaluate(neighbour));
                var frontierContainsNewNode = frontier.contains(newNode);
                if (!frontierContainsNewNode && !explored.contains(neighbour)) {
                    frontier.add(newNode);
                }
                else if (frontierContainsNewNode) {
                    var isRemoved = frontier.removeIf(n -> n.g() + n.h() > newNode.g() + newNode.h());
                    if (isRemoved) {
                        frontier.add(newNode);
                    }
                }
            }
        }

        throw new UnresolvableBoardException();
    }
}
