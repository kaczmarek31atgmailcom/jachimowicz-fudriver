package com.fungisearch.fudriver.reclassification.command.model;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by marcin on 03.02.16.
 */
@Entity
@Table(name="skup_reclassification_details")
public class ReclassificationDetailSkup {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="reclassification_id")
    @NotNull
    private Long reclassificationId;

    @Column(name="barcode")
    @NotEmpty
    private String barcode;

    @Column(name="remote_rodzaj_id")
    @NotNull
    private Long remoteRodzajId;

    @Column(name="picker_id")
    @NotNull
    private Long pickerId;

    @Column(name="uniq_id")
    @NotNull
    private Long uniqId;

    @Column(name="local_rodzaj_id")
    private Long localRodzajId;

    @Column(name = "active", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean active;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReclassificationId() {
        return reclassificationId;
    }

    public void setReclassificationId(Long reclassificationId) {
        this.reclassificationId = reclassificationId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Long getRemoteRodzajId() {
        return remoteRodzajId;
    }

    public void setRemoteRodzajId(Long remoteRodzajId) {
        this.remoteRodzajId = remoteRodzajId;
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

    public Long getLocalRodzajId() {
        return localRodzajId;
    }

    public void setLocalRodzajId(Long localRodzajId) {
        this.localRodzajId = localRodzajId;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}

