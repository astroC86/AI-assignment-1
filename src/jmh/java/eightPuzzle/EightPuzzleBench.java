package eightPuzzle;

import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 200, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@Fork(value = 1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class EightPuzzleBench {


    private static int SIDE_LENGTH;
    private int[] currentState;
    Iterator<Integer[]> boardStates;
    int[] initialState;

    @Setup(Level.Invocation)
    public void setUp() {
        SIDE_LENGTH = 3;
        boardStates = EightPuzzleGenerator.BoardGenerator.iterator();
        initialState = Arrays.stream(boardStates.next()).mapToInt(Integer::intValue).toArray();
    }

    @Benchmark
    public void constructorWithCheck() {
        checkState(initialState);
        this.currentState = Arrays.copyOf(initialState, initialState.length);
        var emptyIndex = getEmptyIndex();
    }

    @Benchmark
    public void constructorWithOutCheck() {
        this.currentState = Arrays.copyOf(initialState, initialState.length);
        var emptyIndex = getEmptyIndex();
    }

    private static void checkState(int[] initialState){
        if (initialState == null){
            throw new IllegalArgumentException("Initial Board State cannot be NULL.");
        }

        if (initialState.length != 9) {
            throw new IllegalStateException("Initial Board State must be size 9.");
        }

        final int MAX_TILES = SIDE_LENGTH * SIDE_LENGTH;
        boolean[] bitmap = new boolean[MAX_TILES];
        for (int ii  = 0 ;ii  <  initialState.length;ii++) {
            if (!(bitmap[initialState[ii]] ^= true)) {
                throw new IllegalStateException("Initial Board State must not contain duplicate values.");
            }
        }
    }

    private int getEmptyIndex() {
        for (int i = 0; i < currentState.length; i++) {
            if (currentState[i] == 0) {
                return i;
            }
        }
        throw new IllegalStateException("State doesn't contain a zero (blank tile).");
    }

}
