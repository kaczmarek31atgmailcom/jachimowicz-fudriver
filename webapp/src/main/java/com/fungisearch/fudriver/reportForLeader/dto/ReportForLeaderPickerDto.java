package com.fungisearch.fudriver.reportForLeader.dto;


import com.fungisearch.fudriver.person.person.query.dto.ReportForLeaderPersonHeaderDto;

public class ReportForLeaderPickerDto {

    private ReportForLeaderPersonHeaderDto personHeaderDto;
    private String startDate;
    private String endDate;
    private double kraj;
    private double export;
    private double total;
    private double krajFactor;
    private String workingHours;
    private Long workingMinutes;
    private double totalPerHour;
    private double tested;
    private double rejected;

    public double getTested() {
        return tested;
    }

    public void setTested(double tested) {
        this.tested = tested;
    }

    public double getRejected() {
        return rejected;
    }

    public void setRejected(double rejected) {
        this.rejected = rejected;
    }

    public ReportForLeaderPersonHeaderDto getPersonHeaderDto() {
        return personHeaderDto;
    }

    public void setPersonHeaderDto(ReportForLeaderPersonHeaderDto personHeaderDto) {
        this.personHeaderDto = personHeaderDto;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public double getKrajFactor() {
        return krajFactor;
    }

    public void setKrajFactor(double krajFactor) {
        this.krajFactor = krajFactor;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public Long getWorkingMinutes() {
        return workingMinutes;
    }

    public void setWorkingMinutes(Long workingMinutes) {
        this.workingMinutes = workingMinutes;
    }

    public double getTotalPerHour() {
        return totalPerHour;
    }

    public void setTotalPerHour(double totalPerHour) {
        this.totalPerHour = totalPerHour;
    }
}
