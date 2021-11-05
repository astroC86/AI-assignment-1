package eightPuzzle;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 20, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@Fork(value = 1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class EightPuzzleConstructorBench {
    @Benchmark
    public void constructorWithCheckWithCopy(){
        Iterator<Integer[]> boardStates = new EightPuzzleGenerator().BoardGenerator.iterator();
        for (int i=0; i < 400000;i++){
            var temp = new MockEightPuzzle();
            temp.MockEightPuzzleWithCheck(new int[]{5,2,8,4,1,7,0,3,6});
        }
    }

    @Benchmark
    public void constructorWithoutCheckWithCopy(){
        Iterator<Integer[]> boardStates = new EightPuzzleGenerator().BoardGenerator.iterator();
        for (int i=0; i < 400000 ; i++){
            var temp = new MockEightPuzzle();
            temp.MockEightPuzzleWithOutCheck(new int[]{5,2,8,4,1,7,0,3,6});
        }
    }

    @Benchmark
    public void constructorWithoutCheckWithoutCopy(){
        Iterator<Integer[]> boardStates = new EightPuzzleGenerator().BoardGenerator.iterator();
        for (int i=0; i < 400000 ; i++){
            var temp = new MockEightPuzzle();
            temp.MockEightPuzzleNoCopyNoCheck(new int[]{5,2,8,4,1,7,0,3,6});
        }
    }

    @Benchmark
    public void constructorWithCheckWithoutCopy(){
        Iterator<Integer[]> boardStates = new EightPuzzleGenerator().BoardGenerator.iterator();
        for (int i=0; i < 400000 ; i++){
            var temp = new MockEightPuzzle();
            temp.MockEightPuzzleNoCopyWithCheck(new int[]{5,2,8,4,1,7,0,3,6});
        }
    }


    private class MockEightPuzzle{
        private static int SIDE_LENGTH =3;
        private int[] currentState;
        private int emptyIndex;

        public void MockEightPuzzleWithCheck(int[] initialState) {
            checkState(initialState);
            this.currentState = Arrays.copyOf(initialState, initialState.length);
            emptyIndex = getEmptyIndex();
        }

        public void MockEightPuzzleWithOutCheck(int[] initialState) {
            this.currentState = Arrays.copyOf(initialState, initialState.length);
            emptyIndex = getEmptyIndex();
        }

        public void MockEightPuzzleNoCopyNoCheck(int[] initialState) {
            this.currentState = initialState;
            emptyIndex = getEmptyIndex();
        }

        public void MockEightPuzzleNoCopyWithCheck(int[] initialState) {
            checkState(initialState);
            this.currentState = initialState;
            emptyIndex = getEmptyIndex();
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
            for (int i : initialState) {
                if (!(bitmap[i] ^= true)) {
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

}
