package solvers;

import model.EightPuzzle;

public final record AStarNode(EightPuzzle state, EightPuzzle parent, int g, double h) implements Comparable<AStarNode> {
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
