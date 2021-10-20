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
public class UpdateChamberNameCommandHandlerTest {

    @Mock
    private ChamberRepository chamberRepository;
    @Mock
    private BeanValidator beanValidator;
    @Mock
    private DepotRepository depotRepository;
    @Mock
    private ChamberFactory chamberFactory;

    @InjectMocks
    private UpdateChamberNameCommandHandler handler;
    @InjectMocks
    private DepotFactory depotFactory;


    @Test
    public void shouldUpdateChamberName(){
        //Given
        Chamber chamber = new Chamber.ChamberBuilder(chamberRepository,beanValidator)
                .name("ala_ma_kota")
                .depot(depotFactory.getBuilder().name("test_depot").build())
                .area(100)
                .build();
        when(chamberFactory.find(any(Long.class))).thenReturn(chamber);
        UpdateChamberNameCommand command = new UpdateChamberNameCommand();
        command.id = 1L;
        command.name = "NEW_NAME";
        //When
        handler.handle(command);
        //Then
        assertEquals(chamber.getName(),command.name);
    }

}