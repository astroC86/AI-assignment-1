package Model;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class EightPuzzleTest {
    //5|2|8
    //4|1|7
    //0|3|6
    @Test
    public void InitialBoardStateIsSolvable(){
        EightPuzzle puzzle     = new EightPuzzle(new int[]{5,2,8,4,1,7,0,3,6});
        boolean     isSolvable = puzzle.isSolvable();
        assertTrue(isSolvable);
    }

    //1|2|3
    //4|5|6
    //0|8|7
    @Test
    public void InitialBoardStateIsNotSolvable(){
        EightPuzzle puzzle     = new EightPuzzle(new int[]{1,2,3,4,5,6,0,8,7});
        boolean     isSolvable = puzzle.isSolvable();
        assertFalse(isSolvable);
    }

    //5|2|8     5|2|8   5|2|8
    //4|1|7 ->  0|1|7 , 4|1|7
    //0|3|6     4|3|6   3|0|6
    @Test
    public void GetNeighboursReturnsAppropriateNeighboursForLeftCorner(){
        EightPuzzle puzzle      = new EightPuzzle(new int[]{5,2,8,4,1,7,0,3,6});

        Set<EightPuzzle> actual = new HashSet<>();
        actual.add(new EightPuzzle(new int[]{5,2,8,4,1,7,3,0,6}));
        actual.add(new EightPuzzle(new int[]{5,2,8,0,1,7,4,3,6}));

        for (var nb:
             puzzle.getNeighbours()) {
            if (!actual.contains(nb)) {
                fail("State:\n" +nb.toString() + " is not present in set.");
            }
        }
    }

    @Test
    public void throwsIllegalStateExceptionDueToDuplicates(){
        IllegalStateException exception = Assertions.assertThrows(
                IllegalStateException.class, ()->{
                    new EightPuzzle(new int[]{5,2,8,4,1,7,0,3,8});
                });
    }
}
