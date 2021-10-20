package com.fungisearch.fudriver.payroll.wage.command.model;


import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;
import com.fungisearch.fudriver.payroll.wage.command.repository.WageRepository;
import com.fungisearch.fudriver.type.command.model.ExportType;
import com.fungisearch.fudriver.type.command.model.Type;
import com.fungisearch.fudriver.type.command.repository.TypeRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WageHeaderTest {

    @Mock
    private WageRepository wageRepository;
    @Mock
    private BeanValidator beanValidator;
    @Mock
    private TypeRepository typeRepository;
    @InjectMocks
    private WageFactory wageFactory;


    @Test
    public void INTERNAL_shouldCreateTypes(){
        assertEquals(6, getTypes().size());
    }

    @Test
    public void shouldCreateWageHeaderWithValidName(){
        //Given
        List<Type> types = getTypes();
        when(typeRepository.findAll()).thenReturn(types);
        WageHeader.WageHeaderBuilder builder = new WageHeader.WageHeaderBuilder(wageRepository,beanValidator,typeRepository);
        //When
        WageHeader wageHeader = builder.name("test_builder").build();
        //Then
        assertEquals("test_builder",wageHeader.getName());
    }

    @Test
    public void shouldCreateActiveWageHeader(){
        //Given
        List<Type> types = getTypes();
        when(typeRepository.findAll()).thenReturn(types);
        WageHeader.WageHeaderBuilder builder = new WageHeader.WageHeaderBuilder(wageRepository,beanValidator,typeRepository);
        //When
        WageHeader wageHeader = builder.name("test_builder").build();
        //Then
        assertTrue(wageHeader.isActive);
    }

    @Test
    public void shouldCreateProperNumberOfWages(){
        //Given
        List<Type> types = getTypes();
        when(typeRepository.findAll()).thenReturn(types);
        WageHeader.WageHeaderBuilder builder = new WageHeader.WageHeaderBuilder(wageRepository,beanValidator,typeRepository);
        //When
        WageHeader wageHeader = builder.name("test_builder").build();
        //Then
        assertEquals(18,wageHeader.getWages().size());
    }

    @Test
    public void shouldCreateWageForTheType(){
        //Given
        List<Type> types = new ArrayList<>();
        Type type = createType(ExportType.EXPORT,"ala_ma_kota",1.23);
        type.setId(1L);
        types.add(type);
        when(typeRepository.findAll()).thenReturn(types);
        WageHeader.WageHeaderBuilder builder = new WageHeader.WageHeaderBuilder(wageRepository,beanValidator,typeRepository);
        //When
        WageHeader wageHeader = builder.name("test_builder").build();
        //Then
        assertEquals(3,wageHeader.getWages().size());
        for(Wage wage: wageHeader.getWages()){
            assertEquals(new Long(1),wage.getType().getId());
            assertEquals("ala_ma_kota",wage.getType().getName());
            assertEquals(new Double(1.23),wage.getType().getWeight());
        }
        assertEquals(SalaryDayTypeEnum.REGULAR_DAY, wageHeader.getWages().get(0).getDayType());
        assertEquals(SalaryDayTypeEnum.SUNDAY, wageHeader.getWages().get(1).getDayType());
        assertEquals(SalaryDayTypeEnum.BONUS_DAY, wageHeader.getWages().get(2).getDayType());
    }


    private List<Type> getTypes(){
        List<Type> result = new ArrayList<>();
        result.add(createType(ExportType.EXPORT,"exp_1",1));
        result.add(createType(ExportType.EXPORT,"exp_2",2));
        result.add(createType(ExportType.EXPORT,"exp_3",3));
        result.add(createType(ExportType.KRAJ,"kraj_1",4));
        result.add(createType(ExportType.KRAJ,"kraj_2",5));
        result.add(createType(ExportType.KRAJ,"kraj_3",6));
        return result;
    }

    private Type createType(ExportType exportType,String name,double weight){
        return new Type.TypeBuilder(typeRepository,beanValidator,wageFactory)
                .name(name)
                .weight((long)(weight * 1000))
                .exportType(exportType)
                .build();

    }
}