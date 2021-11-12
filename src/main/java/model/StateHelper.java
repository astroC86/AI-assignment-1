package model;

public class StateHelper {
    // xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx
    //                                      8    7    6    5    4    3    2    1    0
    private StateHelper() {}

    public static long fromArray(int[] state) {
        long result = 0;
        for (int i = 8; i >= 0; i--) {
            result = result << 4;
            result += state[i];
        }

        return result;
    }

    public static int getAtIndex(int index, long state) {
        return (int)((state >> (index * 4)) & 0b1111);
    }

    public static long setAtIndex(int index, int newValue, long state) {
        // Set the bits to zero first.
        state &= (~(0b1111 << (index * 4)));
        state |= (newValue << (index * 4));
        return state;
    }
}
