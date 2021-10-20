package com.fungisearch.fudriver.type.command.model;

import com.fungisearch.fudriver.box.command.model.Box;
import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;
import com.fungisearch.fudriver.payroll.wage.command.model.Wage;
import com.fungisearch.fudriver.payroll.wage.command.model.WageFactory;
import com.fungisearch.fudriver.payroll.wage.command.model.WageHeader;
import com.fungisearch.fudriver.type.command.repository.TypeRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "rodzaj")
public class Type extends BaseEntity{

    public transient TypeRepository typeRepository;
    public transient BeanValidator beanValidator;
    public transient WageFactory wageFactory;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "waga")
    @NotNull
    private Double weight;

    @Column(name = "export")
    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private ExportType exportType;

    @Column(name = "archiwalne")
    private Boolean archived;

    @OneToOne
    @JoinColumn(name = "skrzynka_id")
    private Box box;

    @OneToOne
    @JoinColumn(name = "grupa_id")
    private TypeGroup typeGroup;

    @ManyToOne
    @JoinColumn(name = "size_id")
    private TypeSize typeSize;

    @Column(name = "skup_rodzaj_id")
    private Long remoteTypeId;

    private Type() {
    }

    public Type(TypeRepository typeRepository, BeanValidator beanValidator, WageFactory wageFactory) {
        this.typeRepository = typeRepository;
        this.beanValidator = beanValidator;
        this.wageFactory = wageFactory;
    }

    public void assignRemoteType(long remoteTypeId){
        this.remoteTypeId = remoteTypeId;
    }

    public void unassignRemoteType(){
        this.remoteTypeId = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        beanValidator.validate(this);
    }

    public Double getWeight() {
        return weight;
    }

    public ExportType getExportType() {
        return exportType;
    }

    public Boolean getArchived() {
        return archived;
    }


    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
        beanValidator.validate(this);
    }

    public TypeGroup getTypeGroup() {
        return typeGroup;
    }

    public void setTypeGroup(TypeGroup typeGroup) {
        this.typeGroup = typeGroup;
        beanValidator.validate(this);
    }

    public TypeSize getTypeSize() {
        return typeSize;
    }

    public Long getRemoteTypeId() {
        return remoteTypeId;
    }

    public void setTypeSize(TypeSize typeSize) {
        this.typeSize = typeSize;
    }

    private void create() {
        beanValidator.validate(this);
        typeRepository.save(this);
    }

    public void delete(){
        this.archived = true;
        beanValidator.validate(this);
    }

    public static class TypeBuilder {
        private TypeRepository typeRepository;
        private BeanValidator beanValidator;
        private WageFactory wageFactory;
        private String name;
        private Double weight;
        private ExportType exportType;
        private Box box;
        private TypeGroup typeGroup;
        private TypeSize typeSize;

        public TypeBuilder(TypeRepository typeRepository, BeanValidator beanValidator, WageFactory wageFactory) {
            this.typeRepository = typeRepository;
            this.beanValidator = beanValidator;
            this.wageFactory = wageFactory;
        }

        public TypeBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TypeBuilder weight(long weight) {
            this.weight = (double) (weight) / 1000 ;
            return this;
        }

        public TypeBuilder exportType(ExportType exportType) {
            this.exportType = exportType;
            return this;
        }

        public TypeBuilder box(Box box) {
            this.box = box;
            return this;
        }

        public TypeBuilder typeGroup(TypeGroup typeGroup) {
            this.typeGroup = typeGroup;
            return this;
        }

        public TypeBuilder typeSize(TypeSize typeSize){
            this.typeSize = typeSize;
            return this;
        }
        public Type build() {
            Type type = new Type(typeRepository, beanValidator, wageFactory);
            type.name = name;
            type.weight = weight;
            type.exportType = exportType;
            type.box = box;
            type.archived = false;
            type.typeGroup = typeGroup;
            type.typeSize = typeSize;
            type.create();
            createTypeWages(type);
            return type;
        }
        void createTypeWages(Type type) {
            List<WageHeader> headers = wageFactory.getAllHeaders();
            headers.forEach(h -> h.getWages().add(createWage(type, SalaryDayTypeEnum.REGULAR_DAY,h)));
            headers.forEach(h -> h.getWages().add(createWage(type, SalaryDayTypeEnum.SUNDAY,h)));
            headers.forEach(h -> h.getWages().add(createWage(type, SalaryDayTypeEnum.BONUS_DAY,h)));
        }

        Wage createWage(Type type, SalaryDayTypeEnum salaryDayType,WageHeader wageHeader) {
            return wageFactory
                    .builder()
                    .type(type)
                    .dayType(salaryDayType)
                    .wageHeader(wageHeader)
                    .value(0L).build();
        }
    }
}


