package com.asdbll.twitter;

public class PercentageCalculator {

    public double calcPositive(int counter, int positiveCounter) {
        double positiveResult = (positiveCounter * 100.0) / counter;
        return positiveResult;
    }

    public double calcNegative(int counter, int negativeCounter) {
        double netResult = (negativeCounter * 100.0) / counter;
        return netResult;
    }

    public double calcNeutral(int counter, int neutralCounter) {
        double negResult = (neutralCounter * 100.0) / counter;
        return negResult;
    }
}


