package ui;

import com.jfoenix.controls.JFXComboBox;
import heuristics.EuclideanHeuristic;
import heuristics.Heuristic;
import heuristics.ManhattanHeuristic;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.EightPuzzle;
import model.StateNavigator;
import solvers.AStarSolver;
import solvers.BFS_Solver;
import solvers.DFS_Solver;
import solvers.UnresolvableBoardException;

// Hacky approach due to JavaFX runtime components are missing error:
// https://stackoverflow.com/questions/59771324/error-javafx-runtime-components-are-missing-and-are-required-to-run-this-appli
public class Main {
    @FXML
    private TextField inputState;
    @FXML
    private Text element0;
    @FXML
    private Text element1;
    @FXML
    private Text element2;
    @FXML
    private Text element3;
    @FXML
    private Text element4;
    @FXML
    private Text element5;
    @FXML
    private Text element6;
    @FXML
    private Text element7;
    @FXML
    private Text element8;

    @FXML
    private JFXComboBox<Solvers> solversCmboBox;


    @FXML
    private Text expndedNodesLbl;
    @FXML
    private Text srchDpthLbl;
    @FXML
    private Text runningTimeLbl;


    @FXML
    private Text ithOfTotal;
    private EightPuzzle eightPuzzle;
    private StateNavigator navigator;

    private String toStringHelper(int number) {
        if (number == 0) {
            return "";
        }

        return String.valueOf(number);
    }

    public void loadStateAndRun(MouseEvent mouseEvent) {
        var text = inputState.getText();
        if (text.length() != 9) {
            new Alert(Alert.AlertType.WARNING, "Invalid input length. Should have length of 9.").showAndWait();
            return;
        }

        var state = new int[9];
        for (int i = 0; i < 9; i++) {
            var number = text.charAt(i) - '0';
            if (number < 0 || number > 8) {
                new Alert(Alert.AlertType.WARNING, "Invalid input. Number should be between 0 and 8.").showAndWait();
                return;
            }

            state[i] = number;
        }

        try {
            eightPuzzle = new EightPuzzle(state);
            // Clear the previous navigator (if any), which can be of another state. Must be done before showState.
            navigator = null;
            showState(eightPuzzle);
        }
        catch (Exception e) {
            new Alert(Alert.AlertType.WARNING, e.getMessage()).showAndWait();
            return;
        }
        Solvers alg = solversCmboBox.getValue();
        if(alg == null){
            new Alert(Alert.AlertType.WARNING, "Choose an algorithm.").showAndWait();
            return;
        } else {
            switch (alg) {
                case DFS -> {
                    long runningTime = solveDFS();
                    runningTimeLbl.setText(runningTime + " ms");
                }
                case BFS -> {
                    long runningTime = solveBFS();
                    runningTimeLbl.setText(runningTime + " ms");
                }
                case AStarEuclidean -> {
                    long runningTime = solveAStarEuclidean();
                    runningTimeLbl.setText(runningTime + " ms");
                }
                case AStarManhattan -> {
                    long runningTime = solveAStarManhattan();
                    runningTimeLbl.setText(runningTime + " ms");
                }
                default -> {
                    new Alert(Alert.AlertType.WARNING, "Invalid Algorithm").showAndWait();
                    runningTimeLbl.setText("0 ms");
                    expndedNodesLbl.setText("0");
                    srchDpthLbl.setText("0");
                }
            }

        }

    }

    private void showState(EightPuzzle state) {
        element0.setText(toStringHelper(state.getNumberAtIndex(0)));
        element1.setText(toStringHelper(state.getNumberAtIndex(1)));
        element2.setText(toStringHelper(state.getNumberAtIndex(2)));
        element3.setText(toStringHelper(state.getNumberAtIndex(3)));
        element4.setText(toStringHelper(state.getNumberAtIndex(4)));
        element5.setText(toStringHelper(state.getNumberAtIndex(5)));
        element6.setText(toStringHelper(state.getNumberAtIndex(6)));
        element7.setText(toStringHelper(state.getNumberAtIndex(7)));
        element8.setText(toStringHelper(state.getNumberAtIndex(8)));
        if (navigator == null) {
            ithOfTotal.setText("");
        }
        else {
            ithOfTotal.setText(String.format("%d of %d", navigator.getCurrentIndex() + 1, navigator.countStates()));
        }
    }

    public long solveDFS() {
        long startTime = 0, endTime = 0 ;
        if (eightPuzzle == null) {
            new Alert(Alert.AlertType.WARNING, "Load an initial state").showAndWait();
            return 0;
        }
        try {
            startTime = System.currentTimeMillis();
            var solver = new DFS_Solver(eightPuzzle);
            var solution = solver.solution();
            endTime = System.currentTimeMillis();
            navigator = new StateNavigator(solution);
            showCurrent();
            expndedNodesLbl.setText(String.valueOf(solver.getNumberNodesExpanded()));
            srchDpthLbl.setText(String.valueOf(solver.getSearchDepth()));
        }  catch (UnresolvableBoardException e) {
            new Alert(Alert.AlertType.WARNING, e.getMessage()).showAndWait();
        }

        return endTime - startTime;
    }

    public long solveBFS() {
        long startTime = 0, endTime = 0 ;
        if (eightPuzzle == null) {
            new Alert(Alert.AlertType.WARNING, "Load an initial state").showAndWait();
            return 0;
        }

        try {
            startTime = System.currentTimeMillis();
            var solver = new BFS_Solver(eightPuzzle);
            var sln = solver.solution();
            endTime = System.currentTimeMillis();
            navigator = new StateNavigator(sln);
            showCurrent();
            expndedNodesLbl.setText(String.valueOf(solver.getNumberNodesExpanded()));
            srchDpthLbl.setText(String.valueOf(solver.getSearchDepth()));
        }  catch (UnresolvableBoardException e) {
            new Alert(Alert.AlertType.WARNING, e.getMessage()).showAndWait();
        }

        return endTime - startTime;
    }

    private long solveAStar(Heuristic heuristic) {
        long startTime = 0, endTime = 0 ;

        if (eightPuzzle == null) {
            new Alert(Alert.AlertType.WARNING, "Load an initial state").showAndWait();
            return 0 ;
        }

        try {
            startTime = System.currentTimeMillis();
            var solver = new AStarSolver();
            var states = solver.solve(eightPuzzle, heuristic);
            endTime = System.currentTimeMillis();
            navigator = new StateNavigator(states);
            showCurrent();
            expndedNodesLbl.setText(String.valueOf(solver.getNumberNodesExpanded()));
            srchDpthLbl.setText(String.valueOf(solver.getSearchDepth()));
        }
        catch (UnresolvableBoardException e) {
            new Alert(Alert.AlertType.WARNING, e.getMessage()).showAndWait();
        }

        return endTime - startTime;
    }

    public long solveAStarManhattan() {
        return solveAStar(new ManhattanHeuristic());
    }

    public long solveAStarEuclidean() {
        return solveAStar(new EuclideanHeuristic());
    }


    public void showCurrent() {
        if (navigator == null) {
            return;
        }

        showState(navigator.getCurrent());
    }

    public void goNext(MouseEvent mouseEvent) {
        if (navigator == null) {
            return;
        }

        navigator.goNext();
        showCurrent();
    }

    public void goPrevious(MouseEvent mouseEvent) {
        if (navigator == null) {
            return;
        }

        navigator.goPrevious();
        showCurrent();
    }

    public void goFirst(MouseEvent mouseEvent) {
        if (navigator == null) {
            return;
        }

        navigator.goFirst();
        showCurrent();
    }

    public void goLast(MouseEvent mouseEvent) {
        if (navigator == null) {
            return;
        }

        navigator.goLast();
        showCurrent();
    }

    @FXML
    void initialize() {
        solversCmboBox.setItems(FXCollections.observableArrayList(Solvers.values()));

    }

    public static class MainInner extends Application {

        @Override
        public void start(Stage primaryStage) throws Exception {
            Parent root = new FXMLLoader(Main.class.getResource("/main.fxml")).load();
            ((VBox)root).setPadding(new Insets(20));
            primaryStage.setTitle("Eight Puzzle (Assignment 1)");
            var scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

    }

    public static void main(String[] args) {
        Application.launch(MainInner.class);
    }

}
