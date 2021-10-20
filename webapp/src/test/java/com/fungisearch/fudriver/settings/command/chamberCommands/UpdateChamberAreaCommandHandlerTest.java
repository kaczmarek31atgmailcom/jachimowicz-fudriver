package com.fungisearch.fudriver.settings.command.chamberCommands;

import com.fungisearch.fudriver.settings.command.model.Chamber;
import com.fungisearch.fudriver.settings.command.model.ChamberFactory;
import com.fungisearch.fudriver.settings.command.model.DepotFactory;
import com.fungisearch.fudriver.settings.command.repository.ChamberRepository;
import com.fungisearch.fudriver.settings.command.repository.DepotRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateChamberAreaCommandHandlerTest {

    @Mock
    private ChamberRepository chamberRepository;
    @Mock
    private BeanValidator beanValidator;
    @Mock
    private DepotRepository depotRepository;

    @InjectMocks
    private UpdateChamberAreaCommandHandler handler;

    @InjectMocks
    private DepotFactory depotFactory;
    @Mock
    private ChamberFactory chamberFactory;

    @Test
    public void shouldUpdateChamberArea(){
        //Given
        Chamber chamber = new Chamber.ChamberBuilder(chamberRepository,beanValidator)
                .area(100)
                .name("test_chamber")
                .depot(depotFactory.getBuilder().name("TestDepot").build())
                .build();
        when(chamberFactory.find(any(Long.class))).thenReturn(chamber);
        //When
        UpdateChamberAreaCommand command = new UpdateChamberAreaCommand();
        command.id = 1L;
        command.area = 200;
        handler.handle(command);
        //Then
        assertEquals(command.area,chamber.getArea());
    }
}