package heuristics;

import model.EightPuzzle;

public final class EuclideanHeuristic implements Heuristic {
    @Override
    public double evaluate(EightPuzzle state) {
        int sumMH = 0;// heuristic
        int currPos, targetPos, currR, currC, targetR, targetC; // stands for row and column
        int[] boardCoord;
        for (int i = 0; i < 9; i++) {

            currPos = i;
            targetPos = state.getNumberAtIndex(i);

            if (targetPos == 0)
                continue;

            boardCoord = state.arrayCoordinateToBoardCoordinates(currPos);
            currR = boardCoord[0];
            currC = boardCoord[1];

            boardCoord = state.arrayCoordinateToBoardCoordinates(targetPos);
            targetR = boardCoord[0];
            targetC = boardCoord[1];

            sumMH += Math.sqrt((Math.abs(targetR - currR)) ^ 2 + (Math.abs(targetC - currC)) ^ 2);

        }

        return sumMH;
    }
}
