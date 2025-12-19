package com.hustleborn.service.model.reports;

public class ProfitReport {
    private Double totalSalesVolume;
    private Double grossProfit;
    private Double netProfit;
    private Double damagedLoss;
    private Double totalExpenses;

    public ProfitReport() {}

    public ProfitReport(Double totalSalesVolume, Double grossProfit, Double netProfit, Double damagedLoss, Double totalExpenses) {
        this.totalSalesVolume = totalSalesVolume;
        this.grossProfit = grossProfit;
        this.netProfit = netProfit;
        this.damagedLoss = damagedLoss;
        this.totalExpenses = totalExpenses;
    }

    public Double getTotalSalesVolume() {
        return totalSalesVolume;
    }

    public void setTotalSalesVolume(Double totalSalesVolume) {
        this.totalSalesVolume = totalSalesVolume;
    }

    public Double getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(Double grossProfit) {
        this.grossProfit = grossProfit;
    }

    public Double getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(Double netProfit) {
        this.netProfit = netProfit;
    }

    public Double getDamagedLoss() {
        return damagedLoss;
    }

    public void setDamagedLoss(Double damagedLoss) {
        this.damagedLoss = damagedLoss;
    }

    public Double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(Double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }
}