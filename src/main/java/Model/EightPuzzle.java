package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EightPuzzle {

    private static final int N                 = 3;
    // A blank tile can move either up (1), down (-1), left (-1), right (1)
    private static final int[] TRANSLATION_ARR = {-1,1,0,0};

    private int[] currentState;
    private int emptyIndex;

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
            case UP    -> emptyIndex - N;
            case DOWN  -> emptyIndex + N;
            case LEFT  -> emptyIndex - 1;
            case RIGHT -> emptyIndex + 1;
            default    -> throw new IllegalArgumentException("Model.Action can only be UP, DOWN, LEFT, or RIGHT.");
        };

        var newState = currentState.clone();
        newState[emptyIndex] = newState[newEmptyIndex];
        newState[newEmptyIndex] = 0;
        return new EightPuzzle(newState);
    }

    private int[] arrayCoordinateToBoardCoordinates(int x) {
        // where x is the array index of the blank tile
        int r = x/N;
        int c = x - r*N;
        return new int[]{r, c};
    }

    private int boardCoordinatesToArrayCoordinate(int r, int c) {
        return c +r*N;
    }

    public Iterable<EightPuzzle> getNeighbours() {
        int[] blankCoordinate = arrayCoordinateToBoardCoordinates(emptyIndex);
        List<EightPuzzle> neigbourBoards = new ArrayList < > ();
        for (int i = 0; i < 4; i++) {
            if (( blankCoordinate[0] + TRANSLATION_ARR[i] > -1 &&
                    blankCoordinate[0] + + TRANSLATION_ARR[i] < N ) &&
                    ( blankCoordinate[1] + TRANSLATION_ARR[N - i ] > -1 &&
                            blankCoordinate[1] + TRANSLATION_ARR[N - i] < N ) ) {

                var newBlankCoordinate = boardCoordinatesToArrayCoordinate(
                        blankCoordinate[0] + TRANSLATION_ARR[i],
                        blankCoordinate[1] + TRANSLATION_ARR[N - i]);

                var newBoard = new EightPuzzle(this.currentState);
                swapTiles(newBoard,emptyIndex, newBlankCoordinate);

                neigbourBoards.add(newBoard);
            }
        }
        return neigbourBoards;
    }

    private void swapTiles(EightPuzzle board, int index1, int index2) {
        if(index1 == index2) return;

        if(board.emptyIndex == index1){
            board.emptyIndex = index2;
        } else if (board.emptyIndex == index2){
            board.emptyIndex = index1;
        }

        int temp                   = board.currentState[index1];
        board.currentState[index1] = board.currentState[index2];
        board.currentState[index2] = temp;
    }

    public boolean isSolvable(){
        int inversions = 0;
        for(int i = 0; i < currentState.length; i++ ){
            if(currentState[i] == 0)  continue;
            for(int j = i+1; j < currentState.length; j++){
                if(currentState[j] == 0)  continue;
                if(currentState[j] > currentState[i]){
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

    private static void checkState(int[] initialState){
        if (initialState == null){
            throw new IllegalArgumentException("Initial Board State cannot be NULL.");
        }

        if (initialState.length != 9) {
            throw new IllegalStateException("Initial Board State must be size 9.");
        }
        final int MAX_TILES = N*N;
        boolean[] bitmap = new boolean[MAX_TILES + 1];
        for (int item : initialState) {
            if (!(bitmap[item] ^= true)) {
                throw new IllegalStateException("Initial Board State must not contain duplicate values.");
            }
        }
    }
    public int getDimension() { return N; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EightPuzzle that = (EightPuzzle) o;
        return emptyIndex == that.emptyIndex && Arrays.equals(currentState, that.currentState);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(emptyIndex);
        result = 31 * result + Arrays.hashCode(currentState);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < currentState.length; i++) {
            s.append(String.format("%2d ", currentState[i]));
            if((i+1)%N == 0){ s.append("\n");}
        }
        s.append("\n");
        return s.toString();
    }

}