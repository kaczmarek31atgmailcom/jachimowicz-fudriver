package com.fungisearch.fudriver.payroll.wage.query.dto;

import java.util.ArrayList;
import java.util.List;

public class WageHeaderDto {

    public long id;
    public String name;
    public List<WageDto> wages = new ArrayList<>();
}
