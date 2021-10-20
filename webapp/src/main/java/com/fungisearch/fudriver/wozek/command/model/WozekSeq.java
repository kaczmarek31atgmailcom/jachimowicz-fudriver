package com.fungisearch.fudriver.wozek.command.model;

import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.wozek.command.repository.UniqRepository;
import com.fungisearch.fudriver.wozek.command.repository.WozekSeqRepository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by idea on 12.03.16.
 */
@Entity
@Table(name="wozek_seq")
public class WozekSeq {

    private transient WozekSeqRepository wozekSeqRepository;
    private transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }

    public Long create(){
        return wozekSeqRepository.create(this);
    }

    private WozekSeq(){}
    public WozekSeq(WozekSeqRepository wozekSeqRepository, BeanValidator beanValidator){
        this.wozekSeqRepository = wozekSeqRepository;
        this.beanValidator = beanValidator;
    }


    public static class WozekSeqBuilder {
        private WozekSeqRepository wozekSeqRepository;
        private BeanValidator beanValidator;

        public WozekSeqBuilder(WozekSeqRepository wozekSeqRepository, BeanValidator beanValidator){
            this.wozekSeqRepository = wozekSeqRepository;
            this.beanValidator = beanValidator;
        }

        public WozekSeq build(){
            WozekSeq wozekSeq = new WozekSeq(this.wozekSeqRepository, this.beanValidator);
            return wozekSeq;
        }
    }
}