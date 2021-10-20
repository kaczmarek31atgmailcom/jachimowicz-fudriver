package com.fungisearch.fudriver.zarobki.query.dto;

import java.util.Date;

/**
 * Dto do wyświetlania informacji o zeskanowanej skrzyneczce podczas tworzenia wysyłki BRC
 */
public class BoxBrcDto {
    public Long id;
    public Long uniqId;
    public Long pickerId;
    public String pickerName;
    public String pickerSurname;
    public Long rodzajId;
    public String rodzajName;
    public Double rodzajWeight;
    public Date harvestDate;
    public String halaName;
}
