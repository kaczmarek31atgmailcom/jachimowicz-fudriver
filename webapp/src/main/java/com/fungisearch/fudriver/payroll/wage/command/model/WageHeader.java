package com.fungisearch.fudriver.payroll.wage.command.model;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;
import com.fungisearch.fudriver.payroll.wage.command.repository.WageRepository;
import com.fungisearch.fudriver.type.command.model.Type;
import com.fungisearch.fudriver.type.command.repository.TypeRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Kolekcja stawek przeznaczona do przypisywania do pracownika. W ten sposób różni pracownicy mogą mieć różne stawki za te same grzyby.
 */
@Entity
@Table(name = "wage_header")
public class WageHeader extends BaseEntity {

    public transient WageRepository wageRepository;
    public transient BeanValidator beanValidator;
    public transient TypeRepository typeRepository;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "active")
    boolean isActive;

    @OneToMany
    @JoinColumn(name = "header_id")
    private List<Wage> wages;

    private WageHeader() {
    }

    public WageHeader(WageRepository wageRepository, BeanValidator beanValidator, TypeRepository typeRepository) {
        this.wageRepository = wageRepository;
        this.beanValidator = beanValidator;
        this.typeRepository = typeRepository;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Wage> getWages() {
        return wages;
    }

    public boolean isActive() {
        return isActive;
    }

    private void create() {
        beanValidator.validate(this);
        wageRepository.save(this);
    }

    public Wage getRegularWage(Type type) {
        List<Wage> results = this.wages.stream()
                .filter(w -> w.getType().equals(type))
                .filter(w -> w.getDayType().equals(SalaryDayTypeEnum.REGULAR_DAY))
                .collect(Collectors.toList());
        if(results.size() != 1){
            throw new IllegalStateException("There is no exact single Regular Day Wage for given type and typHeader");
        }
        return results.get(0);
    }

    public Wage getSundayWage(Type type) {
        List<Wage> results = this.wages.stream()
                .filter(w -> w.getType().equals(type))
                .filter(w -> w.getDayType().equals(SalaryDayTypeEnum.SUNDAY))
                .collect(Collectors.toList());
        if(results.size() != 1){
            throw new IllegalStateException("There is no exact single Sunday Wage for given type and typHeader");
        }
        return results.get(0);
    }

    public Wage getBonusWage(Type type) {
        List<Wage> results = this.wages.stream()
                .filter(w -> w.getType().equals(type))
                .filter(w -> w.getDayType().equals(SalaryDayTypeEnum.BONUS_DAY))
                .collect(Collectors.toList());
        if(results.size() != 1){
            throw new IllegalStateException("There is no exact single Bonus Day Wage for given type and typHeader");
        }
        return results.get(0);
    }


    public static class WageHeaderBuilder {
        private WageRepository wageRepository;
        private BeanValidator beanValidator;
        private TypeRepository typeRepository;
        private String name;

        public WageHeaderBuilder(WageRepository wageRepository, BeanValidator beanValidator, TypeRepository typeRepository) {
            this.wageRepository = wageRepository;
            this.beanValidator = beanValidator;
            this.typeRepository = typeRepository;
        }

        public WageHeaderBuilder name(String name) {
            this.name = name;
            return this;
        }

        public WageHeader build() {

            WageHeader wageHeader = new WageHeader(wageRepository, beanValidator, typeRepository);
            wageHeader.name = this.name;
            wageHeader.isActive = true;
            List<Type> types = new ArrayList<>();
            types.addAll(getAllTypes());
            wageHeader.create();
            wageHeader.wages = new ArrayList<>();
            wageHeader.wages.addAll(createWages(types, SalaryDayTypeEnum.REGULAR_DAY, wageHeader));
            wageHeader.wages.addAll(createWages(types, SalaryDayTypeEnum.SUNDAY, wageHeader));
            wageHeader.wages.addAll(createWages(types, SalaryDayTypeEnum.BONUS_DAY, wageHeader));
            return wageHeader;
        }

        private List<Type> getAllTypes() {
            List<Type> result = typeRepository.findAll();
            if (result.isEmpty()) {
                return result;
            } else {
                result.stream()
                        .map(t -> (setRepositoryAndValidator(t)))
                        .collect(Collectors.toList());
                return (result.isEmpty()) ? new ArrayList<>() : result;
            }
        }

        private Type setRepositoryAndValidator(Type type) {
            type.beanValidator = beanValidator;
            type.typeRepository = typeRepository;
            return type;
        }

        private List<Wage> createWages(List<Type> types, SalaryDayTypeEnum dayType, WageHeader wageHeader) {
            return types.stream()
                    .map(t -> new Wage.WageBuilder(wageRepository, beanValidator)
                            .dayType(dayType)
                            .type(t)
                            .wageHeader(wageHeader)
                            .value(0L)
                            .build())
                    .collect(Collectors.toList());
        }
    }
}