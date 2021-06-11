package company;

public interface Problem {
    double fit( double a, double b, double c);
    boolean isNeighborBetter(double f0, double f1);
}
