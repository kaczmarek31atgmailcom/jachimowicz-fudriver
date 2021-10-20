package com.fungisearch.fudriver.reclassification.query.dto;

import java.util.Date;

/**
 * Created by marcin on 10.02.16.
 */
public class LocalReclassificationHeaderDto {
    public Long id;
    public Long remoteId;
    public String description;
    public Date created;
    public Long totalAmount;
    public Boolean processed;
    public Date processedDate;
}
