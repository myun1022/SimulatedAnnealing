package company;

public class Main {
    public static void main(String[] args) {
        SimulatedAnnealing sa = new SimulatedAnnealing(100);
        Problem p = new Problem() {
            @Override
            public double fit(double a, double b, double c) {
                double sum=0;
                for(int x=0; x<5; x++) {
                    double y1 = a * x * x + b * x + c;
                    double y0 = x * x - 4 * x + 4;
                    sum += Math.pow(y0-y1, 2);
                }
                return sum;
            }

            @Override
            public boolean isNeighborBetter(double f0, double f1) {
                return f0 > f1;
            }
        };
        double x = sa.solve(p, 100, 0.99, -5,5);
    }
}
