package com.fungisearch.fudriver.reclassification.command.model;

import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by marcin on 03.02.16.
 */
@Entity
@Table(name="skup_reclassification")
public class ReclassificationSkup {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="remote_id")
    @NotNull
    private Long remoteId;

    @Column(name="message")
    private String description;


    @Column(name="created")
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date created;

    @Column(name = "processed", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @NotNull
    private Boolean processed;

    @Column(name="processing_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date processingDate;

    @NotNull
    @Column(name="user_id")
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(Long remoteId) {
        this.remoteId = remoteId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Boolean isProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public Date getProcessingDate() {
        return processingDate;
    }

    public void setProcessingDate(Date processingDate) {
        this.processingDate = processingDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
