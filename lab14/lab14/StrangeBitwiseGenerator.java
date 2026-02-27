package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator {
    private int period;
    private int state;

    public StrangeBitwiseGenerator(int period) {
        state = 0;
        this.period = period;
    }

    @Override
    public double next() {
        state = state + 1;
        int weirdState = state & (state >>> 3) % period;
        weirdState = weirdState % period;
        return normalize(weirdState);
    }

    private double normalize(int state) {
        double k = (double) 2 / period;
        return k * state - 1;
    }
}
