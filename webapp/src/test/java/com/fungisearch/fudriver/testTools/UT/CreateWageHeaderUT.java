package com.fungisearch.fudriver.testTools.UT;

import com.fungisearch.fudriver.payroll.wage.command.model.WageHeader;
import com.fungisearch.fudriver.payroll.wage.command.repository.WageRepository;
import com.fungisearch.fudriver.type.command.repository.TypeRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

public class CreateWageHeaderUT {


    private final WageRepository wageRepository;
    private final BeanValidator beanValidator;
    private final TypeRepository typeRepository;

    public CreateWageHeaderUT(WageRepository wageRepository, BeanValidator beanValidator, TypeRepository typeRepository) {
        this.wageRepository = wageRepository;
        this.beanValidator = beanValidator;
        this.typeRepository = typeRepository;
    }

    public WageHeader create(String name) {
        return new WageHeader.WageHeaderBuilder(wageRepository, beanValidator, typeRepository)
                .name(name)
                .build();
    }
}
