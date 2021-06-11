package company;

import java.util.*;

public class SimulatedAnnealing {
    private int niter;

    public SimulatedAnnealing(int niter) {
        this.niter = niter;
    }


    public double solve(Problem p, double t, double a, double lower, double upper) {
        Random r = new Random();
        double a0 = r.nextDouble() * (upper - lower) + lower;
        double b0 = r.nextDouble() * (upper - lower) + lower;
        double c0 = r.nextDouble() * (upper - lower) + lower;
        double f0 = p.fit(a0,b0,c0);
        System.out.println(a0+"  "+b0+"  "+c0);

        for (int i=0; i<niter; i++) {
            int kt = (int) t;
            for(int j=0; j<kt; j++) {
                double a1 = r.nextDouble() * (upper - lower) + lower;
                double b1 = r.nextDouble() * (upper - lower) + lower;
                double c1 = r.nextDouble() * (upper - lower) + lower;
                double f1 = p.fit(a1,b1,c1);

                if(p.isNeighborBetter(f0, f1)) {
                    a0=a1;
                    b0=b1;
                    c0=c1;
                    f0 = f1;
                    System.out.println(a0+"  "+b0+"  "+c0);
                } else {
                    double d = Math.sqrt(Math.abs(f1 - f0));
                    double p0 = 0.00001;
                    if(r.nextDouble() < p0) {
                        a0=a1;
                        b0=b1;
                        c0=c1;
                        f0 = f1;
                        System.out.println(a0+"  "+b0+"  "+c0);
                    }
                }
            }
            t *= a;
        }
        return 0;
    }
}
