package com.fungisearch.fudriver.testTools.UT;

import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;
import com.fungisearch.fudriver.payroll.wage.command.model.Wage;
import com.fungisearch.fudriver.payroll.wage.command.model.WageHeader;
import com.fungisearch.fudriver.payroll.wage.command.repository.WageRepository;
import com.fungisearch.fudriver.type.command.model.Type;
import com.fungisearch.fudriver.validation.BeanValidator;

public class CreateWageUT {

    private final WageRepository wageRepository;
    private final BeanValidator beanValidator;
    private final WageHeader wageHeader;

    public CreateWageUT(WageRepository wageRepository, BeanValidator beanValidator, WageHeader wageHeader) {
        this.wageRepository = wageRepository;
        this.beanValidator = beanValidator;
        this.wageHeader = wageHeader;
    }

    public Wage create(long value, Type type, SalaryDayTypeEnum dayType) {
        return new Wage.WageBuilder(wageRepository, beanValidator)
                .value(value)
                .type(type)
                .dayType(dayType)
                .wageHeader(wageHeader)
                .build();
    }
}
