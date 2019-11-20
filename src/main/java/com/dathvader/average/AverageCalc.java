package com.dathvader.average;

import java.util.ArrayList;
import java.util.List;

public class AverageCalc {

    private final List<Double> average = new ArrayList<>();
    private final String name;

    public AverageCalc(String name) {
        this.name = name;

        new Thread(name + " average") {
            @Override
            public void run() {
                while (true) {
                    try {
                        sleep(1000);
                        double aver;
                        if((aver = calcAverage()) != -1)
                            System.out.println("Average for " + name + " is " + aver);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void addValue(double value) {
        synchronized (average) {
            average.add(value);
        }
    }

    private double calcAverage() {
        synchronized (average) {
            if(average.isEmpty()) return -1;

            double d = average.stream().mapToDouble(a -> a).average().getAsDouble();

            average.clear();

            return d;
        }
    }

}
