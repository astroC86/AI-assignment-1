package solvers;

public class UnresolvableBoardException extends Throwable {
    public UnresolvableBoardException() {
        super("The given board is unsolvable.");
    }
}
