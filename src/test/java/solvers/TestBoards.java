package solvers;

import java.io.File;

public enum TestBoards {
    PUZZLE0("puzzle3x3-00.txt"),
    PUZZLE01("puzzle3x3-01.txt"),
    PUZZLE02("puzzle3x3-02.txt"),
    PUZZLE03("puzzle3x3-03.txt"),
    PUZZLE04("puzzle3x3-04.txt"),
    PUZZLE05("puzzle3x3-05.txt"),
    PUZZLE06("puzzle3x3-06.txt"),
    PUZZLE07("puzzle3x3-07.txt"),
    PUZZLE08("puzzle3x3-08.txt"),
    PUZZLE09("puzzle3x3-09.txt"),
    PUZZLE10("puzzle3x3-10.txt"),
    PUZZLE11("puzzle3x3-11.txt"),
    PUZZLE12("puzzle3x3-12.txt"),
    PUZZLE13("puzzle3x3-13.txt"),
    PUZZLE14("puzzle3x3-14.txt"),
    PUZZLE15("puzzle3x3-15.txt"),
    PUZZLE16("puzzle3x3-16.txt"),
    PUZZLE17("puzzle3x3-17.txt"),
    PUZZLE18("puzzle3x3-18.txt"),
    PUZZLE19("puzzle3x3-19.txt"),
    PUZZL20("puzzle3x3-20.txt"),
    PUZZLE21("puzzle3x3-21.txt"),
    PUZZLE22("puzzle3x3-22.txt"),
    PUZZLE23("puzzle3x3-23.txt"),
    PUZZLE24("puzzle3x3-24.txt"),
    PUZZLE25("puzzle3x3-25.txt"),
    PUZZLE26("puzzle3x3-26.txt"),
    PUZZLE27("puzzle3x3-27.txt"),
    PUZZLE28("puzzle3x3-28.txt"),
    PUZZLE29("puzzle3x3-29.txt"),
    PUZZLE30("puzzle3x3-30.txt"),
    PUZZLE31("puzzle3x3-31.txt"),
    PUZZLE32("puzzle3x3-sample.txt"),
    PUZZLE33("puzzle3x3-unsolvable.txt"),
    PUZZLE34("puzzle3x3-unsolvable1.txt"),
    PUZZLE35("puzzle3x3-unsolvable2.txt");

    private static final String BOARD_DIR = "/boards/";
    private static final String SOlN_DIR = "/sln/optimal/manhattan/";
    private final String fname;
    private final File brd_file;
    private final File sln_file;

    TestBoards(String board){
        fname = board;
        brd_file = new File(this.getClass().getResource(BOARD_DIR.concat(board)).getFile());
        sln_file = new File(this.getClass().getResource(SOlN_DIR.concat(board)).getFile());
    }

    public String getFilename(){
        return fname;
    }
    public File getBoardFile() {
        return brd_file;
    }

    public File getSolutionFile() {
        return sln_file;
    }
}
