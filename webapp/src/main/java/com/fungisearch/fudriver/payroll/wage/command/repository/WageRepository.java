package com.fungisearch.fudriver.payroll.wage.command.repository;

import com.fungisearch.fudriver.payroll.wage.command.model.Wage;
import com.fungisearch.fudriver.payroll.wage.command.model.WageHeader;

import java.util.List;

/**
 * Created by marcin on 16.05.16.
 */
public interface WageRepository {
    void save(Wage wage);

    void save(WageHeader wageHeader);
    List getAllHeaders();

    Wage findWage(long id);

    WageHeader findHeader(long id);


}
