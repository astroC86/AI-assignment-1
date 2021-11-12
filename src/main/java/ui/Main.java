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
import javafx.scene.control.Label;
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

import java.util.Objects;

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
        }else{
            long startTime = System.currentTimeMillis();
            switch (alg) {
                case DFS -> {
                    solveDFS();
                }
                case BFS -> {
                    solveBFS();
                }
                case AStarEuclidean -> {
                    solveAStarEuclidean();
                }
                case AStarManhattan -> {
                    solveAStarManhattan();
                }
                default -> new Alert(Alert.AlertType.WARNING, "Invalid Algorithm").showAndWait();
            }
            long stopTime = System.currentTimeMillis();
            updateRunningTime(stopTime - startTime);

        }

    }

    private void updateSearchDepth(int i) {
        srchDpthLbl.setText("");
        srchDpthLbl.setText(String.valueOf(i));
    }

    private void updateExpandedNodes(int i) {
        expndedNodesLbl.setText("");
        expndedNodesLbl.setText(String.valueOf(i));
    }

    private void updateRunningTime(long time){
        runningTimeLbl.setText("");
        runningTimeLbl.setText(time +" ms");
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

    public void solveDFS() {
        if (eightPuzzle == null) {
            new Alert(Alert.AlertType.WARNING, "Load an initial state").showAndWait();
            return;
        }

        var solver = new DFS_Solver(eightPuzzle);
        navigator = new StateNavigator(solver.solution());
        showCurrent();
        updateExpandedNodes(9);
        updateSearchDepth(9);
    }

    public void solveBFS() {
        if (eightPuzzle == null) {
            new Alert(Alert.AlertType.WARNING, "Load an initial state").showAndWait();
            return;
        }

        var solver = new BFS_Solver(eightPuzzle);
        var sln = solver.solution();
        navigator = new StateNavigator(sln);
        showCurrent();
        updateExpandedNodes(9);
        updateSearchDepth(sln.length - 1);
    }

    private void solveAStar(Heuristic heuristic) {
        if (eightPuzzle == null) {
            new Alert(Alert.AlertType.WARNING, "Load an initial state").showAndWait();
            return;
        }

        try {
            var states = AStarSolver.solve(eightPuzzle, heuristic);
            navigator = new StateNavigator(states);
            showCurrent();
            updateExpandedNodes(9);
            updateSearchDepth(states.length -1);
        }
        catch (UnresolvableBoardException e) {
            new Alert(Alert.AlertType.WARNING, e.getMessage()).showAndWait();
        }
    }

    public void solveAStarManhattan() {
        solveAStar(new ManhattanHeuristic());
    }

    public void solveAStarEuclidean() {
        solveAStar(new EuclideanHeuristic());
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
