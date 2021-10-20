package com.fungisearch.fudriver.reclassification.query.dto;

import java.util.Date;

/**
 * Created by marcin on 11.02.16.
 */
public class LocalReclassificationDetailDto {
    public Long id;
    public Long zarobkiId;
    public String barcode;
    public Long pickerId;
    public Long remotePickerId;
    public Long remoteUniqId;
    public Integer pickerNr;
    public String pickerName;
    public String pickerSurname;
    public String pickerGroupName;
    public String halaName;
    public String wagowyLogin;
    public String wagowyName;
    public String wagowySurname;
    public Date harvestDate;
    public Boolean payed;
    public String originalTypeName;
    public Double originalTypeWeight;
    public Long reclassifcationTypeId;
    public Long afterReclassifcationTypeId;
    public String reclassificationTypeName;
    public Double reclassificationTypeWeight;
    public Boolean active;
}
