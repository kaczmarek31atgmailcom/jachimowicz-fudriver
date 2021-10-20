package com.fungisearch.fudriver.payroll.wage.command.model;


import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;
import com.fungisearch.fudriver.payroll.wage.command.repository.WageRepository;
import com.fungisearch.fudriver.type.command.model.ExportType;
import com.fungisearch.fudriver.type.command.model.Type;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import com.fungisearch.fudriver.type.command.repository.TypeRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class WageTest {

    @Mock
    private WageRepository wageRepository;
    @Mock
    private BeanValidator beanValidator;

    @Mock
    private WageFactory wageFactory;

    @Mock
    private TypeRepository typeRepository;
    @InjectMocks
    private TypeFactory typeFactory;


    @Test
    public void shouldCreateWage(){
        //Given
        Wage.WageBuilder builder = new Wage.WageBuilder(wageRepository,beanValidator);
        Type type = createType(ExportType.EXPORT,"test_type",1.23);
        //When
        Wage tested = builder.dayType(SalaryDayTypeEnum.REGULAR_DAY).type(type).value(1L).build();
        //Then
        assertEquals(SalaryDayTypeEnum.REGULAR_DAY,tested.getDayType());
        assertEquals(type,tested.getType());
        assertEquals(new Long(1),tested.getValue());
    }

    @Test
    public void shouldUpdateValue(){
        //Given
        Wage.WageBuilder builder = new Wage.WageBuilder(wageRepository,beanValidator);
        Type type = createType(ExportType.EXPORT,"test_type",1.23);
        Wage tested = builder.dayType(SalaryDayTypeEnum.REGULAR_DAY).type(type).value(1L).build();
        //When
        tested.updateValue(3L);
        //Then
        assertEquals(new Long(3),tested.getValue());
    }

    @Test
    public void shouldNotUpdateValueBelowZero(){
        //Given
        Wage.WageBuilder builder = new Wage.WageBuilder(wageRepository,beanValidator);
        Type type = createType(ExportType.EXPORT,"test_type",1.23);
        Wage tested = builder.dayType(SalaryDayTypeEnum.REGULAR_DAY).type(type).value(1L).build();
        //When & Then
        try {
            tested.updateValue(-1L);
            fail();
        } catch (IllegalStateException ex){
            assertEquals("Wage value below zero", ex.getMessage());
        }
        assertEquals(new Long(1),tested.getValue());
    }



    private Type createType(ExportType exportType, String name, double weight){
        Type type = typeFactory.getBuilder()
                .name(name)
                .weight((long)(weight * 1000))
                .exportType(exportType)
                .build();
        return type;
    }

    @Test
    public void shouldCreateWageWithValueEqualsZeroWhenValueNotProvided(){
        //Given
        Wage.WageBuilder builder = new Wage.WageBuilder(wageRepository,beanValidator);
        Type type = createType(ExportType.EXPORT,"test_type",1.23);
        //When
        Wage tested = builder.dayType(SalaryDayTypeEnum.REGULAR_DAY).type(type).build();
        //Then
        assertEquals(SalaryDayTypeEnum.REGULAR_DAY,tested.getDayType());
        assertEquals(type,tested.getType());
        assertEquals(new Long(0),tested.getValue());
    }



}