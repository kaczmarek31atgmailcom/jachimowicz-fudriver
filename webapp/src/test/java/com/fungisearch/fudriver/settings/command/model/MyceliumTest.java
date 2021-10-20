package com.fungisearch.fudriver.settings.command.model;

import com.fungisearch.fudriver.settings.command.repository.MyceliumRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;


@RunWith(MockitoJUnitRunner.class)
public class MyceliumTest {

    @Mock
    private MyceliumRepository myceliumRepository;
    @Mock
    private BeanValidator beanValidator;

    @Test
    public void shouldCreateMycelium(){
        //Given
        Mycelium mycelium = new Mycelium.MyceliumBuilder(myceliumRepository,beanValidator).name("test_mycelium").build();
        mycelium.setId(1L);
        //When & Then
        assertEquals(mycelium.getName(),"test_mycelium");
        assertTrue(mycelium.isActive());
    }

    @Test
    public void shouldChangeMyceliumName(){
        //Given
        Mycelium mycelium = new Mycelium.MyceliumBuilder(myceliumRepository,beanValidator).name("test_mycelium").build();
        //When
        mycelium.edit(new Mycelium.Edit().name("new_name"));
        //Then
        assertEquals(mycelium.getName(),"new_name");
    }

    @Test
    public void shouldRemoveMycelium(){
        //Given
        Mycelium mycelium = new Mycelium.MyceliumBuilder(myceliumRepository,beanValidator).name("test_mycelium").build();
        assertTrue(mycelium.isActive());
        //When
        mycelium.remove();
        //Then
        assertFalse(mycelium.isActive());
    }
}