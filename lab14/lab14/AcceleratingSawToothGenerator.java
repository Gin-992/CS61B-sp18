package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private int period;
    private int state;
    private double fact;

    public AcceleratingSawToothGenerator(int period, double fact) {
        this.state = 0;
        this.period = period;
        this.fact = fact;
    }

    @Override
    public double next() {
        double cur = normalize(state);
        state = (state + 1) % period;
        if (state == 0) {
            period = (int) (period * fact);
        }
        return cur;
    }

    private double normalize(int state) {
        double k = (double) 2 / period;
        return k * state - 1;
    }
}
