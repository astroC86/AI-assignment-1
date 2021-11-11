package heuristics;

import model.EightPuzzle;

public interface Heuristic {
    double evaluate(EightPuzzle state);
}
