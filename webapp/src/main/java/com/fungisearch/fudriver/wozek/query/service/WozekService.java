package com.fungisearch.fudriver.wozek.query.service;

import com.fungisearch.fudriver.exception.AnotherActiveTrolleyException;
import com.fungisearch.fudriver.user.query.service.UserService;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.wozek.command.model.WozekSeq;
import com.fungisearch.fudriver.wozek.command.repository.WozekSeqRepository;
import com.fungisearch.fudriver.wozek.query.dao.WozekDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Created by idea on 15.03.16.
 */
@Service
@Transactional
public class WozekService {

    @Autowired
    WozekDao wozekDao;

    @Autowired
    WozekSeqRepository wozekSeqRepository;

    @Autowired
    BeanValidator beanValidator;

    @Autowired
    UserService userService;

    public void generateTrolleyNumbers(){
        Long userId = userService.getCurrentUserId();
        Map<Long, Long> wozekNumbers = wozekDao.getNumbersOnTrolley(userId);
        for(Map.Entry<Long, Long> entry: wozekNumbers.entrySet()){
            if(entry.getValue() == 0L){
                WozekSeq wozekSeq = new WozekSeq(wozekSeqRepository,beanValidator);
                Long trolleyNumber = wozekSeqRepository.create(wozekSeq);
                wozekDao.setTrolleyNumber(userId,entry.getKey(),trolleyNumber);
            } else {
                wozekDao.setTrolleyNumber(userId,entry.getKey(),entry.getValue());
            }
        }
    }

    public void onHold(Long wozekId){
        wozekDao.onHold(wozekId);
    }

    public void activate(Long wozekId){
        if(wozekDao.isTrolleyReadyToActivate(wozekId,userService.getCurrentUserId())) {
            wozekDao.activate(wozekId);
        } else {
            throw new AnotherActiveTrolleyException();
        }
    }



}
