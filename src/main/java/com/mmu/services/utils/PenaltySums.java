package com.mmu.services.utils;
public class PenaltySums {
    private double penaltyAmountSum;
    private double anotherParameterSum;

    public PenaltySums(double penaltyAmountSum, double anotherParameterSum) {
        this.penaltyAmountSum = penaltyAmountSum;
        this.anotherParameterSum = anotherParameterSum;
    }

    public double getPenaltyAmountSum() {
        return penaltyAmountSum;
    }

    public void setPenaltyAmountSum(double penaltyAmountSum) {
        this.penaltyAmountSum = penaltyAmountSum;
    }

    public double getAnotherParameterSum() {
        return anotherParameterSum;
    }

    public void setAnotherParameterSum(double anotherParameterSum) {
        this.anotherParameterSum = anotherParameterSum;
    }

    public void add(PenaltySums other) {
        this.penaltyAmountSum += other.penaltyAmountSum;
        this.anotherParameterSum += other.anotherParameterSum;
    }
}