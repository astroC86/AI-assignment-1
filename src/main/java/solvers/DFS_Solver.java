package solvers;

import model.EightPuzzle;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class DFS_Solver {
    private class SearchNode {
        private final int   moves;
        private final EightPuzzle board;
        private SearchNode  previous;

        public SearchNode(EightPuzzle bd) {
            if (bd == null) throw new IllegalArgumentException();
            this.board    = bd;
            this.moves    = 0;
        }

        public SearchNode(EightPuzzle board, SearchNode prev) {
            if (board == null) throw new IllegalArgumentException();
            if (prev == null) throw new IllegalArgumentException();
            this.board     = board;
            this.previous  = prev;
            this.moves     = prev.moves + 1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SearchNode that = (SearchNode) o;
            return board.equals(that.board);
        }
    }

    private SearchNode current;
    private int nodesExpanded  = 0;
    private int searchDepth    = 0;

    public DFS_Solver(EightPuzzle initialState) {
        if (initialState == null)
            throw new IllegalArgumentException();

        current = new SearchNode(initialState);

        if(!current.board.isSolvable()) return;

        Stack<SearchNode> frontier = new Stack<>();
        Set<EightPuzzle> frontierSet = new HashSet<>();
        Set<EightPuzzle> explored  =  new HashSet<>();

        frontier.push(current);
        frontierSet.add(current.board);
        nodesExpanded++;

        while(!frontier.isEmpty()){
            current = frontier.pop();

            explored.add(current.board);
            frontierSet.remove(current.board);

            if (current.board.isGoalState()) break;

            for (var nb : current.board.getNeighbours()) {
                if (current.previous == null || !nb.equals(current.previous.board)) {
                    if(!explored.contains(nb) &&  !frontierSet.contains(nb)){
                        nodesExpanded++;
                        searchDepth = Integer.max(current.moves + 1, searchDepth);
                        if (nb.isGoalState()) {
                            current = new SearchNode(nb, current);
                            return;
                        }
                        frontier.push(new SearchNode(nb, current));
                        frontierSet.add(nb);
                    }
                }
            }
        }
    }

    public int moves() {
        if (!isSolvable()) return -1;
        return this.current.moves;
    }

    public boolean isSolvable() {
        return current.board.isSolvable();
    }

    public EightPuzzle[] solution() {
        // If we took two steps, then we have three states, etc..
        // ie, the number of states we have is the cost + 1.
        var moves = moves();
        if (moves == -1) {
            // For the purpose of the solution, if we have an unsolvable board, we want
            // to produce an array containing only the initial state.
            moves = 0;
        }

        var result = new EightPuzzle[moves + 1];
        var node = current;
        for (int i = result.length - 1; i >= 0; i--) {
            result[i] = node.board;
            node = node.previous;
        }

        return result;
    }

    public int getNumberNodesExpanded() {
        return nodesExpanded;
    }

    public int getSearchDepth() {
        return searchDepth;
    }

}
