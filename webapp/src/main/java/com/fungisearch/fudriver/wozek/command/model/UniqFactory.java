package com.fungisearch.fudriver.wozek.command.model;

import com.fungisearch.fudriver.exception.BarcodeNotReservedException;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.wozek.command.repository.UniqRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by marcin on 25.02.16.
 */
@Service
public class UniqFactory {
    @Autowired
    UniqRepository uniqRepository;

    @Autowired
    BeanValidator beanValidator;

    public Uniq.UniqBuilder builder(){
        Uniq.UniqBuilder uniqBuilder = new Uniq.UniqBuilder(uniqRepository,beanValidator);
        return uniqBuilder;
    }
/*
    public Uniq findUniq(long uniqId, long pickerId){
        //Uniq uniq = uniqRepository.find(pickerId,uniqId);
        Uniq uniq = uniqRepository.findTransactional(pickerId,uniqId);
        if(uniq == null){
            throw new BarcodeNotReservedException(pickerId, uniqId);
        }
        uniq.setBeanValidator(beanValidator);
        uniq.setUniqRepository(uniqRepository);
        return uniq;
    }
*/
    public Uniq findUniqTransactional(long uniqId, long pickerId){
        Uniq uniq = uniqRepository.findTransactional(pickerId,uniqId);
        if(uniq != null) {
            uniq.setBeanValidator(beanValidator);
            uniq.setUniqRepository(uniqRepository);
            return uniq;
        } else {
            return null;
        }
    }

    public Long findLastUsed(Long pickerId){
        Long highestUsed = uniqRepository.findHighestUniqIdForPicker(pickerId);
        if (highestUsed == null || !(highestUsed >0)){
            highestUsed = 1L;
        }
    return highestUsed;
    }
}
