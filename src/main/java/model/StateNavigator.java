package model;

public class StateNavigator {
    private int current = 0;
    private final EightPuzzle[] states;

    public StateNavigator(EightPuzzle[] states) {
        this.states = states;
    }

    public EightPuzzle getCurrent() {
        return states[current];
    }

    public void goNext() {
        if (current + 1 < states.length) {
            current++;
        }
    }

    public void goLast() {
        current = states.length - 1;
    }

    public void goFirst() {
        current = 0;
    }

    public void goPrevious() {
        if (current > 0) {
            current--;
        }
    }

    public int countStates() {
        return states.length;
    }

    public int getCurrentIndex() {
        return current;
    }
}
