package com.fungisearch.fudriver.payroll.wage.query.dao;


import com.fungisearch.fudriver.payroll.wage.query.dto.PersonWageDto;
import com.fungisearch.fudriver.payroll.wage.query.dto.WageDto;
import com.fungisearch.fudriver.payroll.wage.query.dto.WageHeaderDto;

import java.util.List;

public interface WageDao {
    List<WageHeaderDto> getWageHeaders();
    List<WageDto> getWages(long headerId);
    List<PersonWageDto> getActivePeopleWages();
}
