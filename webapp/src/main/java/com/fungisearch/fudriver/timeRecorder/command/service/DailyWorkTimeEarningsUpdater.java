package com.fungisearch.fudriver.timeRecorder.command.service;

import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLog;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.person.person.command.model.PayrollTypeEnum;
import com.fungisearch.fudriver.timeRecorder.command.model.WorkTimeChanged;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiEntry;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class DailyWorkTimeEarningsUpdater {
    @PersistenceContext
    private EntityManager em;

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void update(WorkTimeChanged event) {
        Set<LocalDate> days = new TreeSet<>();
        addDays(days, event.previousStart, event.previousEnd);
        addDays(days, event.start, event.end);
        // Mutations and this listener share a transaction and hold the employee lock.
        em.flush();
        for (LocalDate day : days) updateDay(event.personId, day);
    }

    private void addDays(Set<LocalDate> days, Date start, Date end) {
        if (start == null) return;
        LocalDate first = new DateTime(start).toLocalDate();
        LocalDate last = new DateTime(end == null ? start : end).toLocalDate();
        for (LocalDate day = first; !day.isAfter(last); day = day.plusDays(1)) days.add(day);
    }

    private void updateDay(Long personId, LocalDate day) {
        Date start = day.toDate();
        Date end = day.plusDays(1).toDate();
        List<TimeWorkLog> logs = em.createQuery(
                "select t from TimeWorkLog t where t.person.id = :personId " +
                "and t.isOpened = false and t.endTime is not null " +
                "and t.startTime < :end and t.endTime > :start", TimeWorkLog.class)
                .setParameter("personId", personId).setParameter("start", start).setParameter("end", end)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultList();
        long first = Long.MAX_VALUE;
        long last = Long.MIN_VALUE;
        long workedMillis = 0;
        int periods = 0;
        for (TimeWorkLog log : logs) {
            long from = Math.max(start.getTime(), log.getStartTime().getTime());
            long to = Math.min(end.getTime(), log.getEndTime().getTime());
            if (to <= from) continue;
            workedMillis += to - from;
            periods++;
            first = Math.min(first, from);
            last = Math.max(last, to);
        }
        long minutes;
        if (em.find(Person.class, personId).getPayrollType() == PayrollTypeEnum.ACCORD) {
            minutes = first == Long.MAX_VALUE ? 0 : TimeUnit.MILLISECONDS.toMinutes(last - first);
            if (minutes > 210) minutes -= 30;
        } else {
            minutes = TimeUnit.MILLISECONDS.toMinutes(workedMillis);
            if (periods >= 3) minutes += 15;
        }
        List<ZarobkiEntry> entries = em.createQuery(
                "select z from ZarobkiEntry z where z.pickerId = :personId " +
                "and z.harvestTime >= :start and z.harvestTime < :end order by z.id", ZarobkiEntry.class)
                .setParameter("personId", personId).setParameter("start", start).setParameter("end", end)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultList();
        // A settled day must not be recalculated or receive a second time entry.
        if (entries.stream().anyMatch(entry -> Boolean.TRUE.equals(entry.payed))) return;
        ZarobkiEntry daily = null;
        for (ZarobkiEntry entry : entries) {
            // Consolidate legacy per-period minutes without changing harvest/pay data.
            if (entry.workingMinutes != null) entry.workingMinutes = 0L;
            if (daily == null && entry.rodzajId == null && entry.trolleyId == null
                    && entry.uniqId == null && entry.cycleId == null
                    && entry.export == null && entry.kraj == null && entry.inne == null
                    && entry.ilosc == null && entry.additionalMinutes == null) daily = entry;
        }
        if (daily == null) {
            if (minutes == 0 && logs.isEmpty()) return;
            daily = new ZarobkiEntry();
            daily.pickerId = personId;
            daily.harvestTime = start;
            daily.timeshort = Long.valueOf(day.toString("yyyyMM"));
            daily.payed = false;
            em.persist(daily);
        }
        daily.workingMinutes = minutes;
    }
}
