package com.fungisearch.fudriver.settings.command.model;


import com.fungisearch.fudriver.settings.command.repository.SubsoilRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SubsoilTest {

    @Mock
    private BeanValidator beanValidator;
    @Mock
    private SubsoilRepository subsoilRepository;

    @Test
    public void shouldCreateSubsoil(){
        //Given
        Subsoil subsoil = new Subsoil.SubsoilBuilder(subsoilRepository,beanValidator).name("test_subsoil").build();
        //When
        subsoil.setId(1L);
        // Then
        assertEquals(subsoil.getName(), "test_subsoil");
        assertTrue(subsoil.isActive());
    }

    @Test
    public void shouldChangeName(){
        //Given
        Subsoil subsoil = new Subsoil.SubsoilBuilder(subsoilRepository,beanValidator).name("test_subsoil").build();
        //when
        subsoil.changeName(new Subsoil.ChangeName().name("new_subsoil"));
        //then
        assertEquals(subsoil.getName(),"new_subsoil");
    }

    @Test
    public void shouldInactivateSubsoil(){
        //Given
        Subsoil subsoil = new Subsoil.SubsoilBuilder(subsoilRepository,beanValidator).name("test_subsoil").build();
        //when
        subsoil.inactivate();
        //Then
        assertFalse(subsoil.isActive());
    }
    //Given

}