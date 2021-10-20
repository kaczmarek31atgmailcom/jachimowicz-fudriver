package com.fungisearch.fudriver.payroll.wage.command.model;

import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;
import com.fungisearch.fudriver.payroll.wage.command.repository.WageRepository;
import com.fungisearch.fudriver.type.command.model.Type;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Detale stawki
 */
@Entity
@Table(name = "wage")
public class Wage {

    private transient WageRepository wageRepository;
    private transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private Type type;

    @Column(name = "salary_day_type")
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private SalaryDayTypeEnum dayType;

    @Column(name = "value")
    @NotNull
    private Long value;

    @ManyToOne
    @JoinColumn(name = "header_id", nullable = false)
    private WageHeader wageHeader;


    private Wage() {}

    public  Wage(WageRepository wageRepository, BeanValidator beanValidator) {
        this.wageRepository = wageRepository;
        this.beanValidator = beanValidator;
    }

    public void setWageRepository(WageRepository wageRepository) {
        this.wageRepository = wageRepository;
    }
    public void setBeanValidator(BeanValidator beanValidator) {
        this.beanValidator = beanValidator;
    }

    public Long getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public SalaryDayTypeEnum getDayType() {
        return dayType;
    }

    public Long getValue() {
        return value;
    }


    public void updateValue(long value){
        if(!(value >= 0)){
            throw new IllegalStateException("Wage value below zero");
        }
        this.value = value;
    }

    private void create() {
        beanValidator.validate(this);
        wageRepository.save(this);
    }

    public static class WageBuilder {
        private WageRepository wageRepository;
        private BeanValidator beanValidator;
        private Type type;
        private WageHeader wageHeader;
        private SalaryDayTypeEnum dayType;
        private long value;

        public WageBuilder(WageRepository wageRepository, BeanValidator beanValidator) {
            this.wageRepository = wageRepository;
            this.beanValidator = beanValidator;
        }


        public WageBuilder dayType(SalaryDayTypeEnum dayType) {
            this.dayType = dayType;
            return this;
        }

        public WageBuilder type(Type type){
            this.type = type;
            return this;
        }

        public WageBuilder wageHeader(WageHeader wageHeader){
            this.wageHeader = wageHeader;
            return this;
        }

        public WageBuilder value(long value) {
            this.value = value;
            return this;
        }

        public Wage build() {
            Wage wage = new Wage(this.wageRepository, this.beanValidator);
            wage.dayType = this.dayType;
            wage.value = this.value;
            wage.type = type;
            wage.wageHeader = wageHeader;
            wage.create();
            return wage;
        }
    }
}