package com.fungisearch.fudriver.settings.command;

import com.fungisearch.fudriver.settings.command.depotCommands.DeleteDepotCommandHandler;
import com.fungisearch.fudriver.settings.command.model.Depot;
import com.fungisearch.fudriver.settings.command.model.DepotFactory;
import com.fungisearch.fudriver.settings.command.repository.DepotRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;




@RunWith(MockitoJUnitRunner.class)
public class DeleteDepotCommandHandlerTest {

    @Mock
    private DepotRepository depotRepository;
    @Mock
    private BeanValidator beanValidator;
    @Mock
    private DepotFactory depotFactory;
    @InjectMocks
    private DeleteDepotCommandHandler deleteDepotCommandHandler;

    @Test
    public void shouldInactivateDepot(){
        //Given
        Depot depot = new Depot.DepotBuilder(depotRepository,beanValidator).name("test_depot").build();
        assertTrue(depot.isActive());
        when(depotFactory.find(any(Long.class))).thenReturn(depot);
        //When
        deleteDepotCommandHandler.handle(1L);
        //Then
        assertFalse(depot.isActive());
    }
}