import java.util.ArrayList;

public class EightPuzzle {
    private int[] currentState;

    // |0|1|2|
    // |3|4|5|
    // |6|7|8|
    private static final int TOP_LEFT_INDEX = 0;
    private static final int TOP_MIDDLE_INDEX = 1;
    private static final int TOP_RIGHT_INDEX = 2;
    private static final int MIDDLE_LEFT_INDEX = 3;
    private static final int CENTER_INDEX = 4;
    private static final int MIDDLE_RIGHT_INDEX = 5;
    private static final int BOTTOM_LEFT_INDEX = 6;
    private static final int BOTTOM_MIDDLE_INDEX = 7;
    private static final int BOTTOM_RIGHT_INDEX = 8;

    public EightPuzzle(int[] initialState) {
        // TODO: Is storing the state in a long (states need 4 * 9 bits = 36bits) better? Do it later and see how the improvement looks like.
        this.currentState = initialState;
    }

    public boolean isGoalState() {
        if (currentState.length != 9) {
            throw new java.lang.IllegalStateException();
        }

        for (int i = 0; i < currentState.length; i++) {
            if (currentState[i] != i) {
                return false;
            }
        }

        return true;
    }

    private EightPuzzle takeAction(Action action, int emptyIndex) {
        var newEmptyIndex = switch (action) {
            case UP -> emptyIndex - 3;
            case DOWN -> emptyIndex + 3;
            case LEFT -> emptyIndex - 1;
            case RIGHT -> emptyIndex + 1;
            default -> throw new IllegalArgumentException();
        };

        var newState = currentState.clone();
        newState[emptyIndex] = newState[newEmptyIndex];
        newState[newEmptyIndex] = 0;
        return new EightPuzzle(newState);
    }

    private int getEmptyIndex() {
        for (int i = 0; i < currentState.length; i++) {
            if (currentState[i] == 0) {
                return i;
            }
        }

        throw new IllegalStateException();
    }

    public EightPuzzle[] getNeighbours() {
        var emptyIndex = getEmptyIndex();

        if (emptyIndex == TOP_LEFT_INDEX) {
            return new EightPuzzle[]{
                    takeAction(Action.DOWN, emptyIndex),
                    takeAction(Action.RIGHT, emptyIndex)
            };
        } else if (emptyIndex == TOP_MIDDLE_INDEX) {
            return new EightPuzzle[]{
                    takeAction(Action.DOWN, emptyIndex),
                    takeAction(Action.RIGHT, emptyIndex),
                    takeAction(Action.LEFT, emptyIndex)
            };
        } else if (emptyIndex == TOP_RIGHT_INDEX) {
            return new EightPuzzle[]{
                    takeAction(Action.DOWN, emptyIndex),
                    takeAction(Action.LEFT, emptyIndex)
            };
        } else if (emptyIndex == MIDDLE_LEFT_INDEX) {
            return new EightPuzzle[]{
                    takeAction(Action.UP, emptyIndex),
                    takeAction(Action.DOWN, emptyIndex),
                    takeAction(Action.RIGHT, emptyIndex)
            };
        } else if (emptyIndex == CENTER_INDEX) {
            return new EightPuzzle[]{
                    takeAction(Action.UP, emptyIndex),
                    takeAction(Action.DOWN, emptyIndex),
                    takeAction(Action.LEFT, emptyIndex),
                    takeAction(Action.RIGHT, emptyIndex)
            };
        } else if (emptyIndex == MIDDLE_RIGHT_INDEX) {
            return new EightPuzzle[]{
                    takeAction(Action.UP, emptyIndex),
                    takeAction(Action.DOWN, emptyIndex),
                    takeAction(Action.LEFT, emptyIndex)
            };
        } else if (emptyIndex == BOTTOM_LEFT_INDEX) {
            return new EightPuzzle[]{
                    takeAction(Action.UP, emptyIndex),
                    takeAction(Action.RIGHT, emptyIndex)
            };
        } else if (emptyIndex == BOTTOM_MIDDLE_INDEX) {
            return new EightPuzzle[]{
                    takeAction(Action.UP, emptyIndex),
                    takeAction(Action.RIGHT, emptyIndex),
                    takeAction(Action.LEFT, emptyIndex)
            };
        } else if (emptyIndex == BOTTOM_RIGHT_INDEX) {
            return new EightPuzzle[]{
                    takeAction(Action.UP, emptyIndex),
                    takeAction(Action.LEFT, emptyIndex)
            };
        }

        throw new IllegalStateException();
    }
}