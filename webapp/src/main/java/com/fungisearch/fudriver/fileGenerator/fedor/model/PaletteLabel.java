package com.fungisearch.fudriver.fileGenerator.fedor.model;

import java.util.Date;
import java.util.Map;

/**
 * Created by marcin on 27.07.16.
 */
public class PaletteLabel {
    private Long paletteId;
    private Long orderId;
    private String barcode;
    private String customerName;
    private Date required;
    private String plate;
    private String companyName;
    private String companyCity;
    private String companyStreet;
    private String companyNip;
    private String companyRegon;
    private String companyEmail;
    private String companyPhoneNo;

    private Map<String,Long> details;

    public Long getPaletteId() {
        return paletteId;
    }

    public void setPaletteId(Long paletteId) {
        this.paletteId = paletteId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getBarcode() {
        String paletteNr = Long.toString(paletteId);
        StringBuilder sb  = new StringBuilder(paletteNr);
        for (int i = paletteNr.length(); i< 10; i++){
            sb.insert(0,"0");
        }
        sb.insert(0,"999");
        return sb.toString();
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getRequired() {
        return required;
    }

    public void setRequired(Date required) {
        this.required = required;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public void setDetails(Map<String, Long> details) {
        this.details = details;
    }

    public Map<String, Long> getDetails() {
        return details;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCity() {
        return companyCity;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }

    public String getCompanyStreet() {
        return companyStreet;
    }

    public void setCompanyStreet(String companyStreet) {
        this.companyStreet = companyStreet;
    }

    public String getCompanyNip() {
        return companyNip;
    }

    public void setCompanyNip(String companyNip) {
        this.companyNip = companyNip;
    }

    public String getCompanyRegon() {
        return companyRegon;
    }

    public void setCompanyRegon(String companyRegon) {
        this.companyRegon = companyRegon;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyPhoneNo() {
        return companyPhoneNo;
    }

    public void setCompanyPhoneNo(String companyPhoneNo) {
        this.companyPhoneNo = companyPhoneNo;
    }
}
