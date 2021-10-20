package com.fungisearch.fudriver.zarobki.query.dto;

import java.util.Date;
import java.util.Map;

/**
 * Nagłówek do przeklasyfikowań palet
 */
public class PaletaHeaderDto {

    public Long nr;
    public Long rodzajId;
    public String rodzajName;
    public Double rodzajWeight;
    public Long totalPcs;
    public Double totalWeight;
    public Date harvestDate;
    public Map<Long,String> hale;
}
