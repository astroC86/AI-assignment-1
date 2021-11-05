package eightPuzzle;

import io.herrmann.generator.Generator;

public class EightPuzzleGenerator {
    public Generator<Integer[]> BoardGenerator;
    public EightPuzzleGenerator(){
        BoardGenerator =  new Generator<Integer[]>() {
            @Override
            public void run() throws InterruptedException {
                Integer[] a = {1,2,3,4,5,6,7,8,0}; // 0 represents "gap"
                int[] stack = {0,0,0,0,0,0,0,0,0};
                boolean parity = true;
                int i = 0;
                int n = a.length;
                while (i < n) {
                    if (stack[i] < i) {
                        int j = i % 2 > 0 ? stack[i] : 0;
                        // SWAP a[i], a[j]
                        int temp = a[i];
                        a[i] = a[j];
                        a[j] = temp;
                        // Toggle parity when "gap" is not moved
                        // or it is moved with even taxicab distance
                        if (a[i] > 0 && a[j] > 0 || ((i^j)&1) == 0) parity = !parity;
//                        if (parity)
                        this.yield(a);
                        stack[i]++;
                        i = 0;
                    } else {
                        stack[i++] = 0;
                    }
                }
            }
        };
    }
}
