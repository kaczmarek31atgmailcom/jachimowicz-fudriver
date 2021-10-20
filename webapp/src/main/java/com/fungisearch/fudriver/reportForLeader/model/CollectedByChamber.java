package com.fungisearch.fudriver.reportForLeader.model;


public class CollectedByChamber {
    int cycleId;
    String chamberName;
    double kraj;
    double export;
    double total;

    public int getCycleId() {
        return cycleId;
    }

    public void setCycleId(int cycleId) {
        this.cycleId = cycleId;
    }

    public String getChamberName() {
        return chamberName;
    }

    public void setChamberName(String chamberName) {
        this.chamberName = chamberName;
    }

    public double getKraj() {
        return kraj;
    }

    public void setKraj(double kraj) {
        this.kraj = kraj;
    }

    public double getExport() {
        return export;
    }

    public void setExport(double export) {
        this.export = export;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
