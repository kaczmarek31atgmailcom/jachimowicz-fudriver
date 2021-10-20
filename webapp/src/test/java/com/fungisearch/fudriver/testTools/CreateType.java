package com.fungisearch.fudriver.testTools;

import com.fungisearch.fudriver.payroll.wage.command.model.WageFactory;
import com.fungisearch.fudriver.type.command.model.ExportType;
import com.fungisearch.fudriver.type.command.model.Type;
import com.fungisearch.fudriver.type.command.repository.TypeRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CreateType {

    private final TypeRepository typeRepository;
    private final BeanValidator beanValidator;
    private final WageFactory wageFactory;

    @Autowired
    public CreateType(TypeRepository typeRepository,
                      BeanValidator beanValidator,
                      WageFactory wageFactory){
        this.typeRepository = typeRepository;
        this.beanValidator = beanValidator;
        this.wageFactory = wageFactory;
    }

    public Type create(){
        return new Type.TypeBuilder(typeRepository,beanValidator,wageFactory)
                .exportType(ExportType.EXPORT)
                .name("test_type")
                .weight(1100)
                .build();
    }
}
