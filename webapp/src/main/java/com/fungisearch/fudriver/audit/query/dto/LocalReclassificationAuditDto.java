package com.fungisearch.fudriver.audit.query.dto;

import java.util.Date;

/**
 * Created by marcin on 03.08.16.
 */
public class LocalReclassificationAuditDto {
    public long uniqId;
    public Date date;
    public long supplierId;
    public long pickerId;
    public int pickerNr;
    public String pickerName;
    public String pickerSurname;
    public String login;
    public String userName;
    public String userSurname;
    public String sourceTypeName;
    public double sourceTypeWeight;
    public String targetTypeName;
    public double targetTypeWeight;
    public String sourceChamberName;
    public String targetChamberName;
    public String reason;
}
