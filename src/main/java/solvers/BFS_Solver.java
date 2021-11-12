package solvers;


import model.EightPuzzle;

import java.util.*;

public final class BFS_Solver {
    private final class SearchNode {
        private final int   moves;
        private final EightPuzzle board;
        private SearchNode  previous;

        public SearchNode(EightPuzzle bd) {
            if (bd == null) throw new IllegalArgumentException();
            this.board = bd;
            this.moves = 0;
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
    private int nodesExpanded;

    public BFS_Solver(EightPuzzle initialState) {
        nodesExpanded = 0;

        if (initialState == null)
            throw new IllegalArgumentException();

        current = new SearchNode(initialState);
        if (!current.board.isSolvable()) return;

        LinkedList<SearchNode> frontier = new LinkedList<>();
        Set<EightPuzzle> frontierSet = new HashSet<>();
        Set<EightPuzzle> explored  =  new HashSet<>();

        frontier.add(current);
        frontierSet.add(current.board);
        nodesExpanded++;

        while (!frontier.isEmpty()) {
            current = frontier.removeFirst();

            explored.add(current.board);
            frontierSet.remove(current.board);

            if (current.board.isGoalState()) break;
            for (var nb : current.board.getNeighbours()) {
                if (current.previous == null || !nb.equals(current.previous.board)) {
                    if(!explored.contains(nb) &&  !frontierSet.contains(nb)){
                        nodesExpanded++;
                        frontier.add(new SearchNode(nb, current));
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

    public Iterable<EightPuzzle> solution() {
        if (!isSolvable())
            return null;
        Stack<EightPuzzle> seq = new Stack<>();
        SearchNode runner = current;
        while (runner != null) {
            seq.push(runner.board);
            runner = runner.previous;
        }
        return seq;
    }

    public int getNumberNodesExpanded() {
        return nodesExpanded;
    }

    public int getSearchDepth() {
        return current.moves;
    }
}