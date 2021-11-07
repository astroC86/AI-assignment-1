package solvers;

import java.util.HashSet;
import java.util.PriorityQueue;

import heuristics.Heuristic;
import model.EightPuzzle;

public final class AStarSolver {
    private AStarSolver() {}

    public static AStarNode solve(EightPuzzle initialState, Heuristic heuristic) {
        double h = heuristic.evaluate(initialState);

        var frontier = new PriorityQueue<AStarNode>();
        frontier.add(new AStarNode(initialState, null, 0, h));
        var explored = new HashSet<EightPuzzle>();
        while (!frontier.isEmpty()) {
            var node = frontier.remove();
            var state = node.state();
            explored.add(state);

            if (state.isGoalState()) {
                return node;
            }

            for (var neighbour : state.getNeighbours()) {
                var newNode = new AStarNode(neighbour, state, node.g() + 1, heuristic.evaluate(neighbour));
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

        return null; // TODO: Throw no solution?
    }
}
