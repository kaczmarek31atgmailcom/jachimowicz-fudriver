package com.fungisearch.fudriver.person.person.command;

import com.fungisearch.fudriver.person.barcode.command.CreateUniqCommand;
import com.fungisearch.fudriver.person.barcode.command.CreateUniqCommandHandler;
import com.fungisearch.fudriver.wozek.command.model.Uniq;
import com.fungisearch.fudriver.wozek.command.repository.UniqRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.List;

import static org.junit.Assert.assertEquals;


@ContextConfiguration(locations={"/test-spring.xml"})
public class CreateUniqCommandHandlerITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CreateUniqCommandHandler createUniqCommandHandler;

    @Autowired
    private UniqRepository uniqRepository;

    @Test
    public void shouldReserveCodes() {
        //Given
        CreateUniqCommand command = new CreateUniqCommand();
        command.pickerId = 1L;
        command.numberOfUniqsToBeCreated = 100;
        //When
        createUniqCommandHandler.handle(command);
        List<Uniq> actual = uniqRepository.findOpenForPicker(command.pickerId);
        //Then
        assertEquals(actual.size(),100);
    }
}