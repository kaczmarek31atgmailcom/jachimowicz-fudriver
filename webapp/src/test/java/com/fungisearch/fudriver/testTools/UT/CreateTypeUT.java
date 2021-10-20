package com.fungisearch.fudriver.testTools.UT;

import com.fungisearch.fudriver.payroll.wage.command.model.WageFactory;
import com.fungisearch.fudriver.payroll.wage.command.model.WageHeader;
import com.fungisearch.fudriver.type.command.model.ExportType;
import com.fungisearch.fudriver.type.command.model.Type;
import com.fungisearch.fudriver.type.command.repository.TypeRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;


public class CreateTypeUT {

    private final TypeRepository typeRepository;
    private final BeanValidator beanValidator;
    private final WageFactory wageFactory;

    public CreateTypeUT(TypeRepository typeRepository, BeanValidator beanValidator, WageFactory wageFactory) {
        this.typeRepository = typeRepository;
        this.beanValidator = beanValidator;
        this.wageFactory = wageFactory;
    }

    public Type create(String name, double weight){
        List<WageHeader> wageHeaders = new ArrayList<>();
        when(wageFactory.getAllHeaders()).thenReturn(wageHeaders);
        Type type = new Type.TypeBuilder(typeRepository,beanValidator,wageFactory)
                .weight((long)(weight * 1000))
                .name(name)
                .exportType(ExportType.EXPORT)
                .build();
        type.setId(1L);
        return type;
    }
}
