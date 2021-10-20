package com.fungisearch.fudriver.wozek.command.model;

import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.wozek.command.repository.UniqRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;



@ContextConfiguration(locations = {"/test-spring.xml"})
public class UniqITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private UniqRepository uniqRepository;

    @Autowired
    private BeanValidator beanValidator;

    @Test
    public void shouldCreateUniq() {
        //Given
        Uniq uniq = new Uniq.UniqBuilder(uniqRepository, beanValidator)
                .uniqId(1L)
                .pickerId(1L)
                .build();
        //When
        uniq.create();
        uniqRepository.findTransactional(uniq.getPickerId(), uniq.getUniqId());
        //Then
        assertFalse(uniq.isUsed());
    }

    @Test
    public void shouldUtiliseUniq() {
        Uniq uniq = new Uniq.UniqBuilder(uniqRepository, beanValidator)
                .uniqId(1L)
                .pickerId(1L)
                .build();
        //When
        uniq.create();
        uniq.utilise();
        Uniq actual = uniqRepository.findTransactional(uniq.getPickerId(), uniq.getUniqId());
        //Then
        assertTrue(actual.isUsed());
    }

    @Test
    public void shouldReclaimUniq() {
        Uniq uniq = new Uniq.UniqBuilder(uniqRepository, beanValidator)
                .uniqId(1L)
                .pickerId(1L)
                .build();
        //When
        uniq.create();
        uniq.utilise();
        assertTrue(uniq.isUsed());
        uniq.reclaim();
        assertFalse(uniq.isUsed());
        Uniq actual = uniqRepository.findTransactional(uniq.getPickerId(), uniq.getUniqId());
        //Then
        assertFalse(actual.isUsed());
    }

}