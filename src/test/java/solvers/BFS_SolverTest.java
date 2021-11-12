package solvers;

import model.EightPuzzle;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.fail;

public class BFS_SolverTest {
    @Test
    public void BFS_CorrectnessTest(){
        for (var brdprblm: TestBoards.values()) {
            try {
                BoardProblem bp = new BoardProblem(
                        brdprblm.getBoardFile(),
                        brdprblm.getSolutionFile()
                );
                BFS_Solver solver = new BFS_Solver(new EightPuzzle(bp.getInitialState()));
                //Since we know that DFS is not optimal
                if(solver.moves() < bp.getMoves()){
                    fail("failed for "+ brdprblm.getFilename());
                }
            } catch (IOException | UnresolvableBoardException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void BFS_OptimalityTestAgainst_DFS(){
        for (var brdprblm: TestBoards.values()) {
            try {
                BoardProblem bp = new BoardProblem(
                        brdprblm.getBoardFile(),
                        brdprblm.getSolutionFile()
                );
                BFS_Solver solverBFS = new BFS_Solver(new EightPuzzle(bp.getInitialState()));
                DFS_Solver solverDFS = new DFS_Solver(new EightPuzzle(bp.getInitialState()));
                //Since we know that DFS is not optimal
                if(solverDFS.moves() < solverBFS.moves()){
                    fail("failed for "+ brdprblm.getFilename());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }catch(UnresolvableBoardException e){
            }
        }
    }

    @Test
    public void BFS_OptimalityTest(){
        for (var brdprblm: TestBoards.values()) {
            try {
                BoardProblem bp = new BoardProblem(
                        brdprblm.getBoardFile(),
                        brdprblm.getSolutionFile()
                );
                BFS_Solver solverBFS = new BFS_Solver(new EightPuzzle(bp.getInitialState()));
                //Since we know that DFS is not optimal
                if(solverBFS.moves() != bp.getMoves() ){
                    fail("failed for "+ brdprblm.getFilename());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }catch(UnresolvableBoardException e){}
        }
    }

    @Test
    public void MovesOptimalityTest(){
        for (var brdprblm: TestBoards.values()) {
            try {
                BoardProblem bp = new BoardProblem(
                        brdprblm.getBoardFile(),
                        brdprblm.getSolutionFile()
                );

                if(bp.isSolvable()) {
                    BFS_Solver solverBFS = new BFS_Solver(new EightPuzzle(bp.getInitialState()));
                    //Since we know that DFS is not optimal
                    Iterator<EightPuzzle> bfsIterator = Arrays.stream(solverBFS.solution()).iterator();
                    Iterator<EightPuzzle> bpIterator = bp.getSolutionSteps().stream().iterator();
                    Stack<EightPuzzle> bpStack = new Stack<>();

                    while (bpIterator.hasNext())
                        bpStack.push(bpIterator.next());

                    while (bpIterator.hasNext()) {
                        var bfsStep = bfsIterator.next();
                        var optStep = bpStack.pop();
                        if (!bfsStep.equals(optStep)) {
                            fail("failed for "             +
                                    brdprblm.getFilename() +
                                    "\nexpected : \n"      +
                                    optStep.toString()     +
                                    "\n actual : \n"       +
                                    bfsStep.toString());
                            break;
                        }
                    }
                }
            } catch (IOException | UnresolvableBoardException e) {
                e.printStackTrace();
            }
        }
    }
}
