package ui;

public enum Solvers {

    AStarManhattan("A* (Manhattan)"),
    DFS("DFS"),
    BFS("BFS"),
    AStarEuclidean("A* (Euclidean)");

    private String name = "";

    Solvers(String s) {
        this.name = s;
    }

    @Override
    public String toString() {
        return name;
    }
}
