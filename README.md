# 모의 담금질 기법

높은 온도에서 액체 상태인 물질이 온도가 점차 낮아지면서 결정체로 변하는 과정을 모방한 해 탐색 알고리즘이다. 



## 알고리즘

1. 임의의 후보해 x0를 선택한다.

2. 초기 t를 정한다.

3. repeat

4. for i = 1 to kt {  // kt는 t에서의 for-루프 반복 횟수이다.

5. x0의 이웃해 중에서 랜덤하게 하나의 해 x1를 선택한다.

6. d = (x0의 값) - (x1의 값)

7. if (d < 0)    // 이웃해인 x1이 더 우수한 경우

8. x0 ← x1

9. else       // x1이 x0보다 우수하지 않은 경우

10. q ← (0,1) 사이에서 랜덤하게 선택한 수

11. if ( q < p ) x0 ← x1  // p는 자유롭게 탐색할 확률이다.

​     }

12. T ← aT // 1보다 작은 상수 a 를 T에 곱하여 새로운 T를 계산

13. until (종료 조건이 만족될 때까지)

14. return s



## 3~4차함수의 전역 최적점을 찾을 수 있는 모의담금질 기법

### 코드

```
package company;

public class Main {
    public static void main(String[] args) {
        SimulatedAnnealing sa = new SimulatedAnnealing(100);
        Problem p = new Problem() {
            @Override
            public double fit(double x) {
                return ((x-1)*(x-2)*(x-3)*(x-6)/6);
            }

            @Override
            public boolean isNeighborBetter(double f0, double f1) {
                return f0 > f1;
            }
        };
        double x = sa.solve(p, 100, 0.99, 0, 10);
        System.out.println(x);
        System.out.println(p.fit(x));
        System.out.println(sa.hist);
    }
}
```

```
package company;

public interface Problem {
    double fit(double x);
    boolean isNeighborBetter(double f0, double f1);
}
```

```
package company;

import java.util.*;

public class SimulatedAnnealing {
    private int niter;
    public ArrayList<Double> hist;

    public SimulatedAnnealing(int niter) {
        this.niter = niter;
        hist = new ArrayList<>();
    }

    public double solve(Problem p, double t, double a, double lower, double upper) {
        Random r = new Random();
        double x0 = r.nextDouble() * (upper - lower) + lower;
        return solve(p, t, a, x0, lower, upper);
    }

    public double solve(Problem p, double t, double a, double x0, double lower, double upper) {
        Random r = new Random();
        double f0 = p.fit(x0);
        hist.add(f0);

        for (int i=0; i<niter; i++) {
            int kt = (int) t;
            for(int j=0; j<kt; j++) {
                double x1 = r.nextDouble() * (upper - lower) + lower;
                double f1 = p.fit(x1);

                if(p.isNeighborBetter(f0, f1)) {
                    x0 = x1;
                    f0 = f1;
                    hist.add(f0);
                } else {
                    double d = Math.sqrt(Math.abs(f1 - f0));
                    double p0 = 0.00001;
                    if(r.nextDouble() < p0) {
                        x0 = x1;
                        f0 = f1;
                        hist.add(f0);
                    }
                }
            }
            t *= a;
        }
        return x0;
    }
}
```

반복 횟수 : 100

초기온도 t : 100

냉각률 a : 0.99

x의 구간 :  0 ~ 10

임의의 후보해  : 랜덤한 값

이웃해 : 랜덤한 값

for루프 반복 횟수(kt) : t

자유롭게 탐색할 확률(p) : 0.00001

로 설정하여 4차함수의 최솟값을 찾아보았다.



### 결과 

(x-1)(x-2)(x-3)(x-6)/6 은 x=5 일 때 -4의 값을 가지고 이 값이 이 함수의 최솟값이다.

![1](https://user-images.githubusercontent.com/80511975/121685536-01290680-cafb-11eb-9aad-cfb1cab0bd7d.PNG)

모의 담금질 기법을 이용하여 함수의 전역 최적해를 찾은 결과 

x = 5.057155492779034 일 때 최솟값 -4.00954530934297 이라는 결과가 나왔다.

아래의 그래프는 코드가 실행되면서 후보해로 뽑힌 x값들에 대한 값이다.

![2](https://user-images.githubusercontent.com/80511975/121686508-3e41c880-cafc-11eb-958d-8df60f561f43.PNG)



3차 함수에 대해서도 확인해보기 위해 위의 코드를 가지고 함수를 (x-3)^2(x-7)로 바꾸고 구간을 2~8로 바꾼 다음 실행해보았다.

 (x-3)^2(x-7)은 x=5.667 일 때 -9.481이라는 값이 나오고 이 값이 구간 2~8 에서의 최솟값이다.

![3](https://user-images.githubusercontent.com/80511975/121688039-fa4fc300-cafd-11eb-80ac-55a09a88c69e.PNG)

모의 담금질 기법을 이용하여 함수의 전역 최적해를 찾은 결과

x=5.666385628261109 일 떄 최솟값 -9.481481165573337 이라는 결과가 나왔다.

아래의 그래프는 코드가 실행되면서 후보해로 뽑힌 x값들에 대한 값이다.

![4](https://user-images.githubusercontent.com/80511975/121688254-32ef9c80-cafe-11eb-91e1-1ee1619718d1.PNG)



## 모델링

2차 함수 x^2-4x+4로 만들기 위한 aX^2+bx+c에서의 a, b, c의 값을 모의 담금질 기법을 이용하여 구해보았다.



### 코드

```
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
```

```
package company;

public interface Problem {
    double fit( double a, double b, double c);
    boolean isNeighborBetter(double f0, double f1);
}
```

```
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
```

첫번째 코드에서 x의 값을 모의 담금질 기법을 이용하여 구한 것처럼 a, b, c의 값을 모의 담금질을 이용하여 구하였다. 

x^2-4x+4에 최대한 근접한 값을 구하기 위하여 fit 부분을 아래와 같이 수정하였고 나머지 코드는 x의 값이 아닌 a, b, c의 값을 구하는 것으로 조금 변형하였으며  첫번째 코드와 거의 같다.

```
public double fit(double a, double b, double c) {
                double sum=0;
                for(int x=0; x<5; x++) {
                    double y1 = a * x * x + b * x + c;
                    double y0 = x * x - 4 * x + 4;
                    sum += Math.pow(y0-y1, 2);
                }
                return sum;
            }
```

더 우수한 값을 찾기 위하여 x의 값으로 0~4를 대입하여 얻은 y0과 y1의 편차의 제곱을 합하여 이 값이 가장 작은 값을 구하였다.



### 결과

![5](https://user-images.githubusercontent.com/80511975/121701721-27a36d80-cb0c-11eb-857f-efebbe5c718f.PNG)

더 우수한 값이 나올때마다 출력해주었다. (왼쪽에서부터 a, b, c)

마지막 줄을 보면 a=1.1556704026663898 , b=-4.6325616805189815, c=4.581734404209881으로 구하고 싶은 값인 1,-4, 4에 근접한 값을 구하였다.

![6](https://user-images.githubusercontent.com/80511975/121703440-c9778a00-cb0d-11eb-8ac7-d7d66dd2d91b.PNG)

더 우수한 값이 나올때의 a, b, c의 값이다. 그래프를 보면 a는 1에, b는 -4에 c는 4에 가까워지고 있다는 것을 알 수 있다.

(main에 올린 코드는 리드미에 있는 코드 중 2번째(모델링)코드입니다.)

