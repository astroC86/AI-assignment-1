package solvers;

import model.EightPuzzle;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

public class DFS_SolverTest {
    @Test
    public void DFS_CorrectnessTest(){
        for (var brdprblm: TestBoards.values()) {
            try {
                BoardProblem bp = new BoardProblem(
                       brdprblm.getBoardFile(),
                        brdprblm.getSolutionFile()
                );
                DFS_Solver solver = new DFS_Solver(new EightPuzzle(bp.getInitialState()));
                //Since we know that DFS is not optimal
                if(solver.moves() < bp.getMoves()){
                    fail("failed for "+ brdprblm.getFilename());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
