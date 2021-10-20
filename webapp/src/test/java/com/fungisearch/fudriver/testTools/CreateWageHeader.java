package com.fungisearch.fudriver.testTools;

import com.fungisearch.fudriver.payroll.wage.command.model.WageHeader;
import com.fungisearch.fudriver.payroll.wage.command.repository.WageRepository;
import com.fungisearch.fudriver.type.command.repository.TypeRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateWageHeader {
    private final WageRepository wageRepository;
    private final BeanValidator beanValidator;
    private final TypeRepository typeRepository;

    @Autowired
    public CreateWageHeader(WageRepository wageRepository, BeanValidator beanValidator, TypeRepository typeRepository) {
        this.wageRepository = wageRepository;
        this.beanValidator = beanValidator;
        this.typeRepository = typeRepository;
    }

    public WageHeader create(String name){
        return new WageHeader.WageHeaderBuilder(wageRepository,beanValidator,typeRepository)
                .name(name)
                .build();
    }
}
