package ui;

public enum Solvers {

    DFS("DFS"),
    BFS("BFS"),
    AStarManhattan("A* (Manhattan)"),
    AStarEuclidean("A* (Euclidean)");

    private String name ;

    Solvers(String s) {
        this.name = s;
    }

    @Override
    public String toString() {
        return name;
    }
}
