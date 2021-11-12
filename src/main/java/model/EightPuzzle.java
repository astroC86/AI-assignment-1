package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class EightPuzzle {
    private static final int SIDE_LENGTH = 3;
    // Index `i` and index `3-i` represent legal moves in the directions of x and y respectively.
    // So, possible movements are:
    //  x, y
    // -1, 0
    //  1, 0
    //  0, 1
    //  0,-1
    private static final int[] TRANSLATION_ARR = {-1, 1, 0, 0};

    private final long currentState;
    private final int emptyIndex;

    // |0|1|2|
    // |3|4|5|
    // |6|7|8|
    public EightPuzzle(int[] initialState) {
        checkState(initialState);
        this.currentState = StateHelper.fromArray(initialState);
        if (currentState < 0) {
            var x = 0;
        }
        this.emptyIndex = getEmptyIndex();
    }

    private EightPuzzle(long initialState, int emptyIndex) {
        this.currentState = initialState;
        if (currentState < 0) {
            var x = 0;
        }
        this.emptyIndex = emptyIndex;
    }

    private static void checkState(int[] initialState) {
        if (initialState == null) {
            throw new IllegalArgumentException("Initial Board State cannot be NULL.");
        }

        if (initialState.length != 9) {
            throw new IllegalStateException("Initial Board State must be size 9.");
        }

        final int MAX_TILES = SIDE_LENGTH * SIDE_LENGTH;
        boolean[] bitmap = new boolean[MAX_TILES];
        for (int item : initialState) {
            if (!(bitmap[item] ^= true)) {
                throw new IllegalStateException("Initial Board State must not contain duplicate values.");
            }
        }
    }

    public int getNumberAtIndex(int index) {
        return StateHelper.getAtIndex(index, currentState);
    }

    // Called ONLY once in the constructor and result stored in emptyIndex field.
    private int getEmptyIndex() {
        for (int i = 0; i < 9; i++) {
            if (getNumberAtIndex(i) == 0) {
                return i;
            }
        }
        throw new IllegalStateException("State doesn't contain a zero (blank tile).");
    }

    public int[] arrayCoordinateToBoardCoordinates(int x) {
        // where x is the array index of the blank tile
        int r = x / SIDE_LENGTH;
        return new int[]{r, x - r * SIDE_LENGTH};
    }

    public int boardCoordinatesToArrayCoordinate(int r, int c) {
        return c + r * SIDE_LENGTH;
    }

    public Iterable<EightPuzzle> getNeighbours() {
        int[] blankCoordinate = arrayCoordinateToBoardCoordinates(emptyIndex);
        List<EightPuzzle> neighbourBoards = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            if ((blankCoordinate[0] + TRANSLATION_ARR[i] > -1 &&
                    blankCoordinate[0] + TRANSLATION_ARR[i] < SIDE_LENGTH) &&
                    (blankCoordinate[1] + TRANSLATION_ARR[SIDE_LENGTH - i] > -1 &&
                            blankCoordinate[1] + TRANSLATION_ARR[SIDE_LENGTH - i] < SIDE_LENGTH)) {

                var newBlankCoordinate = boardCoordinatesToArrayCoordinate(
                        blankCoordinate[0] + TRANSLATION_ARR[i],
                        blankCoordinate[1] + TRANSLATION_ARR[SIDE_LENGTH - i]);

                long newState = StateHelper.setAtIndex(emptyIndex, getNumberAtIndex(newBlankCoordinate), currentState);
                newState = StateHelper.setAtIndex(newBlankCoordinate, 0, newState);
                var newBoard = new EightPuzzle(newState, newBlankCoordinate);

                neighbourBoards.add(newBoard);
            }
        }
        return neighbourBoards;
    }

    public boolean isSolvable() {
        int inversions = 0;
        for (int i = 0; i < 9; i++) {
            if (i == emptyIndex) continue;
            for (int j = i + 1; j < 9; j++) {
                if (j == emptyIndex) continue;
                if (StateHelper.getAtIndex(j, currentState) > StateHelper.getAtIndex(i, currentState)) {
                    inversions++;
                }
            }
        }
        return inversions % 2 != 1;
    }

    public boolean isGoalState() {
        return currentState == 0x876543210L;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EightPuzzle that = (EightPuzzle) o;
        return currentState == that.currentState;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(currentState);
    }
}