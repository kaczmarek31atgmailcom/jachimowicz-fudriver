package com.fungisearch.fudriver.audit.command.model;

import com.fungisearch.fudriver.audit.command.repository.AuditRepository;
import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by marcin on 03.08.16.
 */
@Entity
@Table(name = "local_reclassification_log")
public class LocalReclassificationLog extends BaseEntity{

    public transient AuditRepository auditRepository;
    public transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "date")
    @NotNull
    private Date date;

    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "picker_id")
    private Long pickerId;

    @Column(name = "uniq_id")
    private Long uniqId;

    @Column(name = "user_id")
    @NotNull
    private Long userId;

    @Column(name = "login")
    private String login;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "reason_id")
    private Long reasonId;

    @Column(name = "reason_text")
    private String reasonText;

    @Column(name = "source_type_id")
    private Long sourceTypeId;

    @Column(name = "source_type_name")
    private String sourceTypeName;

    @Column(name = "source_type_weight")
    private Double sourceTypeWeight;

    @Column(name = "target_type_id")
    private Long targetTypeId;

    @Column(name = "target_type_name")
    private String targetTypeName;

    @Column(name = "target_type_weight")
    private Double targetTypeWeight;

    @Column(name = "source_cycle_id")
    private Long sourceCycleId;

    @Column(name = "source_chamber_name")
    private String sourceChamberName;

    @Column(name = "target_cycle_id")
    private Long targetCycleId;

    @Column(name = "target_chamber_name")
    private String targetChamberName;

    @Column(name = "warehouse_only")
    private boolean warehouseOnly;

    private LocalReclassificationLog() {
    }

    public LocalReclassificationLog(AuditRepository auditRepository, BeanValidator beanValidator) {
        this.auditRepository = auditRepository;
        this.beanValidator = beanValidator;
    }

    public void save(){
        beanValidator.validate(this);
        auditRepository.saveLocalReclassificationLog(this);
    }



    public static class LocalReclassificationLogBuilder {
        private AuditRepository auditRepository;
        private BeanValidator beanValidator;
        private Date date;
        private Long supplierId;
        private Long pickerId;
        private Long uniqId;
        private Long userId;
        private String login;
        private String name;
        private String surname;
        private Long reasonId;
        private String reasonText;
        private Long sourceTypeId;
        private String sourceTypeName;
        private Double sourceTypeWeight;
        private Long targetTypeId;
        private String targetTypeName;
        private Double targetTypeWeight;
        private Long sourceCycleId;
        private String sourceChamberName;
        private Long targetCycleId;
        private String targetChamberName;
        private boolean warehouseOnly;

        LocalReclassificationLogBuilder(AuditRepository auditRepository, BeanValidator beanValidator){
            this.auditRepository = auditRepository;
            this.beanValidator = beanValidator;
        }

        public LocalReclassificationLogBuilder date(Date date){
            this.date = date;
            return this;
        }

        public LocalReclassificationLogBuilder supplierId(Long supplierId){
            this.supplierId = supplierId;
            return this;
        }

        public LocalReclassificationLogBuilder pickerId(Long pickerId){
            this.pickerId = pickerId;
            return this;
        }

        public LocalReclassificationLogBuilder uniqId(Long uniqId){
            this.uniqId = uniqId;
            return this;
        }

        public LocalReclassificationLogBuilder userId(Long userId){
            this.userId = userId;
            return this;
        }

        public LocalReclassificationLogBuilder login(String login){
            this.login = login;
            return this;
        }

        public LocalReclassificationLogBuilder name(String name){
            this.name = name;
            return this;
        }

        public LocalReclassificationLogBuilder surname(String surname){
            this.surname = surname;
            return this;
        }

        public LocalReclassificationLogBuilder reasonId(Long reasonId){
            this.reasonId = reasonId;
            return this;
        }

        public LocalReclassificationLogBuilder reasonText(String reasonText){
            this.reasonText = reasonText;
            return this;
        }

        public LocalReclassificationLogBuilder sourceTypeId(Long sourceTypeId){
            this.sourceTypeId = sourceTypeId;
            return this;
        }

        public LocalReclassificationLogBuilder sourceTypename(String sourceTypeName){
            this.sourceTypeName = sourceTypeName;
            return this;
        }

        public LocalReclassificationLogBuilder sourceTypeWeight(Double sourceTypeWeight){
            this.sourceTypeWeight = sourceTypeWeight;
            return this;
        }

        public LocalReclassificationLogBuilder targetTypeId(Long targetTypeId){
            this.targetTypeId = targetTypeId;
            return this;
        }

        public LocalReclassificationLogBuilder targetTypeName(String targetTypeName){
            this.targetTypeName = targetTypeName;
            return this;
        }

        public LocalReclassificationLogBuilder targetTypeWeight(Double targetTypeWeight){
            this.targetTypeWeight = targetTypeWeight;
            return this;
        }

        public LocalReclassificationLogBuilder sourceCycleId(Long sourceCycleId){
            this.sourceCycleId = sourceCycleId;
            return this;
        }

        public LocalReclassificationLogBuilder sourceChamberName(String sourceChamberName){
            this.sourceChamberName = sourceChamberName;
            return this;
        }

        public LocalReclassificationLogBuilder targetCycleId(Long targetCycleId){
            this.targetCycleId = targetCycleId;
            return this;
        }

        public LocalReclassificationLogBuilder targetChamberName(String targetChamberName){
            this.targetChamberName = targetChamberName;
            return this;
        }

        public LocalReclassificationLogBuilder warehouseOnly(boolean warehouseOnly){
            this.warehouseOnly = warehouseOnly;
            return this;
        }

        public LocalReclassificationLog build(){
            LocalReclassificationLog localReclassificationLog = new LocalReclassificationLog(this.auditRepository,this.beanValidator);
            localReclassificationLog.date = this.date;
            localReclassificationLog.supplierId = this.supplierId;
            localReclassificationLog.pickerId = this.pickerId;
            localReclassificationLog.uniqId = this.uniqId;
            localReclassificationLog.userId = this.userId;
            localReclassificationLog.login = this.login;
            localReclassificationLog.name = this.name;
            localReclassificationLog.surname = this.surname;
            localReclassificationLog.reasonId = this.reasonId;
            localReclassificationLog.reasonText = this.reasonText;
            localReclassificationLog.sourceTypeId = this.sourceTypeId;
            localReclassificationLog.sourceTypeName = this.sourceTypeName;
            localReclassificationLog.sourceTypeWeight = this.sourceTypeWeight;
            localReclassificationLog.targetTypeId = this.targetTypeId;
            localReclassificationLog.targetTypeName = this.targetTypeName;
            localReclassificationLog.targetTypeWeight = this.targetTypeWeight;
            localReclassificationLog.sourceCycleId = this.sourceCycleId;
            localReclassificationLog.sourceChamberName = this.sourceChamberName;
            localReclassificationLog.targetCycleId = this.targetCycleId;
            localReclassificationLog.targetChamberName = this.targetChamberName;
            localReclassificationLog.warehouseOnly = this.warehouseOnly;
            localReclassificationLog.save();
            return localReclassificationLog;
        }
    }
}
