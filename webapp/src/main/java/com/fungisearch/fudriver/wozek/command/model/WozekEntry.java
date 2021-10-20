package com.fungisearch.fudriver.wozek.command.model;

import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.wozek.command.repository.WozekRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by marcin on 23.02.16.
 */
@Entity
@Table(name = "wozek")
public class WozekEntry {

    @Autowired
    private transient WozekRepository wozekRepository;

    @Autowired
    private transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "nr_rwaczki")
    @NotNull
    private Long pickerId;

    @Column(name = "rodzaj_id")
    @NotNull
    private Long rodzajId;

    @Column(name = "time")
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date time;

    @Column(name = "hala")
    @NotNull
    private Long cykleId;

    @Column(name = "wozek_nr")
    private Long wozekNr;

    @Column(name = "przestrzen_id")
    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private WozekStatus wozekStatus;

    @Column(name = "user_id")
    @NotNull
    private Long wagowyId;

    @Column(name = "uniq_id")
    private Long uniqId;

    @Column(name = "jakosciowiec_id")
    private Long jakoscowiecId;

    @Column(name = "test_jakosci")
    @Enumerated(EnumType.ORDINAL)
    private QualityStatus qualityStatus = QualityStatus.NOT_CHECKED;

    @Column(name = "brygadzista_id")
    private Long brygadzistaId;

    @Column(name = "wozkowy_id")
    private Integer wozkowyId;


    @SuppressWarnings("unused")
    private WozekEntry() {
    }

    public WozekEntry(WozekRepository wozekRepository, BeanValidator beanValidator) {
        this.wozekRepository = wozekRepository;
        this.beanValidator = beanValidator;
    }

    public Long getPickerId() {
        return pickerId;
    }

    public Long getUniqId() {
        return uniqId;
    }

    public void setWozekRepository(WozekRepository wozekRepository) {
        this.wozekRepository = wozekRepository;
    }

    public void setBeanValidator(BeanValidator beanValidator) {
        this.beanValidator = beanValidator;
    }

    public Long getId() {
        return id;
    }

    public Long getRodzajId() {
        return rodzajId;
    }

    public Date getTime() {
        return time;
    }

    public Long getCykleId() {
        return cykleId;
    }

    public Long getWozekNr() {
        return wozekNr;
    }

    public WozekStatus getWozekStatus() {
        return wozekStatus;
    }

    public Long getWagowyId() {
        return wagowyId;
    }

    public Long getJakoscowiecId() {
        return jakoscowiecId;
    }

    public QualityStatus getQualityStatus() {
        return qualityStatus;
    }

    public Long getBrygadzistaId() {
        return brygadzistaId;
    }

    public Integer getWozkowyId() {
        return wozkowyId;
    }

    public Long create()  {
        beanValidator.validate(this);
        wozekRepository.save(this);
        return this.id;
    }

    public void delete(){
        wozekRepository.delete(this);
    }

    public void reject(){
        this.wozekStatus = WozekStatus.ODRZUCONY;
        beanValidator.validate(this);
    }

    public void askForAcceptance(){
        this.wozekStatus = WozekStatus.NADANY_NUMER_WOZKA;
        beanValidator.validate(this);
    }

    public void assignToWarehousePalette(){
        if(this.wozekStatus != WozekStatus.NADANY_NUMER_WOZKA){
            throw new IllegalStateException("Can not assign wozek entity that is already assigned");
        }
        this.wozekStatus = WozekStatus.PRZYPISANY_DO_PALETY_MAGAZYNOWEJ;
        beanValidator.validate(this);
    }

    public static class WozekEntryBuilder {
        private BeanValidator beanValidator;
        private WozekRepository wozekRepository;
        private Long id;
        private Long pickerId;
        private Long rodzajId;
        private Long cyckleId;
        private Long uniqId;
        private Long brygadzistaId;
        private Long jakoscowiecId;
        private Integer wozkowyId;
        private Long wagowyId;
        private WozekStatus wozekStatus;
        private QualityStatus qualityStatus = QualityStatus.NOT_CHECKED;

        public WozekEntryBuilder(WozekRepository wozekRepository, BeanValidator beanValidator) {
            this.wozekRepository = wozekRepository;
            this.beanValidator = beanValidator;
        }

        public WozekEntryBuilder id(Long id){
            this.id = id;
            return this;
        }

        public WozekEntryBuilder pickerId(Long pickerId) {
            this.pickerId = pickerId;
            return this;
        }

        public WozekEntryBuilder rodzajId(Long rodzajId) {
            this.rodzajId = rodzajId;
            return this;
        }

        public WozekEntryBuilder cykleId(Long cyckleId) {
            this.cyckleId = cyckleId;
            return this;
        }

        public WozekEntryBuilder uniqId(Long uniqId) {
            this.uniqId = uniqId;
            return this;
        }

        public WozekEntryBuilder brygadzistaId(Long brygadzistaId) {
            this.brygadzistaId = brygadzistaId;
            return this;
        }
        public WozekEntryBuilder jakoscowiecId(Long jakoscowiecId) {
            this.jakoscowiecId = jakoscowiecId;
            return this;
        }

        public WozekEntryBuilder wozkowyId(Integer wozkowyId) {
            this.wozkowyId = wozkowyId;
            return this;
        }

        public WozekEntryBuilder wagowyId(Long wagowyId){
            this.wagowyId = wagowyId;
            return this;
        }

        public WozekEntryBuilder wozekStatus(WozekStatus wozekStatus){
            this.wozekStatus = wozekStatus;
            return this;
        }

        public WozekEntryBuilder qualityStatus(QualityStatus qualityStatus){
            this.qualityStatus = qualityStatus;
            return this;
        }

        public WozekEntry build() {
            WozekEntry wozekEntry = new WozekEntry(this.wozekRepository, this.beanValidator);
            wozekEntry.pickerId = this.pickerId;
            wozekEntry.rodzajId = this.rodzajId;
            wozekEntry.cykleId = this.cyckleId;
            wozekEntry.uniqId = this.uniqId;
            wozekEntry.brygadzistaId = this.brygadzistaId;
            wozekEntry.jakoscowiecId = this.jakoscowiecId;
            wozekEntry.wozkowyId = this.wozkowyId;
            wozekEntry.wagowyId = this.wagowyId;
            wozekEntry.wozekStatus = this.wozekStatus;
            wozekEntry.qualityStatus = this.qualityStatus;
            wozekEntry.time = new Date();
            return wozekEntry;
        }

    }
}
