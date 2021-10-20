package com.fungisearch.fudriver.cycle.command.model;


import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.cycle.Exception.CycleStartDayAfterFirstHarvestDayException;
import com.fungisearch.fudriver.cycle.Exception.CycleStartDayBeforeFirstHarvestDayException;
import com.fungisearch.fudriver.cycle.command.repository.CycleRepository;
import com.fungisearch.fudriver.settings.command.model.Chamber;
import com.fungisearch.fudriver.settings.command.model.Mycelium;
import com.fungisearch.fudriver.settings.command.model.Subsoil;
import com.fungisearch.fudriver.user.command.model.User;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiFactory;
import org.hibernate.StaleObjectStateException;
import sun.security.util.PendingException;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "cykle")
public class Cycle extends BaseEntity {

    transient CycleRepository cycleRepository;
    transient BeanValidator beanValidator;
    transient ZarobkiFactory zarobkiFactory;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hala_id", nullable = false)
    private Chamber chamber;

    @Column(name = "zalozenie")
    @NotNull
    private Integer initDate;

    @Column(name = "poczatek")
    private Integer startFirstPeriod;

    @Column(name = "koniec_1")
    private Integer startSecondPeriod;

    @Column(name = "koniec_2")
    private Integer startThirdPeriod;

    @Column(name = "koniec")
    private Integer end;

    @Column(name = "aktywna")
    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private CycleStatusEnum cycleStatus;

    @Column(name = "ile_ton")
    private int weight;

    @Column(name = "ile_metrow")
    @NotNull
    private int area;

    @ManyToOne
    @JoinColumn(name = "kompostownia_id")
    private Subsoil subsoil;

    @ManyToOne
    @JoinColumn(name = "grzybnia_id")
    private Mycelium mycelium;

    @Column(name = "wilgotnosc")
    @NotNull
    private int humidity;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "technolog_id")
    @NotNull
    private User technologist;

    //@Version
    private int version;

    private Cycle() {
    }

    public Cycle(CycleRepository cycleRepository, BeanValidator beanValidator, ZarobkiFactory zarobkiFactory) {
        this.cycleRepository = cycleRepository;
        this.beanValidator = beanValidator;
        this.zarobkiFactory = zarobkiFactory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Chamber getChamber() {
        return chamber;
    }

    public Integer getInitDate() {
        return initDate;
    }

    public Integer getStartFirstPeriod() {
        return startFirstPeriod;
    }
    public Integer getStartSecondPeriod() {
        return startSecondPeriod;
    }

    public Integer getStartThirdPeriod() {
        return startThirdPeriod;
    }

    public Integer getEnd() {
        return end;
    }

    public CycleStatusEnum getCycleStatus() {
        return cycleStatus;
    }

    public int getWeight() {
        return weight;
    }

    public int getArea() {
        return area;
    }

    public Subsoil getSubsoil() {
        return subsoil;
    }

    public Mycelium getMycelium() {
        return mycelium;
    }

    public int getHumidity() {
        return humidity;
    }

    public String getDescription() {
        return description;
    }

    public User getTechnologist() {
        return technologist;
    }

    public int getVersion() {
        return version;
    }

    private void create() {
        beanValidator.validate(this);
        cycleRepository.save(this);
    }

    public void edit(Edit edit) {
/*
        if (edit.version != this.version) {
            throw new StaleObjectStateException(this.getClass().getName(), this.getId());
        }
*/
        if (edit.mycelium != null) {
            this.mycelium = edit.mycelium;
        }

        if (edit.subsoil != null) {
            this.subsoil = edit.subsoil;
        }

        if(edit.initDate != null){
            checkIfDayAfterFirstHarvest(edit.initDate);
            this.initDate = edit.initDate;
        }

        if (edit.startFirstPeriod != null) {
            this.startFirstPeriod = edit.startFirstPeriod;
        }

        if (edit.startSecondPeriod != null) {
            if (this.startFirstPeriod > edit.startSecondPeriod) {
                throw new IllegalStateException("Second period start day must be after the start of the whole cycle.");
            }
            this.startSecondPeriod = edit.startSecondPeriod;
        }

        if (edit.startThirdPeriod != null) {
            if (this.startSecondPeriod == null) {
                throw new IllegalStateException("No start second period date when starting third cycle period");
            }
            if (this.startSecondPeriod > edit.startThirdPeriod) {
                throw new IllegalStateException("No start second period date when starting third cycle period");
            }
            this.startThirdPeriod = edit.startThirdPeriod;
        }

        if (edit.weight != null) {
            if (edit.weight <= 0) {
                throw new IllegalStateException("Cycle weight should be greater than zero");
            }
            this.weight = edit.weight;
        }

        if (edit.area != null) {
            if (!(edit.area > 0)) {
                throw new IllegalStateException("Cycle area should be greater then zero");
            }
            this.area = edit.area;
        }

        if (edit.humidity != null) {
            if ((edit.humidity < 0) || (edit.humidity > 100)) {
                throw new IllegalStateException("Humidity should be an integer between 0 and 100");
            }
            this.humidity = edit.humidity;
        }

        if (edit.description != null) {
            this.description = edit.description;
        }

        if (edit.technoglogist != null) {
            this.technologist = edit.technoglogist;
        }
        beanValidator.validate(this);
    }


    public void close(Close close) {
/*
        if (close.version != this.version) {
            throw new StaleObjectStateException(this.getClass().getName(), this.getId());
        }
*/
        if (this.startThirdPeriod == null) {
            throw new IllegalStateException("Specify start of third period date before closing the cycle.");
        }
        if (this.startThirdPeriod > close.endDate) {
            throw new IllegalStateException("Cycle end date should be equal or grater then start of third period date.");
        }
        checkIfDayBeforeOrEqualLastHarvest(close.endDate);
        this.end = close.endDate;
        this.cycleStatus = CycleStatusEnum.CLOSED;
    }

    public static class Close {
        private int endDate;
        private int version;

        public Close endDate(int endDate) {
            this.endDate = endDate;
            return this;
        }

        public Close version(int version) {
            this.version = version;
            return this;
        }
    }

    public static class Edit {
        private Mycelium mycelium;
        private Subsoil subsoil;
        private Integer initDate;
        private Integer startFirstPeriod;
        private Integer startSecondPeriod;
        private Integer startThirdPeriod;
        private Integer weight;
        private Integer area;
        private Integer humidity;
        private String description;
        private User technoglogist;
        private int version;


        public Edit mycelium(Mycelium mycelium) {
            this.mycelium = mycelium;
            return this;
        }

        public Edit subsoil(Subsoil subsoil) {
            this.subsoil = subsoil;
            return this;
        }

        public Edit initDate(Integer initDate) {
            this.initDate = initDate;
            return this;
        }

        public Edit startFirstPeriod(Integer startFirstPeriod) {
            this.startFirstPeriod = startFirstPeriod;
            return this;
        }

        public Edit startSecondPeriod(Integer startSecondPeriod) {
            this.startSecondPeriod = startSecondPeriod;
            return this;
        }

        public Edit startThirdPeriod(Integer startThirdPeriod) {
            this.startThirdPeriod = startThirdPeriod;
            return this;
        }

        public Edit weight(Integer weight) {
            this.weight = weight;
            return this;
        }

        public Edit area(int area) {
            this.area = area;
            return this;
        }

        public Edit humidity(int humidity) {
            this.humidity = humidity;
            return this;
        }

        public Edit descripion(String description) {
            this.description = description;
            return this;
        }

        public Edit technologis(User technoglogist) {
            this.technoglogist = technoglogist;
            return this;
        }

        public Edit version(int version) {
            this.version = version;
            return this;
        }
    }


    public static class CycleBuilder {
        private CycleRepository cycleRepository;
        private BeanValidator beanValidator;
        private ZarobkiFactory zarobkiFactory;
        private Chamber chamber;
        private Integer initDate;
        private Integer startFirstPeriod;
        private Integer startSecondPeriod;
        private Integer startThirdPeriod;
        private int weight;
        private int area;
        private Subsoil subsoil;
        private Mycelium mycelium;
        private int humidity;
        private String description;
        private User technologist;

        public CycleBuilder(CycleRepository cycleRepository, BeanValidator beanValidator, ZarobkiFactory zarobkiFactory) {
            this.cycleRepository = cycleRepository;
            this.beanValidator = beanValidator;
            this.zarobkiFactory = zarobkiFactory;
        }

        public CycleBuilder chamber(Chamber chamber) {
            this.chamber = chamber;
            return this;
        }

        public CycleBuilder initDate(Integer initDate) {
            this.initDate = initDate;
            return this;
        }

        public CycleBuilder startFirstPeriod(Integer startFirstPeriod) {
            this.startFirstPeriod = startFirstPeriod;
            return this;
        }

        public CycleBuilder startSecondPeriod(Integer startSecondPeriod) {
            this.startSecondPeriod = startSecondPeriod;
            return this;
        }

        public CycleBuilder startThirdPeriod(Integer startThirdPeriod) {
            this.startThirdPeriod = startThirdPeriod;
            return this;
        }


        public CycleBuilder weight(int weight) {
            this.weight = weight;
            return this;
        }

        public CycleBuilder area(int area) {
            this.area = area;
            return this;
        }

        public CycleBuilder subsoil(Subsoil subsoil) {
            this.subsoil = subsoil;
            return this;
        }

        public CycleBuilder mycelium(Mycelium mycelium) {
            this.mycelium = mycelium;
            return this;
        }

        public CycleBuilder humidity(int humidity) {
            this.humidity = humidity;
            return this;
        }

        public CycleBuilder description(String description) {
            this.description = description;
            return this;
        }

        public CycleBuilder technologist(User technologist) {
            this.technologist = technologist;
            return this;
        }

        public Cycle build() {
            Cycle cycle = new Cycle(cycleRepository, beanValidator, zarobkiFactory);
            cycle.chamber = chamber;
            cycle.mycelium = mycelium;
            cycle.subsoil = subsoil;
            cycle.initDate = initDate;
            cycle.startFirstPeriod = startFirstPeriod;
            cycle.startSecondPeriod = startSecondPeriod;
            cycle.startThirdPeriod = startThirdPeriod;
            cycle.cycleStatus = CycleStatusEnum.ACTIVE;
            if (!(area > 0)) {
                cycle.area = chamber.getArea();
            } else {
                cycle.area = area;
            }
            if (!((humidity > 0) && (humidity <= 100))) {
                cycle.humidity = 20;
            } else {
                cycle.humidity = humidity;
            }
            cycle.weight = weight;
            cycle.description = description;
            cycle.technologist = technologist;
            cycle.create();
            return cycle;
        }
    }

    private void checkIfDayAfterFirstHarvest(int day) {
        Integer firstHarvestDay = zarobkiFactory.findMinDateForCycle(this.getId());
        if ((firstHarvestDay != null) && (day > firstHarvestDay)) {
            throw new CycleStartDayAfterFirstHarvestDayException(this.getId(),"FirstCycleDayAfterHarvest",firstHarvestDay);
        }
    }

    private void checkIfDayBeforeOrEqualLastHarvest(int day) {
        Integer lastHarvestDay = zarobkiFactory.findMaxDateForCycle(this.getId());
        if ((lastHarvestDay != null) && (day <= lastHarvestDay)) {
            throw new CycleStartDayBeforeFirstHarvestDayException();
        }
    }
}
