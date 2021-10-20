package com.fungisearch.fudriver.zarobki.command.model;

import com.fungisearch.fudriver.type.command.model.ExportType;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.wozek.command.model.QualityStatus;
import com.fungisearch.fudriver.zarobki.command.repository.ZarobkiRepository;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by marcin on 17.02.16.
 */
@Entity
@Table(name="zarobki")
public class ZarobkiEntry {

    @Autowired
    private transient ZarobkiRepository zarobkiRepository;

    @Autowired
    private transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    public Long id;

    @Column(name="ludzie_id")
    @NotNull
    public Long pickerId;

    @Column(name="export")
    public Double export;

    @Column(name="kraj")
    public Double kraj;

    @Column(name="inne")
    public Double inne;

    @Column(name="godziny_pracy")
    public Long workingMinutes;

    @Column(name="godziny_dodatkowe")
    public Long additionalMinutes;

    @Column(name="hala_id")
    public Long cycleId;

    @Column(name="time")
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date harvestTime;

    @Column(name="rodzaj_id")
    public Long rodzajId;

    @Column(name="wozek_nr")
    public Long trolleyId;

    @Column(name = "zaplacono", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    public Boolean payed;

    @Column(name="timeshort")
    @NotNull
    public Long timeshort;

    @Column(name="user_id")
    public Long userId;

    @Column(name="uniq_id")
    public Long uniqId;

    @Column(name="ilosc")
    public Double ilosc;

    @Column(name="brygadzista_id")
    public Long leaderId;

    @Column(name="wozkowy_id")
    public Integer trolleyManId;

    @Column(name="jakoscowiec_id")
    public Long qualityManId;

    @Column(name="test_jakosci")
    public Integer qualityCheckStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRodzajId() {
        return rodzajId;
    }

    public void setRodzajId(Long rodzajId) {
        this.rodzajId = rodzajId;
    }

    public Double getExport() {
        return export;
    }

    public void setExport(Double export) {
        this.export = export;
    }

    public Double getKraj() {
        return kraj;
    }

    public void setKraj(Double kraj) {
        this.kraj = kraj;
    }

    public Double getInne() {
        return inne;
    }

    public void setInne(Double inne) {
        this.inne = inne;
    }

    public Double getIlosc() {
        return ilosc;
    }

    public void setIlosc(Double ilosc) {
        this.ilosc = ilosc;
    }

    public Long getPickerId() {
        return pickerId;
    }

    public void setPickerId(Long pickerId) {
        this.pickerId = pickerId;
    }

    public Long getUniqId() {
        return uniqId;
    }

    public void setUniqId(Long uniqId) {
        this.uniqId = uniqId;
    }

    public Long getCycleId() {
        return cycleId;
    }

    public Long getWorkingMinutes() {
        return workingMinutes;
    }

    public Long getAdditionalMinutes() {
        return additionalMinutes;
    }

    public Long getTrolleyId() {
        return trolleyId;
    }

    public Boolean getPayed() {
        return payed;
    }

    public Long getTimeshort() {
        return timeshort;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getLeaderId() {
        return leaderId;
    }

    public Integer getTrolleyManId() {
        return trolleyManId;
    }

    public Long getQualityManId() {
        return qualityManId;
    }

    public Integer getQualityCheckStatus() {
        return qualityCheckStatus;
    }

    public void setZarobkiRepository(ZarobkiRepository zarobkiRepository) {
        this.zarobkiRepository = zarobkiRepository;
    }

    public Date getHarvestTime() {
        return harvestTime;
    }

    public void setBeanValidator(BeanValidator beanValidator) {
        this.beanValidator = beanValidator;
    }

    public ZarobkiEntry(){}

    public ZarobkiEntry(ZarobkiRepository zarobkiRepository, BeanValidator beanValidator){
        this.zarobkiRepository = zarobkiRepository;
        this.beanValidator = beanValidator;
    }

    public void setPayed(){
        this.payed = true;
        beanValidator.validate(this);
    }

    public Long create(){
        beanValidator.validate(this);
        Long id = zarobkiRepository.save(this);
        return id;
    }

    public void delete(){
        zarobkiRepository.delete(this);
    }

    public void reclassify(Long rodzajId, Long cycleId, double weight, ExportType exportType, Integer trolleyManId){
        if(rodzajId != null){
            this.rodzajId = rodzajId;
        }
        if(cycleId != null) {
            this.cycleId = cycleId;
        }
        if(trolleyManId != null){
            this.trolleyManId = trolleyManId;
        }
        this.export = 0.0;
        this.kraj = 0.0;
        this.inne = 0.0;
        this.ilosc = weight;
        if(exportType == ExportType.EXPORT){
            this.export = weight;
        }
        if(exportType == ExportType.KRAJ){
            this.kraj = weight;
        }
        if(exportType == ExportType.INNE){
            this.inne = weight;
        }
        beanValidator.validate(this);
    }

    public void failQualityTest(){
        this.qualityCheckStatus = QualityStatus.REJECTED.getIndex();
    }

    public void passQualityTest(){
        this.qualityCheckStatus = QualityStatus.ACCEPTED.getIndex();
    }

    public void reclassify(Long rodzajId, double weight, ExportType exportType, Long paletteId){
        if(rodzajId != null) {
            this.rodzajId = rodzajId;
        }
        this.trolleyId = paletteId;
        this.export = 0.0;
        this.kraj = 0.0;
        this.inne = 0.0;
        this.ilosc = weight;
        if(exportType == ExportType.EXPORT){
            this.export = weight;
        }
        if(exportType == ExportType.KRAJ){
            this.kraj = weight;
        }
        if(exportType == ExportType.INNE){
            this.inne = weight;
        }
        beanValidator.validate(this);
    }

    public void reclassify(Long rodzajId, double weight, ExportType exportType, Long paletteId, Integer trolleyManId){
        if(rodzajId != null) {
            this.rodzajId = rodzajId;
        }
        if(trolleyManId != null){
            this.trolleyManId = trolleyManId;
        }
        this.trolleyId = paletteId;
        this.export = 0.0;
        this.kraj = 0.0;
        this.inne = 0.0;
        this.ilosc = weight;
        if(exportType == ExportType.EXPORT){
            this.export = weight;
        }
        if(exportType == ExportType.KRAJ){
            this.kraj = weight;
        }
        if(exportType == ExportType.INNE){
            this.inne = weight;
        }
        beanValidator.validate(this);
    }


    public static class ZarobkiEntryBuilder{
        private ZarobkiRepository zarobkiRepository;
        private BeanValidator beanValidator;
        private Long pickerId;
        private Double export;
        private Double kraj;
        private Double inne;
        private Long workingMinutes;
        private Long additionalMinutes;
        private Long cycleId;
        private Date harvestTime;
        private Long rodzajId;
        private Boolean payed;
        private Long trolleyId;
        private Long timeshort;
        private Long userId;
        private Long uniqId;
        private Double ilosc;
        private Long leaderId;
        private Integer trolleyManId;
        private Long qualityManId;
        private Integer qualityCheckStatus;

        public ZarobkiEntryBuilder(ZarobkiRepository zarobkiRepository, BeanValidator beanValidator){
            this.zarobkiRepository = zarobkiRepository;
            this.beanValidator = beanValidator;
        }

        public ZarobkiEntryBuilder pickerId(Long pickerId){
            this.pickerId = pickerId;
            return this;
        }

        public ZarobkiEntryBuilder export(Double export){
            this.export = export;
            return this;
        }

        public ZarobkiEntryBuilder kraj(Double kraj){
            this.kraj = kraj;
            return this;
        }

        public ZarobkiEntryBuilder inne(Double inne){
            this.inne = inne;
            return this;
        }

        public ZarobkiEntryBuilder workingMinutes(Long workingMinutes){
            this.workingMinutes = workingMinutes;
            return this;
        }

        public ZarobkiEntryBuilder additionalMinutes(Long additionalMinutes){
            this.additionalMinutes = additionalMinutes;
            return this;
        }

        public ZarobkiEntryBuilder cycleId(Long cycleId){
            this.cycleId = cycleId;
            return this;
        }

        public ZarobkiEntryBuilder harvestTime(Date harvestTime){
            this.harvestTime = harvestTime;
            return this;
        }

        public ZarobkiEntryBuilder rodzajId(Long rodzajId){
            this.rodzajId = rodzajId;
            return this;
        }

        public ZarobkiEntryBuilder trolleyId(Long trolleyId){
            this.trolleyId = trolleyId;
            return this;
        }

        public ZarobkiEntryBuilder payed(Boolean payed){
            this.payed = payed;
            return this;
        }

        public ZarobkiEntryBuilder timeshort(Long timeshort){
            this.timeshort = timeshort;
            return this;
        }

        public ZarobkiEntryBuilder userId(Long userId){
            this.userId = userId;
            return this;
        }

        public ZarobkiEntryBuilder uniqId(Long uniqId){
            this.uniqId = uniqId;
            return this;
        }

        public ZarobkiEntryBuilder ilosc (Double ilosc){
            this.ilosc = ilosc;
            return this;
        }

        public ZarobkiEntryBuilder leaderId(Long leaderId){
            this.leaderId = leaderId;
            return this;
        }

        public ZarobkiEntryBuilder trollyeManId(Integer trolleyManId){
            this.trolleyManId = trolleyManId;
            return this;
        }

        public ZarobkiEntryBuilder qualityManId(Long qualityManId){
            this.qualityManId = qualityManId;
            return this;
        }

        public ZarobkiEntryBuilder qualityCheckStatus(Integer qualityCheckStatus){
            this.qualityCheckStatus = qualityCheckStatus;
            return this;
        }

        public ZarobkiEntry build(){
            ZarobkiEntry zarobkiEntry = new ZarobkiEntry(this.zarobkiRepository, this.beanValidator);
            zarobkiEntry.pickerId = this.pickerId;
            zarobkiEntry.export = this.export;
            zarobkiEntry.kraj = this.kraj;
            zarobkiEntry.inne = this.inne;
            zarobkiEntry.workingMinutes = this.workingMinutes;
            zarobkiEntry.additionalMinutes = this.additionalMinutes;
            zarobkiEntry.cycleId = this.cycleId;
            zarobkiEntry.harvestTime = this.harvestTime;
            zarobkiEntry.rodzajId = this.rodzajId;
            zarobkiEntry.payed = this.payed;
            zarobkiEntry.trolleyId = this.trolleyId;
            zarobkiEntry.timeshort = this.timeshort;
            zarobkiEntry.userId = this.userId;
            zarobkiEntry.uniqId = this.uniqId;
            zarobkiEntry.ilosc = this.ilosc;
            zarobkiEntry.leaderId = this.leaderId;
            zarobkiEntry.trolleyManId = this.trolleyManId;
            zarobkiEntry.qualityManId = this.qualityManId;
            zarobkiEntry.qualityCheckStatus = this.qualityCheckStatus;
            return zarobkiEntry;
        }
    }

}
