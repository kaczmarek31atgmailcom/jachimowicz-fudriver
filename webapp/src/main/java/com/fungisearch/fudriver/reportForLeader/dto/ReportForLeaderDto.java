package com.fungisearch.fudriver.reportForLeader.dto;

import java.util.List;

/**
 * Created by marcin on 05.12.14.
 */
public class ReportForLeaderDto {

    List<ReportForLeaderPickerDto> pickers;
    double sumKraj;
    double sumExport;
    double sumTotal;
    double sumKrajFactor;
    String sumWorkingHours;
    double sumTotalPerHour;
    double averageTested;
    double averageRejected;

    public double getAverageTested() {
        return averageTested;
    }

    public void setAverageTested(double averageTested) {
        this.averageTested = averageTested;
    }

    public double getAverageRejected() {
        return averageRejected;
    }

    public void setAverageRejected(double averageRejected) {
        this.averageRejected = averageRejected;
    }

    public List<ReportForLeaderPickerDto> getPickers() {
        return pickers;
    }

    public void setPickers(List<ReportForLeaderPickerDto> pickers) {
        this.pickers = pickers;
    }

    public double getSumKraj() {
        return sumKraj;
    }

    public void setSumKraj(double sumKraj) {
        this.sumKraj = sumKraj;
    }

    public double getSumExport() {
        return sumExport;
    }

    public void setSumExport(double sumExport) {
        this.sumExport = sumExport;
    }

    public double getSumTotal() {
        return sumTotal;
    }

    public void setSumTotal(double sumTotal) {
        this.sumTotal = sumTotal;
    }

    public double getSumKrajFactor() {
        return sumKrajFactor;
    }

    public void setSumKrajFactor(double sumKrajFactor) {
        this.sumKrajFactor = sumKrajFactor;
    }

    public String getSumWorkingHours() {
        return sumWorkingHours;
    }

    public void setSumWorkingHours(String sumWorkingHours) {
        this.sumWorkingHours = sumWorkingHours;
    }

    public double getSumTotalPerHour() {
        return sumTotalPerHour;
    }

    public void setSumTotalPerHour(double sumTotalPerHour) {
        this.sumTotalPerHour = sumTotalPerHour;
    }

}
