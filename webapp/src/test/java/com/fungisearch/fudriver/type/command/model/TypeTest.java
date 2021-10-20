package com.fungisearch.fudriver.type.command.model;

import com.fungisearch.fudriver.payroll.wage.command.model.WageFactory;
import com.fungisearch.fudriver.type.command.repository.TypeRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(MockitoJUnitRunner.class)
public class TypeTest {

    @Mock
    private TypeRepository typeRepository;
    @Mock
    private BeanValidator beanValidator;
    @Mock
    private WageFactory wageFactory;


    @Test
    public void shouldCreateType(){
        //Given
        Type.TypeBuilder typeBuilder = new Type.TypeBuilder(typeRepository,beanValidator,wageFactory);
        //When
        Type tested = typeBuilder.exportType(ExportType.EXPORT).weight(1100).name("test_type").build();
        //Then
        assertEquals("test_type",tested.getName());
        assertEquals(new Double(1.1), tested.getWeight());
        assertEquals(ExportType.EXPORT,tested.getExportType());
        assertFalse(tested.getArchived());
    }

    @Test
    public void shouldCreateWageHeader(){
        Type.TypeBuilder typeBuilder = new Type.TypeBuilder(typeRepository,beanValidator,wageFactory);
    }

}