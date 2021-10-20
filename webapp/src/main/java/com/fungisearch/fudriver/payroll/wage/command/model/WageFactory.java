package com.fungisearch.fudriver.payroll.wage.command.model;

import com.fungisearch.fudriver.payroll.wage.command.repository.WageRepository;
import com.fungisearch.fudriver.type.command.repository.TypeRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class WageFactory {

    private final WageRepository wageRepository;
    private final BeanValidator beanValidator;
    private final TypeRepository typeRepository;

    @Autowired
    public WageFactory(WageRepository wageRepository, BeanValidator beanValidator, TypeRepository typeRepository) {
        this.wageRepository = wageRepository;
        this.beanValidator = beanValidator;
        this.typeRepository = typeRepository;
    }

    public Wage.WageBuilder builder() {
        return new Wage.WageBuilder(wageRepository, beanValidator);
    }

    public WageHeader.WageHeaderBuilder headerBuilder() {
        return new WageHeader.WageHeaderBuilder(wageRepository, beanValidator, typeRepository);
    }

    public List<WageHeader> getAllHeaders() {
        List<WageHeader> headers = wageRepository.getAllHeaders();
        return headers.stream()
                .map(h -> (setRepositoryAndValidator(h)))
                .collect(Collectors.toList());
    }

    private WageHeader setRepositoryAndValidator(WageHeader wageHeader) {
        wageHeader.wageRepository = wageRepository;
        wageHeader.beanValidator = beanValidator;
        return wageHeader;
    }

    public Wage findWage(long id) {
        Wage wage = wageRepository.findWage(id);
        wage.setBeanValidator(beanValidator);
        wage.setWageRepository(wageRepository);
        return wage;
    }

    public WageHeader findHeader(long id) {
        WageHeader wageHeader = wageRepository.findHeader(id);
        wageHeader.typeRepository = typeRepository;
        wageHeader.wageRepository = wageRepository;
        wageHeader.beanValidator = beanValidator;
        return wageHeader;
    }

}
