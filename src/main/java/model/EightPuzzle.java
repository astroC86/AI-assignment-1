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

    private final int[] currentState;
    private final int emptyIndex;

    // |0|1|2|
    // |3|4|5|
    // |6|7|8|
    public EightPuzzle(int[] initialState) {
        checkState(initialState);
        // TODO: Is storing the state in a long (states need 4 * 9 bits = 36bits) better?
        //  Do it later and see how the improvement looks like.
        this.currentState = Arrays.copyOf(initialState, initialState.length);
        this.emptyIndex = getEmptyIndex();
    }

    private EightPuzzle(int[] initialState, int emptyIndex) {
        this.currentState = initialState.clone();
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

    public int getNumberAtIndex(int index){
        return currentState[index];
    }

    // Called ONLY once in the constructor and result stored in emptyIndex field.
    private int getEmptyIndex() {
        for (int i = 0; i < currentState.length; i++) {
            if (currentState[i] == 0) {
                return i;
            }
        }
        throw new IllegalStateException("State doesn't contain a zero (blank tile).");
    }

    public EightPuzzle takeAction(Action action, int emptyIndex) {
        var newEmptyIndex = switch (action) {
            case UP -> emptyIndex - SIDE_LENGTH;
            case DOWN -> emptyIndex + SIDE_LENGTH;
            case LEFT -> emptyIndex - 1;
            case RIGHT -> emptyIndex + 1;
            default -> throw new IllegalArgumentException("Model.Action can only be UP, DOWN, LEFT, or RIGHT.");
        };

        var newState = currentState.clone();
        newState[emptyIndex] = newState[newEmptyIndex];
        newState[newEmptyIndex] = 0;
        return new EightPuzzle(newState);
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
        List<EightPuzzle> neigbourBoards = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            if ((blankCoordinate[0] + TRANSLATION_ARR[i] > -1 &&
                    blankCoordinate[0] + TRANSLATION_ARR[i] < SIDE_LENGTH) &&
                    (blankCoordinate[1] + TRANSLATION_ARR[SIDE_LENGTH - i] > -1 &&
                            blankCoordinate[1] + TRANSLATION_ARR[SIDE_LENGTH - i] < SIDE_LENGTH)) {

                var newBlankCoordinate = boardCoordinatesToArrayCoordinate(
                        blankCoordinate[0] + TRANSLATION_ARR[i],
                        blankCoordinate[1] + TRANSLATION_ARR[SIDE_LENGTH - i]);

                var newBoard = new EightPuzzle(this.currentState, newBlankCoordinate);

                newBoard.currentState[emptyIndex] = newBoard.currentState[newBlankCoordinate];
                newBoard.currentState[newBlankCoordinate] = 0;

                neigbourBoards.add(newBoard);
            }
        }
        return neigbourBoards;
    }

    public boolean isSolvable() {
        int inversions = 0;
        for (int i = 0; i < currentState.length; i++) {
            if (currentState[i] == 0) continue;
            for (int j = i + 1; j < currentState.length; j++) {
                if (currentState[j] == 0) continue;
                if (currentState[j] > currentState[i]) {
                    inversions++;
                }
            }
        }
        return inversions % 2 != 1;
    }

    public boolean isGoalState() {
        for (int i = 0; i < currentState.length; i++) {
            if (currentState[i] != i) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EightPuzzle that = (EightPuzzle) o;
        return Arrays.equals(currentState, that.currentState);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(currentState);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < currentState.length; i++) {
            s.append(String.format("%2d ", currentState[i]));
            if ((i + 1) % SIDE_LENGTH == 0) {
                s.append("\n");
            }
        }
        s.append("\n");
        return s.toString();
    }

}