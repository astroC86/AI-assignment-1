package solvers;

import model.EightPuzzle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BoardProblem {

    private final int moves;
    private final List<Integer> initialState;
    private final List<EightPuzzle> solutionSteps;
    private boolean solvable = true;

    public BoardProblem(File board, File boardSln) throws IOException {
        BufferedReader initialStateReader = new BufferedReader(new FileReader(board));
        String[] integersInString = initialStateReader.readLine()
                                                      .trim()
                                                      .split(" ");
        initialState = new ArrayList<>();

        for (String value : integersInString) {
            if (value.equals("")) continue;
            initialState.add(Integer.parseInt(value));
        }

        BufferedReader solutionReader  = new BufferedReader(new FileReader(boardSln));

        String line;
        solutionSteps = new ArrayList<>();

        while( (line = solutionReader.readLine()) != null) {
            line = line.trim();
            if(line.contains("-1")){
                solvable = false;
                break;
            }
            integersInString = line.split(" ");
            var temp = new ArrayList<Integer>();
            for (String s : integersInString) {
                if (s.equals("")) continue;
                temp.add(Integer.parseInt(s));
            }

            solutionSteps.add(new EightPuzzle(temp.stream().mapToInt(Integer::intValue).toArray()));
        }
        this.moves = solutionSteps.size() - 1;
    }

    public boolean isSolvable(){
        return solvable;
    }

    public int getMoves() {
        return moves;
    }

    public int[] getInitialState() {
        return initialState.stream().mapToInt(Integer::intValue).toArray();
    }

    public List<EightPuzzle> getSolutionSteps() {
        return solutionSteps;
    }
}
