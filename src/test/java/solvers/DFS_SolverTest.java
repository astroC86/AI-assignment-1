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

    @Test
    public void DFS_DepthTest(){
        for (var brdprblm: TestBoards.values()) {
            try {
                BoardProblem bp = new BoardProblem(
                        brdprblm.getBoardFile(),
                        brdprblm.getSolutionFile()
                );
                if(bp.isSolvable()){
                    DFS_Solver solver = new DFS_Solver(new EightPuzzle(bp.getInitialState()));
                    int    nineFactorial = 362880;
                    double minMaxDepth   = Math.log(362880/2.0)/Math.log(2);
                    double maxMaxDepth   = Math.log(362880/2.0)/Math.log(4);

                    if (solver.getSearchDepth() < minMaxDepth &&
                            solver.getSearchDepth() > maxMaxDepth) {
                        fail("Search depth is not within theoretical depth");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
