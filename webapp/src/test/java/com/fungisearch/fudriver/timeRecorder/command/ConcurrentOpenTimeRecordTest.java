package com.fungisearch.fudriver.timeRecorder.command;

import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLogFactory;
import com.fungisearch.fudriver.timeRecorder.command.model.WorkPeriodSummaryDto;
import com.fungisearch.fudriver.timeRecorder.command.repository.TimeWorkLogHibernateRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.junit.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Properties;
import java.util.concurrent.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConcurrentOpenTimeRecordTest {
    @Test
    public void shouldSerializeOpeningForTheSameEmployee() throws Exception {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(new DriverManagerDataSource("jdbc:h2:mem:concurrentWorkTime;DB_CLOSE_DELAY=-1", "sa", ""));
        bean.setPackagesToScan("com.fungisearch.fudriver");
        bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        bean.setJpaProperties(properties);
        bean.afterPropertiesSet();
        EntityManagerFactory emf = bean.getObject();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        EntityManager first = emf.createEntityManager();
        CountDownLatch attempted = new CountDownLatch(1);
        try {
            first.getTransaction().begin();
            Person person = new Person();
            ReflectionTestUtils.setField(person, "active", true);
            ReflectionTestUtils.setField(person, "isForeigner", false);
            first.persist(person);
            first.getTransaction().commit();
            Long personId = person.getId();

            first.getTransaction().begin();
            WorkPeriodSummaryDto opened = handler(first, personId).handle(command());
            assertTrue(opened.isStateUpdated);
            first.flush();

            Future<WorkPeriodSummaryDto> second = executor.submit(() -> {
                EntityManager em = emf.createEntityManager();
                try {
                    em.getTransaction().begin();
                    OpenTimeRecordCommandHandler handler = handler(em, personId);
                    attempted.countDown();
                    WorkPeriodSummaryDto result = handler.handle(command());
                    em.getTransaction().commit();
                    return result;
                } finally {
                    if (em.getTransaction().isActive()) em.getTransaction().rollback();
                    em.close();
                }
            });
            assertTrue(attempted.await(5, TimeUnit.SECONDS));
            try {
                second.get(300, TimeUnit.MILLISECONDS);
                fail("The second open must wait for the first transaction");
            } catch (TimeoutException expected) {
                // The first transaction deliberately holds the employee lock.
            }
            first.getTransaction().commit();
            WorkPeriodSummaryDto repeated = second.get(5, TimeUnit.SECONDS);
            assertFalse(repeated.isStateUpdated);
            assertTrue(repeated.active);
            assertEquals(Long.valueOf(1), first.createQuery(
                    "select count(t) from TimeWorkLog t where t.person.id = :id and t.isOpened = true",
                    Long.class).setParameter("id", personId).getSingleResult());
        } finally {
            if (first.getTransaction().isActive()) first.getTransaction().rollback();
            executor.shutdownNow();
            executor.awaitTermination(5, TimeUnit.SECONDS);
            first.close();
            bean.destroy();
        }
    }

    private OpenTimeRecordCommandHandler handler(EntityManager em, Long personId) {
        TimeWorkLogHibernateRepository repository = new TimeWorkLogHibernateRepository();
        ReflectionTestUtils.setField(repository, "em", em);
        PersonFactory people = mock(PersonFactory.class);
        when(people.createByRfid("test-badge")).thenAnswer(invocation -> em.find(Person.class, personId));
        TimeWorkLogFactory factory = new TimeWorkLogFactory(repository, mock(BeanValidator.class),
                mock(ApplicationEventPublisher.class), null);
        return new OpenTimeRecordCommandHandler(factory, null, people);
    }

    private AddTimeRecordCommand command() {
        AddTimeRecordCommand command = new AddTimeRecordCommand();
        command.rfid = "test-badge";
        return command;
    }
}
