package com.fungisearch.fudriver.warehouseEastMushrooms.command.model.reclassification;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.repository.EastMushroomsWarehouseRepository;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "east_mushrooms_reclassification_detail")
public class ReclassificationDetail extends BaseEntity {

    transient EastMushroomsWarehouseRepository warehouseRepository;
    transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "header_id")
    @NotNull
    private ReclassificationHeader reclassificationHeader;

    @Column(name = "picker_id")
    @NotNull
    private Long pickerId;

    @Column(name = "uniq_id")
    @NotNull
    private Long uniqId;

    @Column(name = "harvest_date")
    @NotNull
    private Date harvestDate;

    @Column(name = "source_type_id")
    @NotNull
    private Long sourceTypeId;

    @Column(name = "source_type_name")
    @NotNull
    private String sourceTypeName;

    @Column(name = "source_type_weight")
    @NotNull
    private Double sourceTypeWeight;

    @Column(name = "target_type_id")
    @NotNull
    private Long targetTypeId;

    @Column(name = "target_type_name")
    @NotNull
    private String targetTypeName;

    @Column(name = "target_type_weight")
    @NotNull
    private Double targetTypeWeight;

    private ReclassificationDetail(){}

    public ReclassificationDetail(EastMushroomsWarehouseRepository warehouseRepository, BeanValidator beanValidator) {
        this.warehouseRepository = warehouseRepository;
        this.beanValidator = beanValidator;
    }

    public void save(){
        beanValidator.validate(this);
        warehouseRepository.save(this);
    }

    public static class ReclassificationDetailBuilder {
        private final EastMushroomsWarehouseRepository warehouseRepository;
        private final BeanValidator beanValidator;
        private ReclassificationHeader reclassificationHeader;
        private Long pickerId;
        private Long uniqId;
        private Date harvestDate;
        private Long sourceTypeId;
        private String sourceTypeName;
        private Double sourceTypeWeight;
        private Long targetTypeId;
        private String targetTypeName;
        private Double targetTypeWeight;

        public ReclassificationDetailBuilder(EastMushroomsWarehouseRepository warehouseRepository, BeanValidator beanValidator) {
            this.warehouseRepository = warehouseRepository;
            this.beanValidator = beanValidator;
        }

        public ReclassificationDetailBuilder header(ReclassificationHeader header){
            this.reclassificationHeader = header;
            return this;
        }

        public ReclassificationDetailBuilder pickerId(long pickerId){
            this.pickerId = pickerId;
            return this;
        }

        public ReclassificationDetailBuilder uniqId(long uniqId){
            this.uniqId = uniqId;
            return this;
        }

        public ReclassificationDetailBuilder harvestDate(Date harvestDate){
            this.harvestDate = harvestDate;
            return this;
        }

        public ReclassificationDetailBuilder sourceTypeId (long sourceTypeId){
            this.sourceTypeId = sourceTypeId;
            return this;
        }

        public ReclassificationDetailBuilder sourceTypeName(String sourceTypeName){
            this.sourceTypeName = sourceTypeName;
            return this;
        }

        public ReclassificationDetailBuilder sourceTypeWeight(double sourceTypeWeight){
            this.sourceTypeWeight = sourceTypeWeight;
            return this;
        }

        public ReclassificationDetailBuilder targetTypeId (long targetTypeId){
            this.targetTypeId = targetTypeId;
            return this;
        }

        public ReclassificationDetailBuilder targetTypeName(String tagetTypeName){
            this.targetTypeName = tagetTypeName;
            return this;
        }

        public ReclassificationDetailBuilder targetTypeWeight(double targetTypeWeight){
            this.targetTypeWeight = targetTypeWeight;
            return this;
        }

        public ReclassificationDetail build(){
            ReclassificationDetail detail = new ReclassificationDetail(warehouseRepository,beanValidator);
            detail.reclassificationHeader = reclassificationHeader;
            detail.pickerId = pickerId;
            detail.uniqId = uniqId;
            detail.harvestDate = harvestDate;
            detail.sourceTypeId = sourceTypeId;
            detail.sourceTypeName = sourceTypeName;
            detail.sourceTypeWeight = sourceTypeWeight;
            detail.targetTypeId = targetTypeId;
            detail.targetTypeName = targetTypeName;
            detail.targetTypeWeight =  targetTypeWeight;
            detail.save();
            reclassificationHeader.getDetails().add(detail);
            return detail;
        }
    }
}
