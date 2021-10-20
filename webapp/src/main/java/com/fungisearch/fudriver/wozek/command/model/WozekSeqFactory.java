package com.fungisearch.fudriver.wozek.command.model;

import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.wozek.command.repository.WozekSeqRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by marcin on 31.03.16.
 */
@Service
public class WozekSeqFactory {

    @Autowired
    WozekSeqRepository wozekSeqRepository;

    @Autowired
    BeanValidator beanValidator;


    public WozekSeq.WozekSeqBuilder builder(){
        WozekSeq.WozekSeqBuilder builder = new WozekSeq.WozekSeqBuilder(wozekSeqRepository,beanValidator);
        return builder;
    }
}
